

package dev.tawny.Voit.listener.packet;

import dev.tawny.Voit.Voit;
import dev.tawny.Voit.config.Config;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.manager.AlertManager;
import dev.tawny.Voit.manager.PlayerDataManager;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.ColorUtil;
import dev.tawny.Voit.util.type.Pair;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.event.impl.PostPlayerInjectEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.Bukkit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class NetworkManager extends PacketListenerDynamic {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public NetworkManager() {
        super(PacketEventPriority.MONITOR);
    }

    @Override
    public void onPacketPlayReceive(final PacketPlayReceiveEvent event) {
        final PlayerData data = PlayerDataManager.getInstance().getPlayerData(event.getPlayer());

        if (data != null) {
            if (PacketType.Play.Client.Util.isInstanceOfFlying(event.getPacketId())) {
                final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(event.getNMSPacket());

                if (Math.abs(wrapper.getX()) > 1.0E+7
                        || Math.abs(wrapper.getY()) > 1.0E+7
                        || Math.abs(wrapper.getZ()) > 1.0E+7
                        || Math.abs(wrapper.getPitch()) > 1.0E+7
                        || Math.abs(wrapper.getYaw()) > 1.0E+7) {
                    Bukkit.getScheduler().runTask(Voit.INSTANCE.getPlugin(), () -> event.getPlayer().kickPlayer(ColorUtil.translate(Config.ANTICRASHKICKEDMESSAGE)));
                    AlertManager.sendAntiExploitAlert("Player made a large movement that could crash the server/anticheat", "Large Movement");
                    return;
                }
            }

            executorService.execute(() -> Voit.INSTANCE.getReceivingPacketProcessor().handle(
                    data, new Packet(Packet.Direction.RECEIVE, event.getNMSPacket(), event.getPacketId(), event.getTimestamp())));
        }
    }

    @Override
    public void onPacketPlaySend(final PacketPlaySendEvent event) {
        final PlayerData data = PlayerDataManager.getInstance().getPlayerData(event.getPlayer());

        if (data != null) {
            executorService.execute(() -> Voit.INSTANCE.getSendingPacketProcessor().handle(
                    data, new Packet(Packet.Direction.SEND, event.getNMSPacket(), event.getPacketId(), event.getTimestamp()))
            );
            if (event.getPacketId() == PacketType.Play.Server.TRANSACTION) {
                WrappedPacketInTransaction transaction = new WrappedPacketInTransaction(event.getNMSPacket());
                short id = transaction.getActionNumber();

                // Vanilla always uses an ID starting from 1
                if (id <= 0) {

                    if (data.getConnectionProcessor().didWeSendThatTrans.remove((Short) id)) {
                        data.getConnectionProcessor().transactionsSent.add(new Pair<>(id, System.nanoTime()));
                        data.getConnectionProcessor().lastTransactionSent.getAndIncrement();
                    }
                }
            }
        }
    }

    @Override
    public void onPostPlayerInject(final PostPlayerInjectEvent event) {
        final ClientVersion version = event.getClientVersion();


    }

}

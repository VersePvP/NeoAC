package dev.tawny.Voit.check.impl.player.badpackets;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.utils.player.ClientVersion;

@CheckInfo(name = "BadPackets", type = "E", description = "Checks for attacking and digging.")
public class BadPacketsE extends Check {

    public BadPacketsE(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isUseEntity()) {
            if(PacketEvents.get().getPlayerUtils().getClientVersion(data.getPlayer()).isNewerThan(ClientVersion.v_1_8)) {
                return;
            }
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());
            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                final boolean sword = this.data.getPlayer().getItemInHand().getType().toString().contains("SWORD");
                final boolean invalid = this.data.getActionProcessor().isSendingDig();
                if (invalid && sword) {
                    if (increaseBuffer() > 5.0) {
                    }
                }
                else {
                    setBuffer(Math.max(getBuffer() - 1.0, 0.0));
                }
            }
        }
    }
}

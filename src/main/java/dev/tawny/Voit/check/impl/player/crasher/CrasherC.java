package dev.tawny.Voit.check.impl.player.crasher;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.config.Config;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.manager.AlertManager;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.ColorUtil;
import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;

@CheckInfo(name = "Crasher", description = "Checks for spamming data.", type = "C")
public final class CrasherC extends Check {

    public CrasherC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isCustomPayload()) {
            WrappedPacketInCustomPayload wrappedPacketInCustomPayload = new WrappedPacketInCustomPayload(packet.getRawPacket());
            if (wrappedPacketInCustomPayload.getData().length > 15000) {
                AlertManager.sendAntiExploitAlert("Checks for spamming data.", "Data Spam");
                data.getPlayer().kickPlayer(ColorUtil.translate(Config.ANTICRASHKICKEDMESSAGE));
            }

        }
    }
}
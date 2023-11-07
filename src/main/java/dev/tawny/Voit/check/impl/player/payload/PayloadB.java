package dev.tawny.Voit.check.impl.player.payload;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.config.Config;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.manager.AlertManager;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.ColorUtil;
import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;

@CheckInfo(name = "Payload", description = "Checks for spamming payloads.", type = "B")
public final class PayloadB extends Check {

    public PayloadB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isCustomPayload()) {
            WrappedPacketInCustomPayload wrappedPacketInCustomPayload = new WrappedPacketInCustomPayload(packet.getRawPacket());
            String payload = wrappedPacketInCustomPayload.getChannelName();
            if ((payload.equals("MC|BOpen") || payload.equals("MC|BEdit")) && (this.buffer += 2) > 4) {
                if (buffer > 2) {
                    AlertManager.sendAntiExploitAlert("Checks for clients spamming payloads.", "Payload Spam");
                    data.getPlayer().kickPlayer(ColorUtil.translate(Config.PAYLOADKICK));
                }
            }
        }
    }
}
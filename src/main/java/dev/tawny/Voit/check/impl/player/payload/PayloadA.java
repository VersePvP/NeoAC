package dev.tawny.Voit.check.impl.player.payload;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.config.Config;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.manager.AlertManager;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.ColorUtil;
import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;

@CheckInfo(name = "Payload", description = "Checks for invalid payload in clients.", type = "A")
public final class PayloadA extends Check {

    public PayloadA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isCustomPayload()) {
            WrappedPacketInCustomPayload wrappedPacketInCustomPayload = new WrappedPacketInCustomPayload(packet.getRawPacket());
            String payload = wrappedPacketInCustomPayload.getChannelName();
            if ((payload.equalsIgnoreCase("Remix")
                    || payload.contains("CRYSTAL")
                    || payload.contains("matrix")
                    || payload.equalsIgnoreCase("CRYSTAL|6LAKS0TRIES")
                    || payload.equalsIgnoreCase("CrystalWare")
                    || payload.equalsIgnoreCase("CRYSTAL|KZ1LM9TO")
                    || payload.equalsIgnoreCase("Misplace")
                    || payload.equalsIgnoreCase("reach")
                    || payload.equalsIgnoreCase("lmaohax")
                    || payload.equalsIgnoreCase("Reach Mod")
                    || payload.equalsIgnoreCase("cock")
                    || payload.equalsIgnoreCase("Vape v3")
                    || payload.equalsIgnoreCase("1946203560")
                    || payload.equalsIgnoreCase("#unbanearwax")
                    || payload.equalsIgnoreCase("EARWAXWASHERE")
                    || payload.equalsIgnoreCase("Cracked Vape")
                    || payload.equalsIgnoreCase("EROUAXWASHERE")
                    || payload.equalsIgnoreCase("moon:exempt")
                    || payload.equalsIgnoreCase("Vape")
                    || payload.equalsIgnoreCase("WDL|INIT")
                    || payload.equalsIgnoreCase("WDL|CONTROL")
                    || payload.equalsIgnoreCase("Bspkrs Client"))) {
                AlertManager.sendAntiExploitAlert("Checks for clients sending blocked payloads.", "Invalid Payload");
                data.getPlayer().kickPlayer(ColorUtil.translate(Config.PAYLOADKICK));
            }
        }
    }
}
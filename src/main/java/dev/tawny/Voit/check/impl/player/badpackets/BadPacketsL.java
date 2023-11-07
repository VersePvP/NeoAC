package dev.tawny.Voit.check.impl.player.badpackets;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "BadPackets", type = "L", description = "Checks for autistic people [0 delta & pitch]")
public final class BadPacketsL extends Check {

    public BadPacketsL(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if(packet.isPosLook() && data.getRotationProcessor().getDeltaPitch() == 0 && data.getRotationProcessor().getDeltaYaw() == 0 && !isExempt(ExemptType.TELEPORT_DELAY)) {
            if(increaseBuffer() > 5) {
                fail("Autistic People");
            }
        } else {
            decreaseBufferBy(0.15);
        }
    }
}
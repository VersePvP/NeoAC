package dev.tawny.Voit.check.impl.player.ground;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Ground", type = "C", description = "Checks for invalid vertical ground motion.")
public final class GroundC extends Check {
    public GroundC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final int groundTicks = data.getPositionProcessor().getGroundTicks();

            final double deltaY = data.getPositionProcessor().getDeltaY();
            final double lastY = data.getPositionProcessor().getLastY();

            final boolean step = deltaY % 0.015625 == 0.0 && lastY % 0.015625 == 0.0;

            final boolean exempt = isExempt(ExemptType.LONG_BUKKIT_PLACING, ExemptType.TELEPORT, ExemptType.BOAT, ExemptType.WEB, ExemptType.LIQUID, ExemptType.PISTON, ExemptType.CHUNK);
            final boolean invalid = groundTicks > 5 && deltaY != 0.0 && !step;

            if (invalid && !exempt) {
                if (increaseBuffer() > 1) {
                    fail();
                }
            } else {
                decreaseBufferBy(0.05);
            }
        }
    }
}
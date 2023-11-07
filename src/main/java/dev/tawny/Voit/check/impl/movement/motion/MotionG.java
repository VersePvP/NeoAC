package dev.tawny.Voit.check.impl.movement.motion;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Motion", description = "Checks for duplicate position packet.", type = "G", experimental = true)
public final class MotionG
        extends Check {
    public MotionG(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isPosition()) {
            boolean invalid;
            double deltaY = this.data.getPositionProcessor().getDeltaY();
            double lastDeltaY = this.data.getPositionProcessor().getLastDeltaY();
            boolean exempt = this.isExempt(ExemptType.UNDERBLOCK, ExemptType.PISTON, ExemptType.CREATIVE, ExemptType.SLIME, ExemptType.TELEPORT, ExemptType.CHUNK, ExemptType.VEHICLE);
            boolean bl = invalid = deltaY == -lastDeltaY && deltaY != 0.0;

            debug("deltaY: " + deltaY + "lastDeltaY: " +lastDeltaY);

            if (invalid && !exempt) {
                if (increaseBuffer() > 5) {
                    fail("deltaY:" + deltaY + " lastDeltaY:" + lastDeltaY);
                }
            } else {
                this.resetBuffer();
            }
        }
    }
}
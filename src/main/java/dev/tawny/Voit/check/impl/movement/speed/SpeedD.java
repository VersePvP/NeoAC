package dev.tawny.Voit.check.impl.movement.speed;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Speed", type = "D", description = "Checks for flaws in speeds by checking packets.")
public final class SpeedD extends Check
{
    public SpeedD(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            final double deltaY = this.data.getPositionProcessor().getDeltaY();
            final int groundTicks = this.data.getPositionProcessor().getGroundTicks();
            final int airTicks = this.data.getPositionProcessor().getClientAirTicks();
            final boolean exempt = this.isExempt(ExemptType.SLIME);
            final boolean invalid = deltaY == 0.0 && groundTicks == 1 && airTicks == 0;
            if (invalid && !exempt) {
                if (increaseBuffer() > 5) {
                    fail();
                }
            }
            else {
                this.resetBuffer();
            }
        }
    }
}
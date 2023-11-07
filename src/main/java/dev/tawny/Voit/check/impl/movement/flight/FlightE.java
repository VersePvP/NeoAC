package dev.tawny.Voit.check.impl.movement.flight;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Flight", type = "E", description = "Predicts flying")
public final class FlightE extends Check {

    public FlightE(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying()) {

            double yDelta = data.getPositionProcessor().getDeltaY();
            boolean isInAir = data.getPositionProcessor().isInAir();
            double ticks = data.getPositionProcessor().getAirTicks();
            boolean exempt = isExempt(ExemptType.NEARSTAIRS, ExemptType.FLYING, ExemptType.CHUNK, ExemptType.CREATIVE, ExemptType.TELEPORT_DELAY)
                    && (data.getPositionProcessor().getSinceTeleportTicks() > 40);

            debug("inAir:" + isInAir + "   ticks:" + data.getPositionProcessor().getAirTicks());

            if (isInAir && ticks > 15 && !exempt && yDelta >= 0) {
                if (increaseBuffer() > 2) {
                    fail(ticks);
                }
            }
            else {
                decreaseBufferBy(0.35);
            }
        }
    }
}

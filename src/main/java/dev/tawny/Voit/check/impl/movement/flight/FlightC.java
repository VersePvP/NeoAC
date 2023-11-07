package dev.tawny.Voit.check.impl.movement.flight;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Flight", type = "C", description = "Checks if player isn't falling in air.")
public final class FlightC extends Check {

    private double stableY;

    public FlightC(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isPosition()) {
            if (this.isExempt(ExemptType.FLYING, ExemptType.TELEPORT_DELAY, ExemptType.CREATIVE, ExemptType.PLACING)) {
                return;
            }
            this.stableY = this.data.getPositionProcessor().getY() == this.data.getPositionProcessor().getLastY() && this.data.getPositionProcessor().isInAir() ? (this.stableY += 1.0) : 0.0;
            if (this.stableY > 2.0) {
                fail(this.stableY);
            }
        }
    }
}
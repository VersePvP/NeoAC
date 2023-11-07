package dev.tawny.Voit.check.impl.player.ground;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Ground", type = "B", description = "Checks for spoofing ground distance.")
public final class GroundB extends Check {

    private double serverFallDistance;

    public GroundB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final double deltaY = data.getPositionProcessor().getDeltaY();

            final boolean inAir = data.getPositionProcessor().isInAir();
            final boolean nearStair = data.getPositionProcessor().isNearStair();
            final boolean inLiquid = data.getPositionProcessor().isInLiquid();

            if (deltaY < 0.0 && !inAir && !nearStair && !inLiquid) {
                serverFallDistance -= deltaY;
            } else {
                serverFallDistance = 0.0;
            }

            final double serverFallDistance = this.serverFallDistance;
            final double clientFallDistance = data.getPlayer().getFallDistance();

            final boolean exempt = isExempt(ExemptType.FLYING, ExemptType.CREATIVE, ExemptType.WEB, ExemptType.CLIMBABLE,ExemptType.LIQUID, ExemptType.BOAT, ExemptType.VOID, ExemptType.VEHICLE, ExemptType.CHUNK, ExemptType.PISTON);
            final boolean invalid = Math.abs(serverFallDistance - clientFallDistance) - clientFallDistance >= 1.0 && inAir;

            if (invalid && !exempt) {
                if (increaseBuffer() > 3) {
                    fail();
                }
            } else {
                decreaseBufferBy(0.25);
            }
        }
    }
}
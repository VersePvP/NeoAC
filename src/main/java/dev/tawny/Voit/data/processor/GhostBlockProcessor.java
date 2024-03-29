

package dev.tawny.Voit.data.processor;

import dev.tawny.Voit.config.Config;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.util.PlayerUtil;
import lombok.Getter;


@Getter
public final class GhostBlockProcessor {

    private final PlayerData data;

    private boolean onGhostBlock, yGround, lastYGround;

    private int ghostTicks, buffer;

    public GhostBlockProcessor(final PlayerData data) {
        this.data = data;
    }

    public void handleFlying() {
        if (!Config.GHOST_BLOCK_ENABLED) return;

        onGhostBlock = false;

        if(Config.GHOST_BLOCK_MODE== Mode.VoitAC) {
            if(PlayerUtil.isOnBoat(data) || data.getExemptProcessor().isExempt(ExemptType.VEHICLE, ExemptType.WEB, ExemptType.CLIMBABLE, ExemptType.LIQUID, ExemptType.SLIME, ExemptType.TELEPORT, ExemptType.VELOCITY)) {
                return;
            }

            final boolean isBridingUp = data.getPositionProcessor().isPlacementUnder() && data.getPositionProcessor().getDeltaY() > 0.0;

            final boolean onGhostBlock = data.getPositionProcessor().isOnGround() && data.getPositionProcessor().getY() % 0.015625 < 0.03 && data.getPositionProcessor().isInAir();

            final double deltaY = data.getPositionProcessor().getDeltaY();
            final double lastDeltaY = data.getPositionProcessor().getLastDeltaY();

            final int airTicks = data.getPositionProcessor().getAirTicks();

            double predictedY = (lastDeltaY - 0.08) * 0.98F;
            if (Math.abs(predictedY) < 0.005) predictedY = 0.0;

            final boolean underGhostBlock = data.getPositionProcessor().getSinceBlockNearHeadTicks() > 3
                    && Math.abs(deltaY - ((-0.08) * 0.98F)) < 1E-5
                    && Math.abs(deltaY - predictedY) > 1E-5;

            this.onGhostBlock = onGhostBlock || underGhostBlock;

            if (onGhostBlock && airTicks > 5 && !isBridingUp) {
                data.dragDown();
            }
        }
    }

    public enum Mode {
        VoitAC
    }
}

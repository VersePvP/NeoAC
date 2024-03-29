package dev.tawny.Voit.check.impl.movement.motion;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.BlockUtil;
import dev.tawny.Voit.util.PlayerUtil;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Motion", type = "C", description = "Checks for invalid strafing.")
public final class  MotionC extends Check {

    private double blockSlipperiness = 0.91;

    public MotionC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            if(PacketEvents.get().getPlayerUtils().getClientVersion(data.getPlayer()).isNewerThanOrEquals(ClientVersion.v_1_17)) {
                return;
            }
            final Player player = data.getPlayer();

            final double deltaX = data.getPositionProcessor().getDeltaX();
            final double deltaZ = data.getPositionProcessor().getDeltaZ();

            final double deltaXZ = data.getPositionProcessor().getDeltaXZ();

            final double lastDeltaX = data.getPositionProcessor().getLastDeltaX();
            final double lastDeltaZ = data.getPositionProcessor().getLastDeltaZ();

            double blockSlipperiness = this.blockSlipperiness;
            double attributeSpeed = 1.d;

            final boolean onGround = data.getPositionProcessor().isOnGround();
            final boolean sprinting = data.getActionProcessor().isSprinting();

            final int airTicks = data.getPositionProcessor().getClientAirTicks();
            final int groundTicks = data.getPositionProcessor().getGroundTicks();

            attributeSpeed += PlayerUtil.getPotionLevel(player, PotionEffectType.SPEED) * (float) 0.2 * attributeSpeed;
            attributeSpeed += PlayerUtil.getPotionLevel(player, PotionEffectType.SLOW) * (float) -.15 * attributeSpeed;

            if (onGround) {
                blockSlipperiness *= 0.91f;

                if (sprinting) attributeSpeed *= 1.3;
                attributeSpeed *= 0.16277136 / Math.pow(blockSlipperiness, 3);


                attributeSpeed *= data.getPlayer().getWalkSpeed() / 2.0;

                attributeSpeed = 1000;
            } else {
                attributeSpeed = sprinting ? 0.0263 : 0.02;

                blockSlipperiness = 0.91f;
            }

            final double predictedDeltaX = lastDeltaX * blockSlipperiness + attributeSpeed;
            final double predictedDeltaZ = lastDeltaZ * blockSlipperiness + attributeSpeed;

            final double diffX = deltaX - predictedDeltaX;
            final double diffZ = deltaZ - predictedDeltaZ;

            final double limit = sprinting ? 0.026 : 0.02;

            final boolean exempt = this.isExempt(ExemptType.TELEPORT, ExemptType.PISTON, ExemptType.FLYING, ExemptType.UNDERBLOCK, ExemptType.VEHICLE, ExemptType.CLIMBABLE, ExemptType.LIQUID, ExemptType.VELOCITY, ExemptType.UNDERBLOCK, ExemptType.CHUNK);
            final boolean invalid = (diffX > limit || diffZ > limit) && deltaXZ > .175 && (airTicks > 2 || groundTicks > 2);

            if (invalid && !exempt) {
                if (increaseBuffer() > 3) {
                    fail(String.format("diffX: %.3f diffZ: %.3f airT: %s groundT: %s", diffX, diffZ, airTicks, groundTicks));
                }
            } else {
                decreaseBufferBy(0.1);
            }

            final double x = data.getPositionProcessor().getX();
            final double y = data.getPositionProcessor().getY();
            final double z = data.getPositionProcessor().getZ();

            final Location blockLocation = new Location(data.getPlayer().getWorld(), x, Math.floor(y - 0.1), z);

            this.blockSlipperiness = BlockUtil.getBlockFriction(blockLocation) * 0.91F;

            debug(String.format("diffX: %.2f diffZ: %.2f buffer: %.2f airT: %s", diffX, diffZ, getBuffer(), airTicks));
        }
    }
}
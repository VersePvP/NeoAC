package dev.tawny.Voit.check.impl.combat.aim;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;

import java.util.function.Predicate;

@CheckInfo(name = "Aim", description = "Checks for precise aim.", type = "L", experimental = true)
public final class AimL extends Check {

    private final Predicate<Float> validRotation = rotation -> rotation > 3F && rotation < 35F;

    public AimL(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if(packet.isRotation()) {
            final float deltaPitch = Math.abs(data.getRotationProcessor().getDeltaPitch());
            final float deltaYaw =  Math.abs(data.getRotationProcessor().getDeltaYaw() % 360F);

            final float pitch = Math.abs(data.getRotationProcessor().getPitch());

            final boolean invalidPitch = deltaPitch < 0.009 && validRotation.test(deltaYaw);
            final boolean invalidYaw = deltaYaw < 0.009 && validRotation.test(deltaPitch);

            final boolean exempt = isExempt(ExemptType.VEHICLE, ExemptType.TELEPORT);
            final boolean invalid = !exempt && (invalidPitch || invalidYaw) && pitch < 89F;
            final boolean overMaxSens = (data.getRotationProcessor().getSensitivity() > 100);

            debug(deltaYaw + " " + deltaPitch);

            if (invalid && !overMaxSens) {
                if (increaseBuffer() > 10) {
                    fail(deltaYaw + " " + deltaPitch);
                }
            } else {
                decreaseBufferBy(1);
            }
        }
    }
}
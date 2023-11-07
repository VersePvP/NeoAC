package dev.tawny.Voit.check.impl.combat.aim;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.data.processor.RotationProcessor;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.MathUtil;

@CheckInfo(name = "Aim", description = "Checks for invalid gcd usage.", type = "I", experimental = true)
public final class AimI extends Check {

    public AimI(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isRotation() && System.currentTimeMillis() - data.getCombatProcessor().getLastAttack() <= 850) {
            final RotationProcessor processor = data.getRotationProcessor();

            final float deltaYaw = processor.getDeltaYaw();
            final float deltaPitch = processor.getDeltaPitch();
            final float lastDeltaPitch = processor.getLastDeltaPitch();

            final double divisorPitch = MathUtil.getGcd((long) (deltaPitch * MathUtil.EXPANDER), (long) (lastDeltaPitch * MathUtil.EXPANDER));

            final double divisor = Math.abs(processor.getGcd() / divisorPitch);

            debug("divisor=" + divisor);

            if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 2 && deltaPitch < 2) {

                final boolean exempt = isExempt(ExemptType.CINEMATIC);

                final boolean invalid = divisor > 5E-7 && !exempt;

                if (invalid) {
                    if (buffer++ > 10) {
                        fail(divisor);
                    }
                } else {
                    decreaseBufferBy(0.05);
                }
            }
        }
    }
}
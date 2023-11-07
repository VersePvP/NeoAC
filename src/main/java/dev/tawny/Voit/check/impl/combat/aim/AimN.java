package dev.tawny.Voit.check.impl.combat.aim;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.data.processor.RotationProcessor;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.MathUtil;

@CheckInfo(name = "Aim", description = "Checks for GCD bypasses.", type = "N", experimental = true)
public class AimN extends Check {
    public AimN(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isRotation() && System.currentTimeMillis() - data.getCombatProcessor().getLastAttack() < 850) {
            final RotationProcessor processor = data.getRotationProcessor();

            final float deltaPitch = processor.getDeltaPitch();
            final float lastDeltaPitch = processor.getLastDeltaPitch();

            if (deltaPitch > 1 && !isExempt(ExemptType.TELEPORT)) {
                final long expanded = (long) (deltaPitch * MathUtil.EXPANDER);
                final long lastExpanded = (long) (lastDeltaPitch * MathUtil.EXPANDER);

                final long gcd = MathUtil.getGcd(expanded, lastExpanded);

                final double divisor = gcd / MathUtil.EXPANDER;

                final double moduloPitch = Math.abs(processor.getPitch() % divisor);

                debug("modulo=" + moduloPitch);

                if (moduloPitch < 1.2E-5) {
                    if (buffer++ > 3) {
                        fail("modulo=" + moduloPitch);
                    }
                }
            } else {
                decreaseBufferBy(0.5);
            }
        }
    }
}
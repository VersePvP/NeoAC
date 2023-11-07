package dev.tawny.Voit.check.impl.combat.aim;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.data.processor.RotationProcessor;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.MathUtil;

@CheckInfo(name = "Aim", description = "Checks for invalid sensitivity.", type = "D", experimental = true)
public class AimD
        extends Check {

    public AimD(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isUseEntity()) {
            final RotationProcessor processor = data.getRotationProcessor();

            final boolean cinematic = processor.isCinematic();

            if(cinematic && data.getRotationProcessor().getSensitivity() < 0)
                return;

            final boolean attack = data.getCombatProcessor().getLastAttackTick() < 10;

            final float deltaYaw = processor.getDeltaYaw();
            final float lastDeltaYaw = processor.getLastDeltaYaw();
            final float deltaPitch = processor.getDeltaPitch();

            final double divisorYaw = MathUtil.getGcd((long) (deltaYaw * MathUtil.EXPANDER), (long) (lastDeltaYaw * MathUtil.EXPANDER));

            final double epik = data.getRotationProcessor().getGcd() / divisorYaw;
            debug("epik=" + epik);
            if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 1 && deltaPitch < 1 && attack && !isExempt(ExemptType.CINEMATIC)) {
                if (epik > 9.9E-7) {
                    if (buffer++ > 6) {
                        buffer = 0;
                        fail("epik=" + epik);
                    }
                } else {
                    decreaseBufferBy(2);
                }
            }
        }
    }
}
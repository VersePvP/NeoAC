package dev.tawny.Voit.check.impl.combat.aim;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.data.processor.RotationProcessor;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.MathUtil;
import dev.tawny.Voit.util.TimeUtils;

@CheckInfo(name = "Aim", description = "Checks for jitter.", type = "C")
public class AimC extends Check {
    public AimC(PlayerData data) {
        super(data);

    }

    @Override
    public void handle(Packet packet) {
        if (packet.isRotation() && TimeUtils.elapsed(data.getCombatProcessor().getLastAttack()) <= 1000L) {
            final RotationProcessor processor = data.getRotationProcessor();

            final float deltaYaw = processor.getDeltaYaw();
            final float lastDeltaYaw = processor.getLastDeltaYaw();
            final float deltaPitch = processor.getDeltaPitch();

            final double divisorYaw = MathUtil.getGcd((long) (deltaYaw * MathUtil.EXPANDER), (long) (lastDeltaYaw * MathUtil.EXPANDER));

            final double epik = data.getRotationProcessor().getGcd() / divisorYaw;
            debug(epik);
            if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 1 && deltaPitch < 1) {
                if (epik > 1.0E-7) {
                    if (buffer++ > 10) {
                        buffer = 5;
                        fail(epik);
                    }
                } else {
                    decreaseBufferBy(2);
                }
            }
        }
    }
}

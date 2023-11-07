package dev.tawny.Voit.check.impl.combat.aim;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.MathUtil;
import dev.tawny.Voit.util.type.EvictingList;

@CheckInfo(name = "Aim", description = "Checks for lock aim.", type = "J", experimental = true)
public class AimJ extends Check {
    public AimJ(PlayerData data) {
        super(data);
    }

    private final EvictingList<Float> yawAccelSamples = new EvictingList<>(20);
    private final EvictingList<Float> pitchAccelSamples = new EvictingList<>(20);

    @Override
    public void handle(Packet packet) {
        if (packet.isRotation() && isExempt(ExemptType.COMBAT)) {
            float yawAccel = this.data.getRotationProcessor().getJoltYaw();
            float pitchAccel = this.data.getRotationProcessor().getJoltPitch();
            float deltaYaw = this.data.getRotationProcessor().getDeltaYaw() % 360.0F;

            this.yawAccelSamples.add(yawAccel);
            this.pitchAccelSamples.add(pitchAccel);

            if (this.yawAccelSamples.isFull() && this.pitchAccelSamples.isFull()) {

                double yawAccelAverage = this.yawAccelSamples.stream().mapToDouble(value -> value).average().orElse(0.0D);
                double pitchAccelAverage = this.pitchAccelSamples.stream().mapToDouble(value -> value).average().orElse(0.0D);

                double yawAccelDeviation = MathUtil.getStandardDeviation(this.yawAccelSamples);
                double pitchAccelDeviation = MathUtil.getStandardDeviation(this.pitchAccelSamples);

                boolean exemptRotation = (deltaYaw < 1.5F);
                boolean averageInvalid = (yawAccelAverage < 1.0D || (pitchAccelAverage < 1.0D && !exemptRotation));
                boolean deviationInvalid = (yawAccelDeviation < 5.0D && pitchAccelDeviation > 5.0D && !exemptRotation);

                final String format = String.format(
                        "ya=%.2f, pa=%.2f, yd=%.2f, pd=%.2f",
                        yawAccelAverage, pitchAccelAverage, yawAccelDeviation, pitchAccelDeviation);
                debug(format);
                if (averageInvalid && deviationInvalid) {
                    if (increaseBuffer() > 10.0D) {
                        fail(format);
                        if (buffer > 6.0D)
                            decreaseBuffer();
                    }
                } else {
                    decreaseBufferBy(0.75D);
                }
            }
        }
    }
}
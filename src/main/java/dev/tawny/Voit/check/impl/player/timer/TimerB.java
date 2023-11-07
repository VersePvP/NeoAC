package dev.tawny.Voit.check.impl.player.timer;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.MathUtil;
import dev.tawny.Voit.util.type.EvictingList;

@CheckInfo(name = "Timer", type = "B", description = "Checks packet delay between packets.")
public final class TimerB extends Check {

    private final EvictingList<Long> samples = new EvictingList<>(50);
    private long lastFlying;

    public TimerB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            final long now = now();

            final boolean exempt = this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.VEHICLE);

            debug(exempt);
            handle:
            {
                if (exempt) break handle;

                final long delay = now - lastFlying;

                if (delay > 0) {
                    samples.add(delay);
                }

                if (samples.isFull()) {
                    final double average = MathUtil.getAverage(samples);
                    final double deviation = MathUtil.getStandardDeviation(samples);

                    final double speed = 50.0 / average;

                    final boolean invalid = deviation < 40.0 && speed < 0.6 && !Double.isNaN(deviation);

                    if (invalid) {
                        if (increaseBuffer() > 30) {
                            fail("Speed: " + speed);
                            multiplyBuffer(0.50);
                        }
                    } else {
                        decreaseBufferBy(10);
                    }
                }
            }

            this.lastFlying = now;
        } else if (packet.isTeleport()) {
            samples.add(125L);
        }
    }
}
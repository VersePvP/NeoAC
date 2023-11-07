package dev.tawny.Voit.check.impl.player.timer;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.MathUtil;
import dev.tawny.Voit.util.type.EvictingList;

@CheckInfo(name = "Timer", type = "C", description = "Checks for game speed which is too slow.")
public class TimerC extends Check {

    private final EvictingList<Long> samples = new EvictingList<>(50);
    private long lastFlyingTime;

    public TimerC(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying()) {
            final long now = now();
            final long delta = now - lastFlyingTime;
            samples.add(delta);
            if (samples.isFull()) {
                final double average = samples.stream().mapToDouble(value -> value).average().orElse(1.0);
                final double speed = 50 / average;
                final double deviation = MathUtil.getStandardDeviation(samples);
                if (speed <= 0.75 && deviation < 50) {
                    if (increaseBuffer() > 10) {
                        fail(String.format("Speed: %.2f Deviation: %.2f", speed, deviation));
                    }
                }
                else {
                    decreaseBufferBy(2);
                }
            }
            lastFlyingTime = now;
        }
    }
}
package dev.tawny.Voit.check.impl.combat.autoclicker;


import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.MathUtil;
import dev.tawny.Voit.util.type.EvictingList;

@CheckInfo(name = "AutoClicker", type = "B", description = "Checks the outliers on your clicks")
public class AutoClickerB extends Check {

    private final EvictingList<Long> tickList = new EvictingList<>(30);
    private double lastDeviation;
    private int tick;

    public AutoClickerB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isArmAnimation()) {
            final boolean exempt = isExempt(ExemptType.DROP, ExemptType.AUTOCLICKER);
            if (!exempt) tickList.add((long) (tick * 50.0));

            if (tickList.isFull()) {
                final double deviation = MathUtil.getStandardDeviation(tickList);
                final double difference = Math.abs(deviation - lastDeviation);

                final boolean invalid = difference < 6;

                debug("diff: " +deviation+ "devi: " +deviation);

                if (invalid && !exempt) {
                    if (increaseBuffer() > 15) {
                        fail(deviation);
                    }
                } else {
                    decreaseBuffer();
                }

                lastDeviation = deviation;
            }
        } else if (packet.isFlying()) {
            tick++;
        }
    }
}
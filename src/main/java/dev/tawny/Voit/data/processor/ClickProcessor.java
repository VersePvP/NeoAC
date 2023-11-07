

package dev.tawny.Voit.data.processor;

import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.util.MathUtil;
import dev.tawny.Voit.util.type.EvictingList;
import lombok.Getter;

@Getter
public final class ClickProcessor {

    private final PlayerData data;
    private long lastSwing = -1;
    private long delay;
    private int movements;
    private double cps, rate;
    private final EvictingList<Integer> clicks = new EvictingList<>(10);

    public ClickProcessor(final PlayerData data) {
        this.data = data;
    }

    public void handleArmAnimation() {
        if (!data.getActionProcessor().isDigging() && !data.getActionProcessor().isPlacing()) {
            if (lastSwing > 0) {
                delay = System.currentTimeMillis() - lastSwing;
            }
            lastSwing = System.currentTimeMillis();
        }

        final boolean exempt = data.getExemptProcessor().isExempt(ExemptType.PLACING, ExemptType.DIGGING);

        click: {
            if (exempt || movements > 5) break click;

            clicks.add(movements);
        }

        if (clicks.size() > 5) {
            final double cps = MathUtil.getCps(clicks);
            final double rate = cps * movements;

            this.cps = cps;
            this.rate = rate;
        }

        movements = 0;
    }

    public void handleFlying() {
        movements++;
    }
}

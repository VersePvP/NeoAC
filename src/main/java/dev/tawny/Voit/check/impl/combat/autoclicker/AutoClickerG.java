package dev.tawny.Voit.check.impl.combat.autoclicker;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.MathUtil;
import dev.tawny.Voit.util.type.Pair;

import java.util.ArrayDeque;
import java.util.List;

@CheckInfo(name = "AutoClicker", type = "G", description = "Checks for outliers and high dupl", experimental = true)
public class AutoClickerG extends Check {
    private final ArrayDeque<Integer> samples = new ArrayDeque<>();
    private int ticks;

    public AutoClickerG(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isArmAnimation()) {
            final boolean digging = isExempt(ExemptType.AUTOCLICKER);
            if (ticks < 4 && data.getClickProcessor().getCps() > 10 && !digging && ticks != 0) {
                samples.add(ticks);
            }
            if (samples.size() == 20) {
                final Pair<List<Double>, List<Double>> outlierPair = MathUtil.getOutliers(samples);

                final int outliers = outlierPair.getX().size() + outlierPair.getY().size();
                final int duplicates = (int) (samples.size() - samples.stream().distinct().count());
                debug("outliers=" + outliers + ", dupl=" + duplicates);
                if (outliers < 2 && duplicates > 16) {
                    if ((buffer += 10) > 50) {
                        fail("outliers=" + outliers + ", duplicates=" + duplicates);
                    }
                } else {
                    buffer = Math.max(buffer - 15, 0);
                }
                samples.clear();
            }
            ticks = 0;
        } else if (packet.isFlying()) {
            ticks++;
        }
    }
}

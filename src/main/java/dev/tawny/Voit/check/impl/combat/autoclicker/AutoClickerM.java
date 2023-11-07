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


@CheckInfo(name = "AutoClicker", experimental = true, description = "Checks for invalid consistency while clicking.", type = "M")
public final class AutoClickerM extends Check {

    private final ArrayDeque<Integer> samples = new ArrayDeque<>();
    private int ticks;

    public AutoClickerM(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isArmAnimation() && !isExempt(ExemptType.AUTOCLICKER)) {
            if (ticks < 4) {
                samples.add(ticks);
            }

            if (samples.size() == 20) {
                final Pair<List<Double>, List<Double>> outlierPair = MathUtil.getOutliers(samples);

                final int outliers = outlierPair.getX().size() + outlierPair.getY().size();
                final int duplicates = (int) (samples.size() - samples.stream().distinct().count());

                debug("outliers=" + outliers + " dupl=" + duplicates);

                if (outliers < 3 && duplicates > 18) {
                    if ((buffer += 20) > 60) {
                        fail("outliers=" + outliers + " dupl=" + duplicates);
                    }
                } else {
                    buffer = Math.max(buffer - 8, 0);
                }
                samples.clear();
            }

            ticks = 0;
        } else if (packet.isFlying()) {
            ++ticks;
        }
    }
}
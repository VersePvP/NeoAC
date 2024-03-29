package dev.tawny.Voit.check.impl.combat.autoclicker;

import com.google.common.collect.Lists;
import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.MathUtil;
import dev.tawny.Voit.util.type.Pair;

import java.util.Deque;
import java.util.List;

@CheckInfo(name = "AutoClicker", type = "L", description = "Checks for abnormal clicking")
public class AutoClickerL extends Check {
    public AutoClickerL(PlayerData data) {
        super(data);
    }

    private int movements = 0;
    private final Deque<Integer> samples = Lists.newLinkedList();

    @Override
    public void handle(Packet packet) {
        if (packet.isArmAnimation()) {
            final boolean valid = movements < 4 && !isExempt(ExemptType.AUTOCLICKER) && movements != 0;

            if (valid) samples.add(movements);

            // Sample size is assigned to 15
            if (samples.size() == 15) {
                final Pair<List<Double>, List<Double>> outlierPair = MathUtil.getOutliers(samples);

                final double skewness = MathUtil.getSkewness(samples);
                final double kurtosis = MathUtil.getKurtosis(samples);
                final double outliers = outlierPair.getX().size() + outlierPair.getY().size();

                String debug = String.format(
                        "sk=%.2f, ku=%.2f, ou=%.2f",
                        skewness, kurtosis, outliers
                );
                debug(debug);

                // See if skewness and kurtosis is exceeding a specific limit.
                if (skewness < 0.035 && kurtosis < 0.1 && outliers < 2) fail(debug);

                samples.clear();
            }
            movements = 0;
        } else if (packet.isFlying()) {
            ++movements;
        }
    }
}

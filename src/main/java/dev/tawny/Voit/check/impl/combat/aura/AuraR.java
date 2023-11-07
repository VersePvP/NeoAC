package dev.tawny.Voit.check.impl.combat.aura;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Aura", description = "Checks for values that match auras.", type = "R", experimental = true)
public class AuraR extends Check {

    private int ticks, invalidTicks, lastTicks, totalTicks;

    public AuraR(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            ++ticks;
        } else if (packet.isUseEntity()) {
            if (ticks <= 8) {
                if (lastTicks == ticks) {
                    ++invalidTicks;
                }

                if (++totalTicks >= 25) {
                    if (invalidTicks > 22) {
                        fail();
                    }

                    totalTicks = 0;
                    invalidTicks = 0;
                }

                lastTicks = ticks;
            }

            ticks = 0;
        }
    }
}
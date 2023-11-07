package dev.tawny.Voit.check.impl.combat.aura;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.TimeUtils;


@CheckInfo(name = "Aura", type = "M", description = "Checks if pitch doesn't match angle")
public class AuraM extends Check {

    public AuraM(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isPosLook()) {
            if (TimeUtils.elapsed(data.getCombatProcessor().getLastAttack()) <= 850L && !isExempt(ExemptType.TELEPORT)) {
                /*
                Lmao patches sigma, lb, impact HEHEHEHEHEHEHEHEEHEHHEEHEHHEHEHHEHEH
                 */

                if (data.getRotationProcessor().getPitch() == 0) {
                    if (increaseBuffer() > 2) fail("0");
                } else {
                    decreaseBufferBy(0.25);
                }
            }
        }
    }
}
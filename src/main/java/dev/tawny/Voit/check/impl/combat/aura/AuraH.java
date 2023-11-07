package dev.tawny.Voit.check.impl.combat.aura;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Aura", type = "H", description = "Missing ARM ANIMATION packet")
public class AuraH extends Check {
    public AuraH(PlayerData data) {
        super(data);
    }

    private boolean armAnimation, useEntity;

    @Override
    public void handle(Packet packet) {
        if (packet.isArmAnimation()) {
            armAnimation = true;
        } else if (packet.isUseEntityAttack()) {
            useEntity = true;
        } else if (packet.isFlyingType()) {
            if (useEntity && !armAnimation)
                fail("No Swing");
            armAnimation = false;
            useEntity = false;
        }
    }
}
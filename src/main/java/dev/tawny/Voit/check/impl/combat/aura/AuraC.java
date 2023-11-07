package dev.tawny.Voit.check.impl.combat.aura;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Aura", type = "C", description = "Missing ENTITY INTERACT packet")
public class AuraC extends Check {
    public AuraC(PlayerData data) {
        super(data);
    }

    private boolean sentAttack, sentBlock, sentInteract;

    @Override
    public void handle(Packet packet) {
        if (packet.isBlockPlace()) {
            sentBlock = true;
        } else if (packet.isUseEntityAttack()) {
            sentAttack = true;
        } else if (packet.isUseEntityInteractAt() || packet.isUseEntityInteract()) {
            sentInteract = true;
        } else if (packet.isFlyingType()) {
            if (sentBlock && sentAttack && !sentInteract) {
                fail("Autoblock");
            }
            sentAttack = false;
            sentBlock = false;
            sentInteract = false;
        }
    }
}
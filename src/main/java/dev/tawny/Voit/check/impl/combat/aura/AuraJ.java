package dev.tawny.Voit.check.impl.combat.aura;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.PlayerUtil;

@CheckInfo(name = "Aura", type = "J", description = "Missing USE ENTITY Interact")
public class AuraJ extends Check {
    public AuraJ(PlayerData data) {
        super(data);
    }

    private boolean sentInteract;
    private boolean sentInteractAt;

    @Override
    public void handle(Packet packet) {

        if (PlayerUtil.is1_7(data.getPlayer())) return;

        if (packet.isUseEntityInteractAt()) {
            sentInteract = true;
        } else if (packet.isUseEntityInteract()) {
            sentInteractAt = true;
        } else if (packet.isFlyingType()) {
            if (sentInteract != sentInteractAt)
                fail("Interact: " + sentInteract + "\n" + "Interact At:" + sentInteractAt);
            sentInteract = false;
            sentInteractAt = false;
        }
    }
}
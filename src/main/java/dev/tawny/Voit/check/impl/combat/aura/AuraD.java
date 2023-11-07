package dev.tawny.Voit.check.impl.combat.aura;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.PlayerUtil;

@CheckInfo(name = "Aura", type = "D", description = "Invalid Sword Block Movement")
public class AuraD extends Check {
    public AuraD(PlayerData data) {
        super(data);
    }

    private boolean blocked, stopSprint;

    //Fix, I think it falses when you block on the ground
    //It could also be caused by desync but I doubt that
    @Override
    public void handle(Packet packet) {
        if (PlayerUtil.isHoldingSword(data.getPlayer()) && data.getActionProcessor().isSprinting() && packet.isBlockPlace()) {
            blocked = true;
        } else if (packet.isStopSprinting()) {
            stopSprint = true;
        } else if (packet.isFlyingType()) {

            //if (blocked && !stopSprint)
            //flag();
            blocked = false;
            stopSprint = false;
        }
    }
}
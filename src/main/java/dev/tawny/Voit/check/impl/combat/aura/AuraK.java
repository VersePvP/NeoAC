package dev.tawny.Voit.check.impl.combat.aura;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

import java.math.BigDecimal;

@CheckInfo(name = "Aura", type = "K", description = "Extremely Precise Strafe")
public class AuraK extends Check {
    public AuraK(PlayerData data) {
        super(data);
    }

    private int sentLooks, buffer;
    BigDecimal previousAngle, bigDecimal;

    @Override
    public void handle(Packet packet) {
        if (packet.isLook() || packet.isPositionLook()) {
            sentLooks++;
        }
        if (packet.isPositionLook()) {

            previousAngle = bigDecimal;
            bigDecimal = BigDecimal.valueOf(Math.abs(data.getPositionProcessor().getLastMoveAngle()));

            if (sentLooks < 30 || previousAngle.equals(bigDecimal)) return;

            String moveAngle = bigDecimal + "";

            if (moveAngle.contains("E") || moveAngle.equalsIgnoreCase("0.0")) {
                if ((buffer += 5) > 20) {
                    fail();
                    buffer = Math.max(30, buffer);
                }
            } else {
                buffer = Math.max(0, buffer - 1);
            }
        }
    }
}
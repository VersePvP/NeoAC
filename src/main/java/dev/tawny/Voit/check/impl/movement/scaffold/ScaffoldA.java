package dev.tawny.Voit.check.impl.movement.scaffold;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Scaffold", type = "A", description = "Detects unusual changes in vertical movement while placing blocks")
public final class ScaffoldA extends Check {

    private double lastDeltaY;

    public ScaffoldA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying() && data.getActionProcessor().isPlacing()) {
            double currentDeltaY = Math.abs(data.getPositionProcessor().getDeltaY());

            if (lastDeltaY > 0.0 && currentDeltaY > 0.0 && currentDeltaY > lastDeltaY * 2.0) {
                fail(String.format("currentDeltaY=%.2f, lastDeltaY=%.2f", currentDeltaY, lastDeltaY));
            }

            lastDeltaY = currentDeltaY;
        }
    }
}

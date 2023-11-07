package dev.tawny.Voit.check.impl.movement.scaffold;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Scaffold", type = "J", description = "Detects invalid acceleration while scaffolding")
public final class ScaffoldJ extends Check {

    private double lastDeltaX;
    private double lastDeltaZ;

    public ScaffoldJ(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying() && data.getActionProcessor().isPlacing()) {
            double currentDeltaX = data.getPositionProcessor().getDeltaX();
            double currentDeltaZ = data.getPositionProcessor().getDeltaZ();

            if (lastDeltaX != 0.0 && lastDeltaZ != 0.0) {
                double accelerationX = currentDeltaX - lastDeltaX;
                double accelerationZ = currentDeltaZ - lastDeltaZ;

                if (accelerationX > 0.15 || accelerationZ > 0.15) {
                    fail(String.format("accelerationX=%.2f, accelerationZ=%.2f", accelerationX, accelerationZ));
                }
            }

            lastDeltaX = currentDeltaX;
            lastDeltaZ = currentDeltaZ;
        }
    }
}

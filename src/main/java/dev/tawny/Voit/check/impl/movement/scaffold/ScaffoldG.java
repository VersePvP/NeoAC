package dev.tawny.Voit.check.impl.movement.scaffold;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Scaffold", type = "G", description = "Detects irregular head movements while scaffolding")
public final class ScaffoldG extends Check {

    private float lastYaw;
    private float lastPitch;
    private int irregularCount;

    public ScaffoldG(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying() && data.getActionProcessor().isPlacing()) {
            float currentYaw = data.getRotationProcessor().getYaw();
            float currentPitch = data.getRotationProcessor().getPitch();

            float deltaYaw = Math.abs(currentYaw - lastYaw);
            float deltaPitch = Math.abs(currentPitch - lastPitch);

            if (deltaYaw > 10.0f || deltaPitch > 10.0f) {
                irregularCount++;
            } else {
                irregularCount = Math.max(0, irregularCount - 1);
            }

            if (irregularCount > 5) {
                fail(String.format("irregularCount=%d", irregularCount));
            }

            lastYaw = currentYaw;
            lastPitch = currentPitch;
        }
    }
}

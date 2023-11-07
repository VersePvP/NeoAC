package dev.tawny.Voit.check.impl.movement.scaffold;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Scaffold", type = "H", description = "Detects suspicious rotation changes while scaffolding")
public final class ScaffoldH extends Check {

    private float lastYaw;
    private float lastPitch;
    private int suspiciousCount;

    public ScaffoldH(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying() && data.getActionProcessor().isPlacing()) {
            float currentYaw = data.getRotationProcessor().getYaw();
            float currentPitch = data.getRotationProcessor().getPitch();

            float deltaYaw = Math.abs(currentYaw - lastYaw);
            float deltaPitch = Math.abs(currentPitch - lastPitch);

            if (deltaYaw > 2.0f && deltaPitch > 2.0f) {
                suspiciousCount++;
            } else {
                suspiciousCount = Math.max(0, suspiciousCount - 1);
            }

            if (suspiciousCount > 10) {
                fail(String.format("suspiciousCount=%d", suspiciousCount));
            }

            lastYaw = currentYaw;
            lastPitch = currentPitch;
        }
    }
}

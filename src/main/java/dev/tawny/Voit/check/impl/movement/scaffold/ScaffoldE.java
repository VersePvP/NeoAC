package dev.tawny.Voit.check.impl.movement.scaffold;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Scaffold", type = "E", description = "Detects excessive head rotation speed while scaffolding")
public final class ScaffoldE extends Check {

    private float lastYaw;
    private float lastPitch;
    private long lastRotationTime;

    public ScaffoldE(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying() && data.getActionProcessor().isPlacing()) {
            long currentTime = System.currentTimeMillis();

            if (lastRotationTime > 0) {
                float currentYaw = data.getRotationProcessor().getYaw();
                float currentPitch = data.getRotationProcessor().getPitch();

                float deltaYaw = Math.abs(currentYaw - lastYaw);
                float deltaPitch = Math.abs(currentPitch - lastPitch);

                long deltaTime = currentTime - lastRotationTime;

                if (deltaYaw / deltaTime > 0.5f || deltaPitch / deltaTime > 0.5f) {
                    fail(String.format("deltaYaw=%.2f, deltaPitch=%.2f, deltaTime=%dms", deltaYaw, deltaPitch, deltaTime));
                }
            }

            lastYaw = data.getRotationProcessor().getYaw();
            lastPitch = data.getRotationProcessor().getPitch();
            lastRotationTime = currentTime;
        }
    }
}

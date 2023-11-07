package dev.tawny.Voit.check.impl.movement.scaffold;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Scaffold", type = "F", description = "Detects invalid rotation patterns while scaffolding")
public final class ScaffoldF extends Check {

    private float lastYaw;
    private float lastPitch;

    public ScaffoldF(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying() && data.getActionProcessor().isPlacing()) {
            float currentYaw = data.getRotationProcessor().getYaw();
            float currentPitch = data.getRotationProcessor().getPitch();

            float deltaYaw = Math.abs(currentYaw - lastYaw);
            float deltaPitch = Math.abs(currentPitch - lastPitch);

            if (deltaYaw > 10.0f && deltaPitch > 10.0f) {
                fail(String.format("deltaYaw=%.2f, deltaPitch=%.2f", deltaYaw, deltaPitch));
            }

            lastYaw = currentYaw;
            lastPitch = currentPitch;
        }
    }
}

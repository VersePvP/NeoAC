package dev.tawny.Voit.check.impl.movement.scaffold;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Scaffold", type = "I", description = "Detects very fast head movement while placing blocks")
public final class ScaffoldI extends Check {

    private float lastYaw;
    private float lastPitch;
    private int blockCount;
    private long lastBlockPlacedTime;
    private long decayInterval = 2000; // Adjust the decay interval as needed
    private int decayAmount = 1; // Adjust the decay amount as needed

    public ScaffoldI(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying() && data.getActionProcessor().isPlacing()) {
            float currentYaw = data.getRotationProcessor().getYaw();
            float currentPitch = data.getRotationProcessor().getPitch();

            float deltaYaw = Math.abs(currentYaw - lastYaw);
            float deltaPitch = Math.abs(currentPitch - lastPitch);

            long currentTime = System.currentTimeMillis();
            long timeSinceLastBlockPlaced = currentTime - lastBlockPlacedTime;

            if (blockCount >= 4 && timeSinceLastBlockPlaced < 500 && deltaYaw > 15.0f && deltaPitch > 15.0f) {
                fail(String.format("deltaYaw=%.2f, deltaPitch=%.2f", deltaYaw, deltaPitch));
            }

            if (timeSinceLastBlockPlaced > decayInterval) {
                blockCount = Math.max(0, blockCount - decayAmount);
                lastBlockPlacedTime = currentTime;
            }

            if (deltaYaw > 2.0f && deltaPitch > 2.0f) {
                blockCount++;
                lastBlockPlacedTime = currentTime;
            }

            lastYaw = currentYaw;
            lastPitch = currentPitch;
        }
    }
}

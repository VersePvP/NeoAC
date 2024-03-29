package dev.tawny.Voit.check.impl.combat.aura;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.PlayerUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@CheckInfo(name = "Aura", type = "P", description = "Checks for invalid acceleration.")
public final class AuraP extends Check {
    public AuraP(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying() && hitTicks() < 2) {
            final Entity target = data.getCombatProcessor().getTarget();

            final double deltaXZ = data.getPositionProcessor().getDeltaXZ();
            final double lastDeltaXZ = data.getPositionProcessor().getLastDeltaXZ();

            final double baseSpeed = PlayerUtil.getBaseSpeed(data.getPlayer(), 0.22F);
            final boolean sprinting = data.getActionProcessor().isSprinting();

            final double acceleration = Math.abs(deltaXZ - lastDeltaXZ);

            final boolean exempt = !(target instanceof Player);
            final boolean invalid = acceleration < 0.0027 && sprinting && deltaXZ > baseSpeed;

            debug("deltaXZ: " + deltaXZ + "accel: " +acceleration+ "lastDelta: " + lastDeltaXZ);

            if (invalid && !exempt) {
                if (increaseBuffer() > 3) {
                    fail();
                }
            } else {
                decreaseBufferBy(0.05);
            }
        }
    }
}
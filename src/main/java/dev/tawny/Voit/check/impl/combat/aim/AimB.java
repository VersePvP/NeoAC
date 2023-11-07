package dev.tawny.Voit.check.impl.combat.aim;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Aim", description = "Checks for snapping.", type = "B", experimental = true)
public class AimB extends Check {

    private float lastDeltaYaw;
    private float lastLastDeltaYaw;

    public AimB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isRotation()) {
            if (this.isExempt(ExemptType.TELEPORT, ExemptType.JOINED)) {
                return;
            }

            final float deltaYaw = this.data.getRotationProcessor().getDeltaYaw();

            debug("deltaYaw: " + deltaYaw);

            if (deltaYaw < 5.0f && this.lastDeltaYaw > 30.0f && this.lastLastDeltaYaw < 5.0f && isExempt(ExemptType.TELEPORT)) {
                final double low = (deltaYaw + this.lastLastDeltaYaw) / 2.0f;
                final double high = this.lastDeltaYaw;
                if(increaseBuffer() > 5) {
                    fail(low+ " / " +high);
                } else {
                    decreaseBufferBy(0.10);
                }
            }
            this.lastLastDeltaYaw = this.lastDeltaYaw;
            this.lastDeltaYaw = deltaYaw;
        }
    }
}
package dev.tawny.Voit.check.impl.combat.aim;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Aim", description = "Checks for constant rots.", type = "F")
public class AimF
        extends Check {
    public AimF(PlayerData data) {
        super(data);
    }


    @Override
    public void handle(Packet packet) {
        if(packet.isRotation()) {
            final float deltaYaw = Math.abs(data.getRotationProcessor().getDeltaYaw() % 360F);
            final float deltaPitch = Math.abs(data.getRotationProcessor().getDeltaPitch() % 360F);

            debug("Yaw:" + deltaYaw + " round: " + Math.round(deltaYaw) + " Pitch: " + deltaPitch + " round: " + Math.round(deltaPitch));
            if (deltaYaw > 0.09) {
                if (deltaYaw == Math.round(deltaYaw)) {
                    if (buffer++ > 4) {
                        fail(deltaYaw + " round: " + Math.round(deltaYaw));
                    }
                } else {
                    decreaseBufferBy(3);
                }
            }
        }
    }
}
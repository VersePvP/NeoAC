package dev.tawny.Voit.check.impl.combat.hitbox;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import org.bukkit.entity.Player;

@CheckInfo(name = "HitBox", experimental = true, description = "Checks for hitting outside the entity hitbox.", type = "B")
public final class HitBoxA extends Check {

    public HitBoxA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isUseEntityInteractAt()) {
            WrappedPacketInUseEntity interactAt = new WrappedPacketInUseEntity(packet.getRawPacket());
            if (interactAt.getEntity() instanceof Player) {
                double targetX = Math.abs(interactAt.getTarget().get().getX());
                double targetZ = Math.abs(interactAt.getTarget().get().getZ());
                double maxOffset = 0.4;

                if (targetX > maxOffset || targetZ > maxOffset) {
                    fail("x:" + targetX + " z:" + targetZ);
                }
            }
        }
    }
}

package dev.tawny.Voit.check.impl.combat.reach;

import dev.tawny.Voit.Voit;
import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.PlayerUtil;
import dev.tawny.Voit.util.type.HitboxExpansion;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

@CheckInfo(name = "Reach", type = "B", description = "Checks for impossible attack distance.")
public class ReachB extends Check {

    public ReachB(PlayerData data) {
        super(data);
    }

    public int hits;

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying()) {
            if(data.getCombatProcessor().getHitTicks() > 3) {
                hits = 0;
            }
        }
        if (packet.isUseEntity()) {
            hits++;
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());

            final Entity target = data.getCombatProcessor().getTarget();
            final Entity lastTarget = data.getCombatProcessor().getLastTarget();

            if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK
                    || data.getPlayer().getGameMode() != GameMode.SURVIVAL
                    || !(target instanceof LivingEntity)
                    || target != lastTarget
                    || !data.getTargetLocations().isFull()
            ) return;

            final int ticks = Voit.INSTANCE.getTickManager().getTicks();
            final int pingTicks = NumberConversions.floor(PlayerUtil.getPing(data.getPlayer()) / 50.0) + 4;

            final Vector player = data.getPlayer().getLocation().toVector().setY(0);

            final double distance = data.getTargetLocations().stream()
                    .filter(pair -> Math.abs(ticks - pair.getY() - pingTicks) < 3)
                    .mapToDouble(pair -> {
                        final Vector victim = pair.getX().toVector().setY(0);
                        final double expansion = HitboxExpansion.getExpansion(target);
                        return player.distance(victim) - expansion;
                    }).min().orElse(0);

            if (distance == 0) return;

            if(hits == 1 && distance < 3.1 && distance > 3.0) {
                return;
            }

            if(distance > 3.01) {
                if (increaseBuffer() > 3) {
                    fail("Reach: " + distance);
                }
                else {
                    decreaseBufferBy(0.08);
                }
            }
        }
    }
}
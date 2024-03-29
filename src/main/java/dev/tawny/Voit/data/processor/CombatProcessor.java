

package dev.tawny.Voit.data.processor;

import dev.tawny.Voit.Voit;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.manager.PlayerDataManager;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class CombatProcessor {

    private final PlayerData data;
    private int hitTicks, swings, hits, currentTargets, attackedHits, attackedTicks;
    private double hitMissRatio, distance;
    private Entity target, lastTarget;
    private long LastUseEntityPacket;
    private long lastAttack, lastAttackTick;
    private final List<AxisAlignedBB> pastVictimBoxes = new ArrayList<>();

    public CombatProcessor(final PlayerData data) {
        this.data = data;
        new BukkitRunnable() {

            @Override
            public void run() {
                if(!data.getPlayer().isOnline()) {
                    cancel();
                }
                if(target != null && target instanceof Player) {
                    if(pastVictimBoxes.size() > 20) pastVictimBoxes.clear();
                    pastVictimBoxes.add(((CraftPlayer) target).getHandle().getBoundingBox());
                }
            }
        }.runTaskTimerAsynchronously(Voit.INSTANCE.getPlugin(),0L,1L);
    }

    public void onHitTarget(WrappedPacketInUseEntity packet) {
        if (packet.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
            lastAttack = System.currentTimeMillis();
            this.lastAttackTick = Voit.INSTANCE.getTickManager().getTicks();
        }
    }

    public void handleUseEntity(final WrappedPacketInUseEntity wrapper) {
        LastUseEntityPacket = System.currentTimeMillis();
        if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
            return;
        }
        lastTarget = target == null ? wrapper.getEntity() : target;
        target = wrapper.getEntity();
        if(data.getPlayer().getLocation() != null && target != null && target.getLocation() != null) {
            distance = data.getPlayer().getLocation().toVector().setY(0).distance(target.getLocation().toVector().setY(0)) - .42;
        }

        if(target != null && target instanceof Player && !target.isDead()) {
            PlayerData pd = PlayerDataManager.getInstance().getPlayerData((Player) target);
            if (pd != null) {
                pd.getCombatProcessor().attackedTicks = 0;
                pd.getCombatProcessor().attackedHits++;
            }
        }
        ++hits;

        hitTicks = 0;

        if (target != lastTarget) {
            ++currentTargets;
        }
    }

    public void handleArmAnimation() {
        ++swings;
    }

    public void handleFlying() {
        ++hitTicks;
        ++attackedTicks;
        currentTargets = 0;

        if (swings > 1) {
            hitMissRatio = ((double) hits / (double) swings) * 100;
        }
        if (hits > 100 || swings > 100) {
            hits = swings = 0;
        }
        if(attackedTicks > 20) {
            attackedHits = 0;
        }
    }
}

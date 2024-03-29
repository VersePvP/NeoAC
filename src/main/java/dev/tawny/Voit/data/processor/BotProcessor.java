package dev.tawny.Voit.data.processor;

import dev.tawny.Voit.data.PlayerData;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class BotProcessor{

    public int botID, rayCastEntityID, entityAReportedFlags, botTicks, entityATotalAttacks, movedBotTicks, randomBotSwingTicks, randomBotDamageTicks, rayCastFailHitTimes;
    public boolean hasBot, moveBot, WaitingForBot, hasRaycastBot, hasHitRaycast;

    public double EntityAFollowDistance, rayCastEntityRoation;
    public float EntityAMovementOffset, EntityAStartYaw, rayCastStartYaw;
    public long lastEntitySpawn, entityHitTime, lastEntityBotHit, lastRaycastSpawn, lastRaycastGood, raycastEntity2HitTimes;
    public PlayerData forcedUser;

}

package dev.tawny.Voit.check.impl.combat.aura;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

@CheckInfo(name = "Aura", type = "Q", description = "Attacked two entities at once.")
public class AuraQ extends Check {

    private int ticks, lastEntityId;

    public AuraQ(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {

            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());

            check: {

                if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK) break check;

                if (wrapper.getEntityId() != lastEntityId) {
                    if (++ticks > 1) {
                        fail();
                    }
                }
                lastEntityId = wrapper.getEntityId();
            }
        } else if (packet.isFlying()) {
            ticks = 0;
        }
    }
}
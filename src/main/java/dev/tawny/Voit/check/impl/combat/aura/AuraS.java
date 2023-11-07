package dev.tawny.Voit.check.impl.combat.aura;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;

@CheckInfo(name = "Aura", type = "S", description = "Checks for invalid sprinting orders. (S-TAP Detection)")
public final class AuraS extends Check {
    public AuraS(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isEntityAction()) {
            final WrappedPacketInEntityAction wrapper = new WrappedPacketInEntityAction(packet.getRawPacket());

            final boolean sprinting = wrapper.getAction() == WrappedPacketInEntityAction.PlayerAction.START_SPRINTING
                    || wrapper.getAction() == WrappedPacketInEntityAction.PlayerAction.STOP_SPRINTING;

            if (sprinting) {
                if (increaseBuffer() > 1) {
                    fail();
                }
            }
        } else if (packet.isFlying()) {
            resetBuffer();
        }
    }
}

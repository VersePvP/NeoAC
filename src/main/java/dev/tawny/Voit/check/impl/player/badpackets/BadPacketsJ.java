

package dev.tawny.Voit.check.impl.player.badpackets;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

@CheckInfo(name = "BadPackets", type = "J", description = "Checks for attack no-swing.")
public final class BadPacketsJ extends Check {
    public BadPacketsJ(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());

            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                if (increaseBuffer() > 2) {
                    fail();
                }
            }
        } else if (packet.isArmAnimation()) {
            resetBuffer();
        }
    }
}

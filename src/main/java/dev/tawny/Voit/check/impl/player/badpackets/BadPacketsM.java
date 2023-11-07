

package dev.tawny.Voit.check.impl.player.badpackets;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.clientcommand.WrappedPacketInClientCommand;

@CheckInfo(name = "BadPackets", type = "M", description = "Checks if player is trying to respawn while not dead.")
public final class BadPacketsM extends Check {
    public BadPacketsM(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isClientCommand()) {
            final WrappedPacketInClientCommand wrapper = new WrappedPacketInClientCommand(packet.getRawPacket());

            if (wrapper.getClientCommand() == WrappedPacketInClientCommand.ClientCommand.PERFORM_RESPAWN) {
                if (data.getPlayer().getHealth() > 0.0 && increaseBuffer() > 2) {
                    fail();
                }
                else {
                    setBuffer(0);
                }
            }
        }
    }
}

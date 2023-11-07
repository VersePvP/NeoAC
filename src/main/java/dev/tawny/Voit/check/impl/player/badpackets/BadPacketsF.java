

package dev.tawny.Voit.check.impl.player.badpackets;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "BadPackets", type = "F", description = "Checks if player is sending flyings but not responding transactions.")
public final class BadPacketsF extends Check {

    public BadPacketsF(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
    }
}

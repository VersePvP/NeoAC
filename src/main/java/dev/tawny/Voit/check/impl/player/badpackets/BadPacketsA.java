

package dev.tawny.Voit.check.impl.player.badpackets;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "BadPackets", type = "A", description = "Checks if the player pitch is an impossible value.")
public final class BadPacketsA extends Check {
    public BadPacketsA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            final float pitch = data.getRotationProcessor().getPitch();

            if (Math.abs(pitch)> 90.0f && !isExempt(ExemptType.CLIMBABLE)) {
                fail();
            }
        }
    }
}

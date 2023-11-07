package dev.tawny.Voit.check.impl.player.badpackets;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "BadPackets", type = "D", description = "Detects invalid game speed")
public class BadPacketsD extends Check {
    public BadPacketsD(PlayerData data) {
        super(data);
    }

    private long balance = 0L;
    private long lastFlying = 0L;

    @Override
    public void handle(Packet packet) {
        if (packet.isFlying()) {
            final long now = packet.getTimeStamp();
            handle: {
                if (isExempt(ExemptType.JOINED)) break handle;
                if (lastFlying == 0L) break handle;

                final long delay = now - lastFlying;

                balance += 50L - delay;

                if (balance > 5L) {
                    if (increaseBuffer() > 5) {
                        fail("balance: " + balance);
                    }

                    balance = 0;
                } else {
                    decreaseBufferBy(0.001);
                }
            }

            this.lastFlying = now;
        } else if (packet.isTeleport()) {
            if (isExempt(ExemptType.JOINED)) return;
            if (lastFlying == 0L) return;

            balance -= 50L;
        }
    }
}
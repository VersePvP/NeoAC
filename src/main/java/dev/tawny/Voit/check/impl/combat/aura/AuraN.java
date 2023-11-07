package dev.tawny.Voit.check.impl.combat.aura;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;

@CheckInfo(name = "Aura", description = "Checks for invalid samples.", type = "N")

public class AuraN extends Check {

    private int swings;
    private int hits;

    public AuraN(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet.getPacketId() == 43) {
            if (++this.swings >= 100) {
                if (this.hits > 85) {
                    this.fail(swings);
                }

                this.swings = 0;
                this.hits = 0;
            }
        } else if (packet.getPacketId() == 14) {
            ++this.hits;
        }

    }
}
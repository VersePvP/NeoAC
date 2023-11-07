package dev.tawny.Voit.check.impl.combat.aura;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import dev.tawny.Voit.util.type.EvictingList;

@CheckInfo(name = "Aura", type = "B", description = "Invalid ARM ANIMATION order")
public class AuraB extends Check {
    public AuraB(PlayerData data) {
        super(data);
    }

    private final EvictingList<Packet> packetOrder = new EvictingList<>(3);

    @Override
    public void handle(Packet packet) {
        if (packet.isFlyingType() || packet.isArmAnimation() || packet.isTransaction()) {
            packetOrder.add(packet);

            if (packetOrder.size() == 3) {

                boolean flag = packetOrder.get(2).isTransaction() &&
                        packetOrder.get(1).isArmAnimation() &&
                        packetOrder.get(0).isFlyingType();

                if (flag)
                    fail();
            }

        }
    }
}
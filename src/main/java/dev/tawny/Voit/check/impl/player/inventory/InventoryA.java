

package dev.tawny.Voit.check.impl.player.inventory;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;

@CheckInfo(name = "Inventory", type = "A", description = "Checks for slot change to same slot.")
public final class InventoryA extends Check {

    private int lastSlot = -1;
    private boolean server;

    public InventoryA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isIncomingHeldItemSlot() && !isExempt(ExemptType.JOINED)) {
            final WrappedPacketInHeldItemSlot wrapper = new WrappedPacketInHeldItemSlot(packet.getRawPacket());

            final int slot = wrapper.getCurrentSelectedSlot();

            final boolean invalid = slot == lastSlot;
            final boolean exempt = isExempt(ExemptType.FLYING, ExemptType.CREATIVE, ExemptType.WEB, ExemptType.CLIMBABLE,ExemptType.LIQUID, ExemptType.BOAT, ExemptType.VOID, ExemptType.VEHICLE, ExemptType.CHUNK, ExemptType.PISTON, ExemptType.DEAD);

            if (invalid && !exempt) {
                fail();
            }

            lastSlot = slot;
            server = false;
        } else if (packet.isOutgoingHeldItemSlot()) {
            server = true;
        }
    }
}

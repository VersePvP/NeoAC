

package dev.tawny.Voit.check.impl.player.inventory;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.windowclick.WrappedPacketInWindowClick;

@CheckInfo(name = "Inventory", type = "C", description = "Checks if player is clicking windows too fast.")
public final class InventoryC extends Check {
    public InventoryC(final PlayerData data) {
        super(data);
    }

    private long lastClick;
    private int lastClickSlot;

    @Override
    public void handle(final Packet packet) {
        if (packet.isWindowClick()) {
            WrappedPacketInWindowClick wrapped = new WrappedPacketInWindowClick(packet.getRawPacket());
            if (wrapped.getWindowButton() == 0) {

                int slot = wrapped.getWindowSlot();
                if(lastClickSlot != 0 && slot == lastClickSlot) {
                    return;
                }
                lastClickSlot = slot;
                if (slot < 9 || slot > 35) {
                    return;
                }

                long now = System.currentTimeMillis();

                long delay = now - lastClick;

                if (delay > 11L && delay < 90L && increaseBuffer() > 15) {
                    fail("Delay: " + delay);
                }
                else {
                    decreaseBufferBy(0.10);
                }

                this.lastClick = System.currentTimeMillis();
            }
        }
    }
}

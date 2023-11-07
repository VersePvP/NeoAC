package dev.tawny.Voit.check.impl.player.crasher;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.steervehicle.WrappedPacketInSteerVehicle;

@CheckInfo(name = "Crasher", description = "Checks for disablers.", type = "A")
public final class CrasherA extends Check {

    public CrasherA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isSteerVehicle()) {
            final WrappedPacketInSteerVehicle wrapper = new WrappedPacketInSteerVehicle(packet.getRawPacket());

            final float forwardValue = Math.abs(wrapper.getForwardValue());
            final float sideValue = Math.abs(wrapper.getSideValue());

            final boolean invalid = forwardValue > .98F || sideValue > .98F;

            if (invalid) {
                fail(forwardValue);
            }
        }
    }
}
package dev.tawny.Voit.check.impl.combat.autoclicker;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.check.api.CheckInfo;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.exempt.type.ExemptType;
import dev.tawny.Voit.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

@CheckInfo(name = "AutoClicker", description = "Checks for high click speed.", type = "D")
public final class AutoClickerD extends Check {

    private int ticks, cps;

    public AutoClickerD(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            if (++ticks >= 20) {
                debug("cps=" + cps);
                if (cps > 26 && !isExempt(ExemptType.AUTOCLICKER)) {
                    fail("cps=" + cps);
                }
                ticks = cps = 0;
            }
        } else if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());
            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                ++cps;
            }
        }
    }
}
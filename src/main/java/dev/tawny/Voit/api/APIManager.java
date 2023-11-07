

package dev.tawny.Voit.api;

import dev.tawny.Voit.Voit;
import dev.tawny.Voit.api.impl.VoitFlagEvent;
import dev.tawny.Voit.api.impl.VoitPunishEvent;
import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.config.Config;
import org.bukkit.Bukkit;

public final class APIManager {
    public static void callFlagEvent(final Check check) {
        if (!Config.API_ENABLED) return;

        final VoitFlagEvent flagEvent = new VoitFlagEvent(
                check.getData().getPlayer(),
                check.getCheckInfo().name(),
                check.getCheckInfo().type(),
                check.getVl(),
                check.getBuffer()
        );

        Bukkit.getScheduler().runTask(Voit.INSTANCE.getPlugin(), () -> Bukkit.getPluginManager().callEvent(flagEvent));
    }

    public static void callPunishEvent(final Check check) {
        if (!Config.API_ENABLED) return;
        final VoitPunishEvent punishEvent = new VoitPunishEvent(
                check.getData().getPlayer(),
                check.getCheckInfo().name(),
                check.getCheckInfo().type(),
                check.getPunishCommands(),
                check.getVl(),
                check.getBuffer()
        );

        Bukkit.getScheduler().runTask(Voit.INSTANCE.getPlugin(), () -> Bukkit.getPluginManager().callEvent(punishEvent));
    }
}

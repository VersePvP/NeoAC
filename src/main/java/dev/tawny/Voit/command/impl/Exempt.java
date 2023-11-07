

package dev.tawny.Voit.command.impl;

import dev.tawny.Voit.command.CommandInfo;
import dev.tawny.Voit.command.VoitCommand;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.manager.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "exempt", syntax = "<player>", purpose = "Exempts a player from anti-cheat detections.")
public final class Exempt extends VoitCommand {

    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length == 2) {
            final Player player = Bukkit.getPlayer(args[1]);

             if (player != null) {
                 final PlayerData playerData = PlayerDataManager.getInstance().getPlayerData(player);

                 if (playerData != null) {
                     if (playerData.isExempt()) {
                         sendMessage(sender, "&8Un-exempted '&c" + player.getName() + "&8' from Fox.");
                         playerData.setExempt(false);
                     } else {
                         sendMessage(sender, "&8Exempted '&c" + player.getName() + "&8' from Fox.");
                         playerData.setExempt(true);
                     }
                     return true;
                 }
             }
             else {
                 sendMessage(sender, "&cUnknown player.");
                 return true;
             }
        }
        return false;
    }
}



package dev.tawny.Voit.command.impl;

import dev.tawny.Voit.command.CommandInfo;
import dev.tawny.Voit.command.VoitCommand;
import dev.tawny.Voit.config.Config;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.manager.AlertManager;
import dev.tawny.Voit.manager.PlayerDataManager;
import dev.tawny.Voit.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "alerts", purpose = "Toggles cheat alerts.")
public final class Alerts extends VoitCommand implements CommandExecutor {

    @Override
    protected boolean handle(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && sender.hasPermission("voitac.alerts")) {
            final Player player = (Player) sender;
                    final PlayerData data = PlayerDataManager.getInstance().getPlayerData(player);

                    if (data != null) {
                        if (AlertManager.toggleAlerts(data) == AlertManager.ToggleAlertType.ADD) {
                            sendMessage(sender, ColorUtil.translate(Config.ALERTSON));
                        } else {
                            sendMessage(sender, ColorUtil.translate(Config.ALERTSOFF));
                        }
                        return true;
                    }

                } else {
                    sendMessage(sender, "Only players can execute this command.");
                }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            handle(commandSender, command, s, strings);
        }
        return true;
    }
}
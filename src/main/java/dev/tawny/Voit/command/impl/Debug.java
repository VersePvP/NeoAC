

package dev.tawny.Voit.command.impl;

import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.command.CommandInfo;
import dev.tawny.Voit.command.VoitCommand;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.manager.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "debug", purpose = "Allows the player to debug checks.", syntax = "<check> <checktype>")
public final class Debug extends VoitCommand {
    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length == 3) {
            final PlayerData data = PlayerDataManager.getInstance().getPlayerData(((Player) sender));

            final String checkName = args[1];
            final String checkType = args[2];

            if (data != null && checkName != null && checkType != null) {
                Check check = null;

                for (final Check c : data.getChecks()) {
                    if (c.getCheckInfo().name().equalsIgnoreCase(checkName)
                            && c.getCheckInfo().type().equalsIgnoreCase(checkType)) {
                        check = c;
                        break;
                    }
                }

                if (check != null) {
                    if (check.isDebug()) {
                        check.setDebug(false);
                        sendMessage(sender, String.format("Disabled debugging for the check %s (Type %s)", check.getCheckInfo().name(), check.getCheckInfo().type()));
                    } else {
                        check.setDebug(true);
                        sendMessage(sender, String.format("Enabled debugging for the check %s (Type %s)", check.getCheckInfo().name(), check.getCheckInfo().type()));
                    }
                } else {
                    sendMessage(sender, "Check does not exist.");
                }

                return true;
            }
        }
        return false;
    }
}

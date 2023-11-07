

package dev.tawny.Voit.manager;

import dev.tawny.Voit.Voit;
import dev.tawny.Voit.check.Check;
import dev.tawny.Voit.config.Config;
import dev.tawny.Voit.data.PlayerData;
import dev.tawny.Voit.util.ColorUtil;
import dev.tawny.Voit.util.LogUtil;
import dev.tawny.Voit.util.ServerUtil;
import dev.tawny.Voit.util.discord.DiscordWebhook;
import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
public final class AlertManager {

    private static final Set<PlayerData> alerts = new HashSet<>();
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, MMMMM d, yyyy hh:mm aaa");
    private static long lastAlert;

    public static ToggleAlertType toggleAlerts(final PlayerData data) {
        if (alerts.contains(data)) {
            alerts.remove(data);
            return ToggleAlertType.REMOVE;
        } else {
            alerts.add(data);
            return ToggleAlertType.ADD;
        }
    }

    public static void handleAlert(final Check check, final PlayerData data, final String colour, String info) {

        if(check.getCheckInfo() != null) {
            if (Config.LOGGING_ENABLED) {
                final String log = Config.LOG_FORMAT.replaceAll("%player%", data.getPlayer().getName())
                        .replaceAll("%check%", check.getCheckInfo().name())
                        .replaceAll("%dev%", check.getCheckInfo().experimental() ? ColorUtil.translate("&7*") : "")
                        .replaceAll("%vl%", Integer.toString(check.getVl()))
                        .replaceAll("%type%", check.getCheckInfo().type())
                        .replaceAll("%date%", new Date().toString())
                        .replaceAll("%bar%", ColorUtil.translate(check.getBar()))
                        .replaceAll("%tps%", String.valueOf(Math.min(ServerUtil.getTPS(), 20.0)))
                        .replaceAll("%info%", info)
                        .replaceAll("%tping%", String.valueOf(data.getConnectionProcessor().getTransactionPing()));

                LogUtil.logToFile(data.getLogFile(), log);
            }

            final TextComponent alertMessage = new TextComponent(ColorUtil.translate(Config.ALERT_FORMAT)
                    .replaceAll("%player%", data.getPlayer().getName())
                    .replaceAll("%check%", check.getCheckInfo().name())
                    .replaceAll("%dev%", check.getCheckInfo().experimental() ? ColorUtil.translate("&7*") : "")
                    .replaceAll("%vl%", Integer.toString(check.getVl()))
                    .replaceAll("%colour%", colour)
                    .replaceAll("%bar%", ColorUtil.translate(check.getBar()))
                    .replaceAll("%type%", check.getCheckInfo().type())
                    .replaceAll("%maxvl%", String.valueOf(check.getMaxVl())));

            alertMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Config.CLICKCOMMAND.replaceAll("%player%", data.getPlayer().getName())));
            alertMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtil.translate(
                    "&9Description: &f" + check.getCheckInfo().description() +
                            "\n&eInfo: &b" + info +
                            "\n&9Ping: &b" + PacketEvents.get().getPlayerUtils().getPing(data.getPlayer()) +
                            "\n&eTransPing: &b" + data.getConnectionProcessor().getTransactionPing() +
                            "\n&9TPS: &f" + String.format("%.2f", Math.min(20, PacketEvents.get().getServerUtils().getTPS())) +
                            "\n&eClick to teleport.")).create()));

            alerts.forEach(player -> player.getPlayer().spigot().sendMessage(alertMessage));
        } else {
            if (Config.LOGGING_ENABLED) {
                if(check.custom == 1) {
                    final String log = Config.LOG_FORMAT.replaceAll("%player%", data.getPlayer().getName())
                            .replaceAll("%check%", "Speed")
                            .replaceAll("%dev%", "")
                            .replaceAll("%vl%", Integer.toString(check.getVl()))
                            .replaceAll("%type%", "F")
                            .replaceAll("%date%", new Date().toString())
                            .replaceAll("%tps%", String.valueOf(Math.min(ServerUtil.getTPS(), 20.0)))
                            .replaceAll("%info%", info)
                            .replaceAll("%tping%", String.valueOf(data.getConnectionProcessor().getTransactionPing()));

                    LogUtil.logToFile(data.getLogFile(), log);
                }
                else if(check.custom == 2) {
                    final String log = Config.LOG_FORMAT.replaceAll("%player%", data.getPlayer().getName())
                            .replaceAll("%check%", "FastBow")
                            .replaceAll("%dev%", "")
                            .replaceAll("%vl%", Integer.toString(check.getVl()))
                            .replaceAll("%type%", "A")
                            .replaceAll("%date%", new Date().toString())
                            .replaceAll("%bar%", ColorUtil.translate(check.getBar()))
                            .replaceAll("%tps%", String.valueOf(Math.min(ServerUtil.getTPS(), 20.0)))
                            .replaceAll("%info%", info)
                            .replaceAll("%tping%", String.valueOf(data.getConnectionProcessor().getTransactionPing()));

                    LogUtil.logToFile(data.getLogFile(), log);
                }
            }
            TextComponent alertMessage = new TextComponent("");
            if(check.custom == 1) {
                alertMessage = new TextComponent(ColorUtil.translate(Config.ALERT_FORMAT)
                        .replaceAll("%player%", data.getPlayer().getName())
                        .replaceAll("%check%", "Speed")
                        .replaceAll("%dev%", "")
                        .replaceAll("%vl%", Integer.toString(check.getVl()))
                        .replaceAll("%colour%", colour)
                        .replaceAll("%bar%", ColorUtil.translate(check.getBar()))
                        .replaceAll("%type%", "F")
                        .replaceAll("%maxvl%", String.valueOf(check.getMaxVl())));

                alertMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Config.CLICKCOMMAND.replaceAll("%player%", data.getPlayer().getName())));
                alertMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtil.translate(
                        "&cDescription: &f" + "Checks for ZoneCraft speed (And some other speeds too!)." +
                                "\n&cInfo: &7" + info +
                                "\n&cPing: &7" + PacketEvents.get().getPlayerUtils().getPing(data.getPlayer()) +
                                "\n&cTPS: &7" + String.format("%.2f", Math.min(20, PacketEvents.get().getServerUtils().getTPS())) +
                                "\n&cClick to teleport.")).create()));
            }
            else if(check.custom == 2) {
                alertMessage = new TextComponent(ColorUtil.translate(Config.ALERT_FORMAT)
                        .replaceAll("%player%", data.getPlayer().getName())
                        .replaceAll("%check%", "FastBow")
                        .replaceAll("%dev%", "")
                        .replaceAll("%vl%", Integer.toString(check.getVl()))
                        .replaceAll("%type%", "A")
                        .replaceAll("%colour%", colour)
                        .replaceAll("%bar%", ColorUtil.translate(check.getBar()))
                        .replaceAll("%maxvl%", String.valueOf(check.getMaxVl())));

                alertMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,  Config.CLICKCOMMAND.replaceAll("%player%", data.getPlayer().getName())));
                alertMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtil.translate(
                        "&cDescription: &f" + "Checks for FastBow (Bow Timer)." +
                                "\n&cInfo: &7" + info +
                                "\n&cPing: &7" + PacketEvents.get().getPlayerUtils().getPing(data.getPlayer()) +
                                "\n&cTPS: &7" + String.format("%.2f", Math.min(20, PacketEvents.get().getServerUtils().getTPS())) +
                                "\n&cClick to teleport.")).create()));
            }

            TextComponent finalAlertMessage = alertMessage;
            alerts.forEach(player -> player.getPlayer().spigot().sendMessage(finalAlertMessage));
        }

        if(Config.WEBHOOK) {
            if(System.currentTimeMillis() - lastAlert > 1500) {
                DiscordWebhook webhook = new DiscordWebhook(Config.ALERT_URL);
                DiscordWebhook banWebhook = new DiscordWebhook(Config.BAN_URL);

                webhook.setUsername(Config.DISCORDNAME);
                webhook.addEmbed(new DiscordWebhook.EmbedObject().setAuthor(Config.DISCORDNAME, null, null)
                        .setDescription("```md\\n" +
                                "" + data.getPlayer().getName() + " failed [" + check.getCheckInfo().name() + "] (" + check.getCheckInfo().type() + ") (VL:" + check.getVl() + ")```" +

                                "```md\\n" +
                                "                -Information-\\n" +
                                "\\n" +
                                "* Server: " + Bukkit.getServer().getName() + "\\n" +
                                "* Ping: " + data.getConnectionProcessor().getTransactionPing() + "\\n" +
                                "* TPS: " + ServerUtil.getTPS() + "\\n" +
                                "* Client: " + data.getClientBrand().replaceAll("vanilla", "Vanilla").replaceAll("Tecknix-Client", "Tecknix").replaceAll("fml, forge", "Forge") + "\\n" +
                                "* Past VL: " + data.getTotalViolations() +"```\\n" +

                                "```md\\n" +
                                "            -Check Information-\\n" +
                                "\\n" +
                                "* Description: " + check.getCheckInfo().description() + "\\n" +
                                "* Info: " + (info.length() >= 2000 ? info.substring(0, 2000) : info) + "\\n" +
                                "```")
                        .setThumbnail("https://minotar.net/avatar/" + data.getPlayer().getName() + "/64")
                        .setFooter(simpleDateFormat.format(new Date(System.currentTimeMillis())), ""));

                try {
                    webhook.execute();
                } catch (IOException exception) {
                    exception.printStackTrace();

                }
                lastAlert = System.currentTimeMillis();
            }
        }
    }

    public static void handleAlertLag(final Check check, final PlayerData data, final String info) {
        if(check.getCheckInfo() != null) {
            alerts.forEach(player -> player.getPlayer().sendMessage(ColorUtil.translate(Config.PREFIX + player.getPlayer().getName() + " would flag for " + check.getFullName()) + ", but server skipped " + Voit.INSTANCE.getTickManager().getA() + "MS/" + (Voit.INSTANCE.getTickManager().getA() / 50) + " Ticks."));
        }
    }

    public static void sendMessage(final String message) {
        alerts.forEach(player -> player.getPlayer().sendMessage(ColorUtil.translate(Config.PREFIX + message)));
    }

    public static void sendAntiExploitAlert(final String info, String check) {
        alerts.forEach(player -> player.getPlayer().sendMessage(ColorUtil.translate(Config.ANTICRASHALERT.replaceAll("%player%", player.getPlayer().getName()).replaceAll("%check%", check).replaceAll("%info%", info))));
    }

    public enum ToggleAlertType {
        ADD, REMOVE
    }
}

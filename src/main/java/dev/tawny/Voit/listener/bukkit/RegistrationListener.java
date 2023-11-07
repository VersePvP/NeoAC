package dev.tawny.Voit.listener.bukkit;

import dev.tawny.Voit.Voit;
import dev.tawny.Voit.config.Config;
import dev.tawny.Voit.manager.AFKManager;
import dev.tawny.Voit.manager.AlertManager;
import dev.tawny.Voit.manager.PlayerDataManager;
import dev.tawny.Voit.util.PlayerUtil;
import dev.tawny.Voit.util.type.VpnInfo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public final class RegistrationListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) throws IOException {
        PlayerDataManager.getInstance().add(event.getPlayer());
        if (Voit.INSTANCE.getUpdateChecker().isUpdateAvailable()) {
            if (event.getPlayer().hasPermission("VoitACUP.alerts")) {
                final String version = Voit.INSTANCE.getVersion();
                final String latestVersion = Voit.INSTANCE.getUpdateChecker().getLatestVersion();

                AlertManager.sendMessage("An update is available for &cVoit&8! You have &c" + version + "&8 latest is &c" + latestVersion + "&8.");
            }
        }
        if (Config.VPN_ENABLED) {
            VpnInfo info = PlayerUtil.isUsingVPN(event.getPlayer());
            if (!info.getIsVpn()) {
                return;
            }
            event.getPlayer().kickPlayer(Config.VPN_MESSAGE.replaceAll("%country%", info.getCountry()));
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("voitac.alerts")) {
                    AlertManager.sendAntiExploitAlert("Player tried to join using a vpn/proxy", "Vpn/Proxy");
                }
            }
        }
        if (event.getPlayer().getName().contains(" ")) {
            event.getPlayer().kickPlayer("Disconnected");
            AlertManager.sendAntiExploitAlert("Player joined with name that contains invalid characters", "Invalid Name");
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        PlayerDataManager.getInstance().remove(event.getPlayer());
        PlayerDataManager.getInstance().suspectedPlayers.remove(event.getPlayer());
        BukkitEventManager.wannadelet.remove(event.getPlayer());
        AFKManager.INSTANCE.removePlayer(event.getPlayer());
    }
}

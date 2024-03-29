package dev.tawny.Voit;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import dev.tawny.Voit.command.CommandManager;
import dev.tawny.Voit.command.impl.Alerts;
import dev.tawny.Voit.config.Config;
import dev.tawny.Voit.gui.GuiManager;
import dev.tawny.Voit.listener.bukkit.BukkitEventManager;
import dev.tawny.Voit.listener.bukkit.RegistrationListener;
import dev.tawny.Voit.listener.packet.NetworkManager;
import dev.tawny.Voit.manager.*;
import dev.tawny.Voit.packet.processor.ReceivingPacketProcessor;
import dev.tawny.Voit.packet.processor.SendingPacketProcessor;
import dev.tawny.Voit.update.UpdateChecker;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginAwareness;
import org.bukkit.plugin.messaging.Messenger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public enum Voit {

    INSTANCE;

    private VoitPlugin plugin;

    @Setter
    private YamlConfiguration yaml;

    private long startTime;
    private final TickManager tickManager = new TickManager();
    private final ReceivingPacketProcessor receivingPacketProcessor = new ReceivingPacketProcessor();
    private final SendingPacketProcessor sendingPacketProcessor = new SendingPacketProcessor();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final CommandManager commandManager = new CommandManager(this.getPlugin());


    private final String version = "V1.7";
    private final UpdateChecker updateChecker = new UpdateChecker();
    private GuiManager guiManager;

    boolean fullyLoaded = false;

    public void load(final VoitPlugin plugin) {
        this.plugin = plugin;
        assert plugin != null : "Error while starting NeoVerse.";

        getPlugin().saveDefaultConfig();
        File checks = new File(getPlugin().getDataFolder(), "checks.yml");
        if(!checks.exists()) {
            getPlugin().saveResource("checks.yml", false);
            checks = new File(getPlugin().getDataFolder(), "checks.yml");
        }
        yaml = YamlConfiguration.loadConfiguration(checks);
        Config.updateConfig();
        
        fullyLoaded = true;
        


        setupPacketEvents();
    }

    public void start(final VoitPlugin plugin) {
        runPacketEvents();

        if(fullyLoaded) {
            CheckManager.setup();
            Bukkit.getOnlinePlayers().forEach(player -> PlayerDataManager.getInstance().add(player));

            guiManager = new GuiManager();
            getPlugin().getCommand("neoverse").setExecutor(commandManager);
            getPlugin().getCommand("alerts").setExecutor(new Alerts());


            tickManager.start();

            new AFKManager();

            final Messenger messenger = Bukkit.getMessenger();
            messenger.registerIncomingPluginChannel(plugin, "MC|Brand", new ClientBrandListener());

            startTime = System.currentTimeMillis();

            registerEvents();
        }

    }

    public void stop(final VoitPlugin plugin) {
        this.plugin = plugin;
        assert plugin != null : "Error while shutting down NeoVerse.";

        tickManager.stop();

        stopPacketEvents();
        Bukkit.getScheduler().cancelAllTasks();
    }

    private void setupPacketEvents() {
        PacketEvents.create(plugin).getSettings()
                .fallbackServerVersion(ServerVersion.v_1_8_8);

        PacketEvents.get().load();
    }

    private void runPacketEvents() {
        PacketEvents.get().init();
    }

    private void stopPacketEvents() {
        PacketEvents.get().terminate();
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new RegistrationListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new BukkitEventManager(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ClientBrandListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new GuiManager(), plugin);
        PacketEvents.get().getEventManager().registerListener(new NetworkManager());
    }

    public InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = Voit.INSTANCE.getPlugin().getClass().getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    public void saveConfig() {
        try {
            yaml.save(new File(getPlugin().getDataFolder(), "checks.yml"));
        } catch (IOException e) {

        }
    }

    public void reloadConfig() {
        yaml = YamlConfiguration.loadConfiguration(new File(Voit.INSTANCE.getPlugin().getDataFolder(), "checks.yml"));

        final InputStream defConfigStream = getResource("config.yml");
        if (defConfigStream == null) {
            return;
        }

        final YamlConfiguration defConfig;
        if (getPlugin().getDescription().getAwareness().contains(PluginAwareness.Flags.UTF8) || FileConfiguration.UTF8_OVERRIDE) {
            defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8));
        } else {
            final byte[] contents;
            defConfig = new YamlConfiguration();
            try {
                contents = ByteStreams.toByteArray(defConfigStream);
            } catch (final IOException e) {
                return;
            }

            final String text = new String(contents, Charset.defaultCharset());
            if (!text.equals(new String(contents, Charsets.UTF_8))) {
            }

            try {
                defConfig.loadFromString(text);
            } catch (final InvalidConfigurationException e) {
            }
        }

        yaml.setDefaults(defConfig);
    }

}

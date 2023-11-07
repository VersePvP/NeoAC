
package dev.tawny.Voit;

import org.bukkit.plugin.java.JavaPlugin;

public final class VoitPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        Voit.INSTANCE.load(this);
    }

    @Override
    public void onEnable() {
        Voit.INSTANCE.start(this);
    }

    @Override
    public void onDisable() {
        Voit.INSTANCE.stop(this);
    }

}

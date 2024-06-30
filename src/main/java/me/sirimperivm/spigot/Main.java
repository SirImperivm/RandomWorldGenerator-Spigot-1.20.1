package me.sirimperivm.spigot;

import me.sirimperivm.spigot.utils.Logger;
import me.sirimperivm.utilities.SirUtilities;
import me.sirimperivm.utilities.colors.Colors;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("all")
public final class Main extends JavaPlugin {

    private Main plugin;
    private SirUtilities utilities;
    private Colors colors;
    private Logger log;

    @Override
    public void onEnable() {
        plugin = this;
        utilities = new SirUtilities();
        colors = utilities.getColors();
        colors.setAcceptRGB(true);
        colors.setBukkit(true);
        log = new Logger(plugin, "RandomWorldGenerator");

        log.success("Plugin attivato correttamente!");
    }

    @Override
    public void onDisable() {
        log.success("Plugin disattivato correttamente!");
    }

    public Main getPlugin() {
        return plugin;
    }

    public Colors getColors() {
        return colors;
    }

    public Logger getLog() {
        return log;
    }

    public SirUtilities getUtilities() {
        return utilities;
    }
}

package me.sirimperivm.spigot.utils.general;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.utilities.colors.Colors;

@SuppressWarnings("all")
public class Logger {

    private Main plugin;
    private Colors colors;

    private String pluginName;

    public Logger(Main plugin, String pluginName) {
        this.plugin = plugin;
        this.pluginName = pluginName;

        colors = plugin.getColors();
    }

    public void success(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translatedString("&2[" + pluginName + "] " + message));
    }

    public void info(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translatedString("&e[" + pluginName + "] " + message));
    }

    public void fail(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translatedString("&c[" + pluginName + "] " + message));
    }
}

package me.sirimperivm.spigot.utils.map;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.game.GameManager;
import me.sirimperivm.spigot.utils.general.ConfigManager;
import me.sirimperivm.spigot.utils.general.Logger;
import me.sirimperivm.utilities.colors.Colors;

@SuppressWarnings("all")
public class MapLoader {

    private GameManager gameManager;
    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;

    public MapLoader(GameManager gameManager) {
        this.gameManager = gameManager;
        plugin = gameManager.getPlugin();
        colors = gameManager.getColors();
        log = gameManager.getLog();
        configManager = gameManager.getConfigManager();
    }
}

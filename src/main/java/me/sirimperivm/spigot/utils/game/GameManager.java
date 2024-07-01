package me.sirimperivm.spigot.utils.game;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.general.ConfigManager;
import me.sirimperivm.spigot.utils.general.Logger;
import me.sirimperivm.spigot.utils.map.MapLoader;
import me.sirimperivm.utilities.colors.Colors;

@SuppressWarnings("all")
public class GameManager {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;

    private GameState gameState;
    private MapLoader mapLoader;

    public GameManager(Main plugin) {
        this.plugin = plugin;
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        mapLoader = new MapLoader(this);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        log.info("Stato della partita impostato su: " + gameState);
    }

    public void loadNewGame() {
        setGameState(GameState.GENERATING);
        mapLoader.loadWorlds();
    }

    public Colors getColors() {
        return colors;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Logger getLog() {
        return log;
    }

    public Main getPlugin() {
        return plugin;
    }

    public MapLoader getMapLoader() {
        return mapLoader;
    }
}

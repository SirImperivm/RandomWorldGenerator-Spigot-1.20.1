package me.sirimperivm.spigot;

import me.sirimperivm.spigot.utils.game.GameManager;
import me.sirimperivm.spigot.utils.game.GameState;
import me.sirimperivm.spigot.utils.general.ConfigManager;
import me.sirimperivm.spigot.utils.map.MapLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

@SuppressWarnings("all")
public class Events implements Listener {

    private Main plugin;
    private ConfigManager configManager;
    private GameManager gameManager;
    private MapLoader mapLoader;

    private GameState gameState;

    public Events(Main plugin) {
        this.plugin = plugin;
        configManager = plugin.getConfigManager();
        gameManager = plugin.getGameManager();
        mapLoader = gameManager.getMapLoader();
    }

    @EventHandler
    public void onConnect(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        gameState = gameManager.getGameState();

        if (gameState == GameState.GENERATING && gameState == GameState.END) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, configManager.getTranslatedString(configManager.getSettings(), "messages.generating"));
            return;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        gameState = gameManager.getGameState();

        if (gameState == GameState.WAITING) {
            p.teleport(mapLoader.getLobby().getSpawn());
        }
    }
}

package me.sirimperivm.spigot;

import me.sirimperivm.spigot.nms.CreateNmsAdapterException;
import me.sirimperivm.spigot.nms.NmsAdapter;
import me.sirimperivm.spigot.nms.NmsAdapterFactory;
import me.sirimperivm.spigot.utils.game.GameManager;
import me.sirimperivm.spigot.utils.general.ConfigManager;
import me.sirimperivm.spigot.utils.general.Logger;
import me.sirimperivm.spigot.utils.map.MapLoader;
import me.sirimperivm.utilities.SirUtilities;
import me.sirimperivm.utilities.colors.Colors;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

import static org.bukkit.Bukkit.getPluginManager;

@SuppressWarnings("all")
public final class Main extends JavaPlugin {

    private Main plugin;
    private SirUtilities utilities;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private static Optional<NmsAdapter> nmsAdapter;
    private GameManager gameManager;
    private MapLoader mapLoader;

    @Override
    public void onEnable() {
        plugin = this;
        utilities = new SirUtilities();
        colors = utilities.getColors();
        colors.setAcceptRGB(true);
        colors.setBukkit(true);
        log = new Logger(plugin, "RandomWorldGenerator");
        configManager = new ConfigManager(plugin);
        loadNmsAdapter();
        gameManager = new GameManager(plugin);
        mapLoader = gameManager.getMapLoader();

        log.success("Plugin attivato correttamente!");
        Bukkit.getScheduler().runTaskLater(plugin, () -> gameManager.loadNewGame(), 1);

        loadEvents();
    }

    public void loadEvents() {
        getPluginManager().registerEvents(new Events(plugin), plugin);
    }

    @Override
    public void onDisable() {
        log.success("Plugin disattivato correttamente!");
    }

    private void loadNmsAdapter() {
        try {
            final NmsAdapter adapter = NmsAdapterFactory.create();
            log.info("Caricato l'nmsAdapter: " + adapter.getClass().getName());
            nmsAdapter = Optional.of(adapter);
        } catch (CreateNmsAdapterException e) {
            log.fail("Impossibile caricare l'nmsAdapter.");
            nmsAdapter = Optional.empty();
            e.printStackTrace();
        }
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

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static Optional<NmsAdapter> getNmsAdapter() {
        return nmsAdapter;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}

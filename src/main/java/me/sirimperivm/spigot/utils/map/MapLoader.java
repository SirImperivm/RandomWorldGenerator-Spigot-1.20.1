package me.sirimperivm.spigot.utils.map;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.game.GameManager;
import me.sirimperivm.spigot.utils.game.GameState;
import me.sirimperivm.spigot.utils.general.ConfigManager;
import me.sirimperivm.spigot.utils.general.Logger;
import me.sirimperivm.spigot.utils.structures.Lobby;
import me.sirimperivm.utilities.colors.Colors;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

@SuppressWarnings("all")
public class MapLoader {

    private GameManager gameManager;
    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;

    private World created;
    private String worldName;

    private Lobby lobby;

    public MapLoader(GameManager gameManager) {
        this.gameManager = gameManager;
        plugin = gameManager.getPlugin();
        colors = gameManager.getColors();
        log = gameManager.getLog();
        configManager = gameManager.getConfigManager();
    }

    private void removeOceans() {
        try {
            if (Main.getNmsAdapter().isPresent()) {
                log.info("Rimozione oceani in corso...");
                Main.getNmsAdapter().get().removeOceans();
            } else {
                log.fail("Non è stato possibile rimuovere gli oceani: questa versione non è supportata.");
            }
        } catch (Exception e) {
            log.fail("Non è stato possibile rimuovere gli oceani:");
            e.printStackTrace();
        }
    }

    public void loadWorlds() {
        if (configManager.getSettings().getBoolean("settings.game-managing.remove-oceans", true)) {
            removeOceans();
        }
        deleteOldPlayersFiles();
        deleteLastWorld();
        createNewWorld();
    }

    public void createNewWorld() {
        worldName = configManager.getSettings().getString("settings.game-managing.world-name", "game_world");
        log.info("Creazione del mondo: " + worldName);

        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);
        World created = wc.createWorld();

        this.created = created;
        createLobby();
        //loadChunks();
    }

    private void loadChunks() {
        log.info("Caricando i chunk...");
        long tickSpeed = configManager.getSettings().getLong("settings.game-managing.chunk-loading-speed", 1);
        int size = configManager.getSettings().getInt("settings.game-managing.world-size", 1000) * 2;
        int chunkMinX = (-size) >> 4;
        int chunkMaxX = (size) >> 4;
        int chunkMinZ = (-size) >> 4;
        int chunkMaxZ = (size) >> 4;

        new BukkitRunnable() {
            int x = chunkMinX;
            int z = chunkMinZ;

            @Override
            public void run() {
                if (x > chunkMaxX) {
                    x = chunkMinX;
                    z++;
                }

                if (z > chunkMaxZ) {
                    log.info("Caricamento chunk terminato con successo.");

                    cancel();
                    return;
                }

                log.info("Caricando i chunk... X: " + String.valueOf(x) + ", Z: " + String.valueOf(z));
                Chunk chunk = created.getChunkAt(x, z);
                if (!chunk.isLoaded()) {
                    chunk.load();
                }
                created.save();

                x++;
            }
        }.runTaskTimer(plugin, 0L, tickSpeed);
    }

    private void createLobby() {
        lobby = new Lobby(log, configManager, worldName);
        gameManager.setGameState(GameState.WAITING);
        log.success("Generazione iniziale mondo eseguita con successo! Ora si può entrare.");

        //plugin.loadEvents();
    }

    private void deleteLastWorld() {
        if (worldName != null && !worldName.equals("null") && !worldName.equals("")) {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                Bukkit.unloadWorld(world, false);
            }

            File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
            deleteDirectory(worldFolder);
        }
    }

    private void deleteDirectory(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteDirectory(child);
            }
        }
        file.delete();
    }

    private void deleteOldPlayersFiles() {
        if (Bukkit.getServer().getWorlds().isEmpty()) {
            return;
        }

        final File mainWorldFolder = Bukkit.getWorlds().get(0).getWorldFolder();

        // Deleting old players files
        final File playerdata = new File(mainWorldFolder, "playerdata");
        if(playerdata.exists() && playerdata.isDirectory()){
            for(File playerFile : playerdata.listFiles()){
                playerFile.delete();
            }
        }

        // Deleting old players stats
        final File stats = new File(mainWorldFolder, "stats");
        if(stats.exists() && stats.isDirectory()){
            for(File statFile : stats.listFiles()){
                statFile.delete();
            }
        }

        // Deleting old players advancements
        final File advancements = new File(mainWorldFolder, "advancements");
        if(advancements.exists() && advancements.isDirectory()){
            for(File advancementFile : advancements.listFiles()){
                advancementFile.delete();
            }
        }
    }

    public Lobby getLobby() {
        return lobby;
    }
}

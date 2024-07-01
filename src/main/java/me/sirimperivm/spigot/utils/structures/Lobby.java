package me.sirimperivm.spigot.utils.structures;

import me.sirimperivm.spigot.utils.general.ConfigManager;
import me.sirimperivm.spigot.utils.general.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

@SuppressWarnings("all")
public class Lobby {

    private Logger log;
    private ConfigManager configManager;
    private String worldName;
    private World world;

    private int posX;
    private int posY;
    private int posZ;
    private Location position;

    private double spawnX;
    private double spawnY;
    private double spawnZ;
    private float spawnYaw;
    private float spawnPitch;
    private Location spawn;

    private Material borderMaterial;
    private int width;
    private int length;
    private int height;

    private boolean status;

    public Lobby(Logger log, ConfigManager configManager, String worldName) {
        this.log = log;
        this.configManager = configManager;
        this.worldName = worldName;
        world = Bukkit.getWorld(worldName);

        posX = configManager.getSettings().getInt("settings.lobby-creation.rel-position.x");
        posY = configManager.getSettings().getInt("settings.lobby-creation.rel-position.y");
        posZ = configManager.getSettings().getInt("settings.lobby-creation.rel-position.z");
        position = new Location(world, posX, posY, posZ);

        spawnX = configManager.getSettings().getDouble("settings.lobby-creation.rel-spawn.x");
        spawnY = configManager.getSettings().getDouble("settings.lobby-creation.rel-spawn.y");
        spawnZ = configManager.getSettings().getDouble("settings.lobby-creation.rel-spawn.z");
        spawnYaw = Float.parseFloat(configManager.getSettings().getString("settings.lobby-creation.rel-spawn.yaw"));
        spawnPitch = Float.parseFloat(configManager.getSettings().getString("settings.lobby-creation.rel-spawn.pitch"));
        spawn = new Location(world, spawnX, spawnY, spawnZ, spawnYaw, spawnPitch);

        borderMaterial = Material.getMaterial(configManager.getSettings().getString("settings.lobby-creation.border-material"));
        width = configManager.getSettings().getInt("settings.lobby-creation.size.width");
        length = configManager.getSettings().getInt("settings.lobby-creation.size.length");
        height = configManager.getSettings().getInt("settings.lobby-creation.size.height");

        build();
    }

    public void build() {
        int x = position.getBlockX(), y = position.getBlockY(), z = position.getBlockZ();
        World world = position.getWorld();

        int startLength = length*-1;
        int endLength = length;

        int startHeight = -1;
        int endHeight = height-1;

        int startWidth = width*-1;
        int endWidth = width;

        for (int i=startLength; i<=endLength; i++) {
            for (int j=startHeight; j<=endHeight; j++) {
                for (int k=startWidth; k<=endWidth; k++) {
                    if (i == startLength
                            || i == endLength
                            || j == startHeight
                            || j == endHeight
                            || k == startWidth
                            || k == endWidth
                    ) {
                        world.getBlockAt(x+i, y+j, z+k).setType(borderMaterial);
                    } else {
                        world.getBlockAt(x+i, y+j, z+k).setType(Material.AIR);
                    }
                }
            }
        }

        status = true;
    }

    public void destroy() {
        int lobbyX = position.getBlockX(), lobbyY = position.getBlockY(), lobbyZ = position.getBlockZ();
        World world = position.getWorld();

        for (int x=-length; x<=length; x++) {
            for (int y=height; y>=-height; y--) {
                for (int z=-width; z<=width; z++) {
                    if (!world.getBlockAt(lobbyX + x, lobbyY + y, lobbyZ + z).equals(Material.AIR)) {
                        world.getBlockAt(lobbyX + x, lobbyY + y, lobbyZ + z).setType(Material.AIR);
                    }
                }
            }
        }

        status = false;
    }

    public Location getSpawn() {
        return spawn;
    }

    public boolean getStatus() {
        return status;
    }
}

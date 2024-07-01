package me.sirimperivm.spigot.utils.general;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.utilities.colors.Colors;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ConfigManager {

    private Main plugin;
    private Colors colors;
    private Logger log;

    private File folder;
    private File settingsFile, storageFile;
    private FileConfiguration settings, storage;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        colors = plugin.getColors();
        log = plugin.getLog();

        folder = plugin.getDataFolder();
        settingsFile = new File(folder, "settings.yml");
        settings = new YamlConfiguration();
        storageFile = new File(folder, "storage.yml");
        storage = new YamlConfiguration();

        if (!folder.exists()) folder.mkdir();
        if (!settingsFile.exists()) create(settings, settingsFile);
        if (!storageFile.exists()) create(storage, storageFile);

        loadAll();
    }

    private void create(FileConfiguration c, File f) {
        String n = f.getName();
        try {
            Files.copy(plugin.getResource(n), f.toPath(), new CopyOption[0]);
        } catch (IOException e) {
            log.fail("Impossibile creare il file " + n + "!");
            e.printStackTrace();
        }
    }

    public void save(FileConfiguration c, File f) {
        String n = f.getName();
        try {
            c.save(f);
        } catch (IOException e) {
            log.fail("Impossibile salvare il file " + n + "!");
            e.printStackTrace();
        }
    }

    public void load(FileConfiguration c, File f) {
        String n = f.getName();
        try {
            c.load(f);
        } catch (IOException | InvalidConfigurationException e) {
            log.fail("Impossibile caricare il file " + n + "!");
            e.printStackTrace();
        }
    }

    public void saveAll() {
        save(settings, settingsFile);
        save(storage, storageFile);
    }

    public void loadAll() {
        load(settings, settingsFile);
        load(storage, storageFile);
    }

    public FileConfiguration getSettings() {
        return settings;
    }

    public File getSettingsFile() {
        return settingsFile;
    }

    public FileConfiguration getStorage() {
        return storage;
    }

    public File getStorageFile() {
        return storageFile;
    }

    public String getTranslatedString(FileConfiguration config, String target) {
        return colors.translateString(config.getString(target));
    }

    public List<String> getTranslatedList(FileConfiguration config, String target) {
        List<String> coloredList = new ArrayList<>();
        for (String line : config.getStringList(target)) {
            coloredList.add(colors.translateString(line));
        }
        return coloredList;
    }
}

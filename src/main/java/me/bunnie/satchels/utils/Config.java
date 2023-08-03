package me.bunnie.satchels.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config extends YamlConfiguration {

    private final File file;
    private final String name, directory;

    public Config(JavaPlugin plugin, String name, String directory) {
        this.name = name;
        this.directory = directory;
        this.file = new File(directory, name + ".yml");
        if (!this.file.exists()) {
            plugin.saveResource(name + ".yml", false);
        }
        this.load();
        this.save();
    }


    public void load() {
        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException ex2) {
            final Exception e = new Exception();
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDirectory() {
        return directory;
    }
}

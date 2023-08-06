package me.bunnie.satchels.utils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateUtils {

    private final JavaPlugin plugin;
    private final int resourceId;

    public UpdateUtils(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getLatestVersion(Consumer<String> consumer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                    if (scanner.hasNext()) {
                        consumer.accept(scanner.next());
                    }
                } catch (IOException exception) {
                    plugin.getLogger().info("Unable to get updates: " + exception.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }

}
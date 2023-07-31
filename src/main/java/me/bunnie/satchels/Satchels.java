package me.bunnie.satchels;

import lombok.Getter;
import me.bunnie.satchels.utils.ChatUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class Satchels extends JavaPlugin {

    @Getter private static Satchels instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public String getPrefix() {
        return ChatUtils.format(getConfig().getString("settings.prefix"));
    }

}

package me.bunnie.satchels;

import lombok.Getter;
import me.bunnie.satchels.commands.SatchelsCommand;
import me.bunnie.satchels.hooks.economy.EconomyProvider;
import me.bunnie.satchels.hooks.economy.vault.VaultEconomyProvider;
import me.bunnie.satchels.hooks.value.ValueProvider;
import me.bunnie.satchels.hooks.value.esgui.ESGUIValueProvider;
import me.bunnie.satchels.hooks.value.satchels.DefaultValueProvider;
import me.bunnie.satchels.hooks.value.sgui.SGUIValueProvider;
import me.bunnie.satchels.listeners.PlayerListener;
import me.bunnie.satchels.listeners.SatchelListener;
import me.bunnie.satchels.satchel.SatchelManager;
import me.bunnie.satchels.utils.ChatUtils;
import me.bunnie.satchels.utils.Config;
import me.bunnie.satchels.utils.ui.listener.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class Satchels extends JavaPlugin {

    @Getter private static Satchels instance;
    @Getter private Config configYML, upgradesYML, valuesYML;
    @Getter private SatchelManager satchelManager;
    @Getter private EconomyProvider economyProvider;
    @Getter private ValueProvider valueProvider;

    @Override
    public void onEnable() {
        instance = this;
        registerConfigurations();
        registerManagers();
        registerHooks();
        registerListeners();
        registerCommands();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void registerHooks() {
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            economyProvider = new VaultEconomyProvider(this);
        } else {
            getLogger().warning("Unable to find Vault within the enabled plugins! Disabling Plugin...");
            getPluginLoader().disablePlugin(this);
        }

        switch (getValueHook().toLowerCase()) {
            case "shopguiplus" ->  {
                if(Bukkit.getPluginManager().isPluginEnabled("ShopGUIPlus")) {
                    valueProvider = new SGUIValueProvider(this);
                } else {
                    getLogger().warning("Unable to find ShopGUIPlus within the enabled plugins! Disabling Plugin...");
                    getPluginLoader().disablePlugin(this);
                }
            }
            case "economyshopgui" -> {
                if(Bukkit.getPluginManager().isPluginEnabled("EconomyShopGUI")) {
                    valueProvider = new ESGUIValueProvider(this);
                } else {
                    getLogger().warning("Unable to find EconomyShopGUI within the enabled plugins! Disabling Plugin...");
                    getPluginLoader().disablePlugin(this);
                }
            }
            case "satchels" -> valueProvider = new DefaultValueProvider(this);
        }
    }

    private void registerConfigurations() {
        configYML = new Config(this, "config", getDataFolder().getAbsolutePath());
        upgradesYML = new Config(this, "upgrades", getDataFolder().getAbsolutePath());
        valuesYML = new Config(this, "values", getDataFolder().getAbsolutePath());
    }

    private void registerManagers() {
        saveDefaultConfig();
        satchelManager = new SatchelManager(this);
    }

    private void registerListeners() {
        Arrays.asList(new PlayerListener(), new SatchelListener(this),
                new MenuListener()).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }


    private void registerCommands() {
        new SatchelsCommand(this);
    }


    private String getValueHook() {
        return getConfigYML().getString("settings.value-hook");
    }

    public String getPrefix() {
        return ChatUtils.format(getConfigYML().getString("settings.prefix"));
    }



}

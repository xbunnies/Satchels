package me.bunnie.satchels.hooks.value.esgui;

import me.bunnie.satchels.Satchels;
import me.bunnie.satchels.hooks.value.ValueProvider;
import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import org.bukkit.inventory.ItemStack;

public class ESGUIValueProvider implements ValueProvider {

    private final Satchels plugin;

    public ESGUIValueProvider(Satchels plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("Hooked into EconomyShopGUI for ValueProvider");
    }

    @Override
    public double getItemValue(ItemStack item) {
        return EconomyShopGUIHook.getItemSellPrice(item);
    }
}

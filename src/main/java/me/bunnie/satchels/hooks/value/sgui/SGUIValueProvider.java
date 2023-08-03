package me.bunnie.satchels.hooks.value.sgui;

import me.bunnie.satchels.Satchels;
import me.bunnie.satchels.hooks.value.ValueProvider;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.inventory.ItemStack;

public class SGUIValueProvider implements ValueProvider {

    private final Satchels plugin;

    public SGUIValueProvider(Satchels plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("Hooked into ShopGUIPlus for ValueProvider");
    }

    @Override
    public double getItemValue(ItemStack item) {
        return ShopGuiPlusApi.getItemStackPriceSell(item);
    }
}

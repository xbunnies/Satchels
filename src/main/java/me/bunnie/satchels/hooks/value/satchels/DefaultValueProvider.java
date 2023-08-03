package me.bunnie.satchels.hooks.value.satchels;

import me.bunnie.satchels.Satchels;
import me.bunnie.satchels.hooks.value.ValueProvider;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DefaultValueProvider implements ValueProvider {

    private final Satchels plugin;

    public DefaultValueProvider(Satchels plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("Could not find any other ValueProvider enabling DefaultValueProvider...");
    }

    @Override
    public double getItemValue(ItemStack item) {
        List<String> values = plugin.getValuesYML().getStringList("sell-values");
        for(String key : values) {
            String[] valueArray = key.split(":");
            Material material = Material.valueOf(valueArray[0]);
            double price = Double.parseDouble(valueArray[1]);
            if(item.getType().equals(material)) {
                return price;
            }
        }
        return 0;
    }
}

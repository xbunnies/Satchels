package me.bunnie.satchels.hooks.value;

import org.bukkit.inventory.ItemStack;

public interface ValueProvider {

    // Implement this method to get the value of the given item for the specified player
    double getItemValue(ItemStack item);

}
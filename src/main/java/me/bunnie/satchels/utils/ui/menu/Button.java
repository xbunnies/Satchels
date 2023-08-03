package me.bunnie.satchels.utils.ui.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class Button {

    public abstract ItemStack getItem(Player player);

    public void onButtonClick(Player player, int slot, ClickType clickType) {
    }

    public void update(Player player, Menu menu) {
        player.getOpenInventory().getTopInventory().clear();
        player.getOpenInventory().getTopInventory().setContents(menu.getInventory(player).getContents());
    }

}


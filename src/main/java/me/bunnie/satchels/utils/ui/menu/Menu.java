package me.bunnie.satchels.utils.ui.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Menu {

    private final static Map<UUID, Menu> menuMap = new HashMap<>();

    public abstract String getTitle(Player player);

    public abstract Map<Integer, Button> getButtons(Player player);

    public int getSize(Player player) {
        return -1;
    }

    public Inventory getInventory(Player player) {
        Map<Integer, Button> buttonMap = this.getButtons(player);
        int size = this.getSize(player) == -1 ? this.calculateSize(buttonMap) : this.getSize(player);

        String title = this.getTitle(player);
        if (title == null) {
            title = "Failed to load title";
        }
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }

        Inventory toReturn = Bukkit.createInventory(player, size, title);
        Menu previous = menuMap.get(player.getUniqueId());

        if (player.getOpenInventory().getTopInventory() != null) {
            if (previous == null) player.closeInventory();
        } else {
            int previousSize = player.getOpenInventory().getTopInventory().getSize();
            String previousTitle = player.getOpenInventory().getTitle();

            if (previousSize == size && previousTitle.equals(title)) {
                toReturn = player.getOpenInventory().getTopInventory();
            } else {
                player.closeInventory();
            }
        }

        for (Map.Entry<Integer, Button> buttonEntry : buttonMap.entrySet()) {
            toReturn.setItem(buttonEntry.getKey(), buttonEntry.getValue().getItem(player));
        }

        player.openInventory(toReturn);
        menuMap.put(player.getUniqueId(), this);
        return toReturn;
    }

    public void onClose(Player player) {
        menuMap.remove(player.getUniqueId());
    }

    private int calculateSize(Map<Integer, Button> buttons) {
        int highest = 0;

        for (int buttonValue : buttons.keySet()) {
            if (buttonValue > highest) {
                highest = buttonValue;
            }
        }

        return (int) (Math.ceil((highest + 1) / 9D) * 9D);
    }

    public static Map<UUID, Menu> getMenuMap() {
        return menuMap;
    }
}

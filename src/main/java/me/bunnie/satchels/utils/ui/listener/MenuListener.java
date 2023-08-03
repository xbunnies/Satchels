package me.bunnie.satchels.utils.ui.listener;

import me.bunnie.satchels.utils.ui.menu.Button;
import me.bunnie.satchels.utils.ui.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

public class MenuListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Menu menu = Menu.getMenuMap().get(player.getUniqueId());

        if (menu == null) {
            return;
        }

        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getClickedInventory().getType() != InventoryType.CHEST) {
            return;
        }

        event.setCancelled(true);
        int slot = event.getSlot();
        Map<Integer, Button> buttons = menu.getButtons(player);

        if (buttons.containsKey(slot)) {
            Button button = buttons.get(slot);
            button.onButtonClick(player, slot, event.getClick());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Menu menu = Menu.getMenuMap().get(player.getUniqueId());

        if (menu == null) {
            return;
        }

        menu.onClose(player);
        Menu.getMenuMap().remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Menu menu = Menu.getMenuMap().get(player.getUniqueId());

        if (menu == null) {
            return;
        }

        menu.onClose(player);
        Menu.getMenuMap().remove(player.getUniqueId());
    }
}
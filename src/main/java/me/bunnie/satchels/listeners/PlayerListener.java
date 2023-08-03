package me.bunnie.satchels.listeners;

import me.bunnie.satchels.events.SatchelCollectEvent;
import me.bunnie.satchels.events.SatchelToggleEvent;
import me.bunnie.satchels.satchel.Satchel;
import me.bunnie.satchels.ui.SatchelMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if (itemStack == null) return;

        Satchel clickedSatchel = Satchel.fromItemStack(itemStack);
        if (clickedSatchel == null) return;

        if (player.isSneaking() && event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            new SatchelMenu(clickedSatchel).getInventory(player);
            return;
        }

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!player.isSneaking()) {
                Bukkit.getServer().getPluginManager().callEvent(new SatchelToggleEvent(player, clickedSatchel));
            }
        }

    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        ItemStack itemStack = event.getItemInHand();
        if (itemStack.getType().equals(Material.AIR)) return;

        Satchel clickedSatchel = Satchel.fromItemStack(itemStack);
        if (clickedSatchel == null) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onMobSlay(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) return;

        Player player = event.getEntity().getKiller();
        if (player == null) return;

        List<ItemStack> originalDrops = new ArrayList<>(event.getDrops());

        for (ItemStack items : originalDrops) {
            Material material = items.getType();
            Bukkit.getServer().getPluginManager().callEvent(
                    new SatchelCollectEvent(player, material, event));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Bukkit.getServer().getPluginManager().callEvent(
                new SatchelCollectEvent(event.getPlayer(), event.getBlock().getType(), event));
    }
}
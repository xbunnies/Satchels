package me.bunnie.satchels.listeners;

import me.bunnie.satchels.Satchels;
import me.bunnie.satchels.events.SatchelCollectEvent;
import me.bunnie.satchels.events.SatchelSellEvent;
import me.bunnie.satchels.events.SatchelToggleEvent;
import me.bunnie.satchels.satchel.Satchel;
import me.bunnie.satchels.utils.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.w3c.dom.events.EventTarget;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SatchelListener implements Listener {

    private final Satchels plugin;
    private final Map<UUID, Satchel> activeSatchels;
    private final Map<UUID, Integer> cooldownMap;
    private final int SATCHEL_CD = 2;

    public SatchelListener(Satchels plugin) {
        this.plugin = plugin;
        this.activeSatchels = new HashMap<>();
        this.cooldownMap = new HashMap<>();
    }

    @EventHandler
    public void onToggle(SatchelToggleEvent event) {
        Player player = event.getPlayer();
        Satchel satchel = event.getSatchel();

        if (activeSatchels.containsKey(player.getUniqueId())) {
            Satchel activeSatchel = activeSatchels.get(player.getUniqueId());
            if (activeSatchel.getId().equals(satchel.getId())) {
                deactivateSatchel(player, activeSatchel);
                return;
            } else {
                deactivateSatchel(player, activeSatchel);
                activateSatchel(player, satchel);
                return;
            }
        }
        activateSatchel(player, satchel);
        run(player);
    }

    @EventHandler
    public void onCollect(SatchelCollectEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        Satchel activeSatchel = activeSatchels.get(player.getUniqueId());
        if (activeSatchel == null) return;

        for (int i = 0; i < 9; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            Satchel foundSatchel = Satchel.fromItemStack(item);
            if (foundSatchel == null) {
                continue;
            }

            if (activeSatchel.getId().equals(foundSatchel.getId())) {
                if (activeSatchel.isActive()) {
                    int contents = activeSatchel.getContents();
                    int capacity = activeSatchel.getCapacity();

                    if (activeSatchel.getTargetMaterial() == event.getMaterial()) {

                        if (contents == capacity) {
                            String message = plugin.getConfig().getString("messages.on-conflict.full")
                                    .replace("%satchel%", activeSatchel.getDisplayName())
                                    .replace("%satchel-contents%", String.valueOf(contents))
                                    .replace("%satchel-capacity%", String.valueOf(capacity))
                                    .replace("%prefix%", plugin.getPrefix());
                            player.sendMessage(ChatUtils.format(message));
                            return;
                        }

                        activeSatchel.setContents(contents + 1);
                        player.getInventory().setItem(i, activeSatchel.toItemStack());

                        if (event.getEvent() instanceof BlockBreakEvent) {
                            ((BlockBreakEvent) event.getEvent()).setDropItems(false);
                        }
                        if (event.getEvent() instanceof EntityDeathEvent) {
                            ((EntityDeathEvent) event.getEvent()).getDrops().clear();
                        }

                    }
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onSell(SatchelSellEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        Satchel satchel = event.getSatchel();
        if (satchel == null) return;

        for (int i = 0; i < 9; i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            Satchel foundSatchel = Satchel.fromItemStack(item);
            if (foundSatchel == null) {
                continue;
            }

            if (satchel.getId().equals(foundSatchel.getId())) {
                if (satchel.getContents() > 0) {
                    if(plugin.getEconomyProvider() == null || plugin.getValueProvider() == null) {
                        player.sendMessage(ChatUtils.format("&cUnable to find a valid Economy OR Value Provider."));
                        return;
                    }

                    plugin.getEconomyProvider().deposit(player, satchel.getValue());
                    String message = plugin.getConfig().getString("messages.on-sell.success")
                            .replace("%satchel%", satchel.getDisplayName())
                            .replace("%satchel-contents%", String.valueOf(satchel.getContents()))
                            .replace("%satchel-value%", String.valueOf(satchel.getValue()))
                            .replace("%prefix%", plugin.getPrefix());
                    player.sendMessage(ChatUtils.format(message));
                    satchel.setContents(0);
                    player.getInventory().setItem(i, satchel.toItemStack());
                } else {
                    String message = plugin.getConfig().getString("messages.on-sell.fail")
                            .replace("%satchel%", satchel.getDisplayName())
                            .replace("%prefix%", plugin.getPrefix());
                    player.sendMessage(ChatUtils.format(message));
                }
            }
            break;
        }
    }

    private void activateSatchel(Player player, Satchel satchel) {
        if(cooldownMap.containsKey(player.getUniqueId())) return;
        activeSatchels.put(player.getUniqueId(), satchel);
        satchel.setActive(true);

        String message = plugin.getConfig().getString("messages.on-toggle.enable")
                .replace("%satchel%", satchel.getDisplayName())
                .replace("%prefix%", plugin.getPrefix());
        player.sendMessage(ChatUtils.format(message));

        cooldownMap.put(player.getUniqueId(), SATCHEL_CD);
        run(player);

    }

    private void deactivateSatchel(Player player, Satchel satchel) {
        if(cooldownMap.containsKey(player.getUniqueId())) return;
        satchel.setActive(false);
        activeSatchels.remove(player.getUniqueId(), satchel);

        String message = plugin.getConfig().getString("messages.on-toggle.disable")
                .replace("%satchel%", satchel.getDisplayName())
                .replace("%prefix%", plugin.getPrefix());
        player.sendMessage(ChatUtils.format(message));

        cooldownMap.put(player.getUniqueId(), SATCHEL_CD);
        run(player);

    }

    private void run(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(cooldownMap.containsKey(player.getUniqueId())) {
                    int counter = cooldownMap.get(player.getUniqueId());
                    counter--;
                    cooldownMap.put(player.getUniqueId(), counter);
                    if(counter == 0) {
                        cooldownMap.remove(player.getUniqueId());
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

}
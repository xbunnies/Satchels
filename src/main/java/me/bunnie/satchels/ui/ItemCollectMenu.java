package me.bunnie.satchels.ui;

import me.bunnie.satchels.Satchels;
import me.bunnie.satchels.satchel.Satchel;
import me.bunnie.satchels.ui.action.Action;
import me.bunnie.satchels.utils.ChatUtils;
import me.bunnie.satchels.utils.ItemBuilder;
import me.bunnie.satchels.utils.ui.menu.Button;
import me.bunnie.satchels.utils.ui.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCollectMenu extends Menu {

    private final Satchels plugin;
    private final Satchel satchel;
    private int amount;

    public ItemCollectMenu(Satchel satchel) {
        this.satchel = satchel;
        this.plugin = Satchels.getInstance();
        this.amount = 1;
    }

    @Override
    public String getTitle(Player player) {
        return ChatUtils.format(plugin.getConfigYML().getString("menus.collect.title")
                .replace("%satchel-name%", satchel.getDisplayName()));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for (String key : plugin.getConfigYML().getConfigurationSection("menus.collect.buttons").getKeys(false)) {
            String path = "menus.collect.buttons." + key;
            if (plugin.getConfig().isInt(path + ".slot")) {
                int slot = plugin.getConfig().getInt(path + ".slot");
                buttons.put(slot, new Button() {
                    @Override
                    public ItemStack getItem(Player player) {
                        String name = plugin.getConfigYML().getString(path + ".name")
                                .replace("%satchel-item.name%", satchel.getDisplayName());
                        String materialString = plugin.getConfigYML().getString(path + ".material")
                                .replace("%satchel-item.material%", satchel.toItemStack().getType().name());
                        Material material = Material.valueOf(materialString);
                        List<String> toReplace = plugin.getConfigYML().getStringList(path + ".lore");
                        ArrayList<String> lore = new ArrayList<>();
                        for (String s : toReplace) {
                            s = s.replace("%satchel-contents%", String.valueOf(satchel.getContents()));
                            s = s.replace("%satchel-capacity%", String.valueOf(satchel.getCapacity()));
                            s = s.replace("%amount%", String.valueOf(amount));
                            lore.add(s);
                        }
                        return new ItemBuilder(material)
                                .setName(name)
                                .setLore(lore)
                                .setGlow(plugin.getConfig().getBoolean(path + ".enchanted"))
                                .build();
                    }

                    @Override
                    public void onButtonClick(Player player, int slot, ClickType clickType) {
                        String actionName = plugin.getConfig().getString(path + ".action");
                        Action action = Action.valueOf(actionName);
                        switch (action) {
                            case INCREASE_AMOUNT -> {
                                if (clickType.isLeftClick()) {
                                    if (amount == satchel.getContents()) return;
                                    if (amount < satchel.getContents()) {
                                        amount++;
                                        update(player, ItemCollectMenu.this);
                                    }
                                } else if (clickType.isRightClick()) {
                                    if (amount > 1) amount--;
                                    update(player, ItemCollectMenu.this);
                                }
                            }
                            case COLLECT_CONFIRM -> {
                                if (satchel.getContents() == 0) return;
                                ItemStack itemStack = new ItemStack(satchel.getTargetMaterial());
                                itemStack.setAmount(amount);
                                player.getInventory().addItem(itemStack);
                                satchel.setContents(satchel.getContents() - amount);
                                for (int i = 0; i < 9; i++) {
                                    ItemStack item = player.getInventory().getItem(i);
                                    if (item == null || item.getType() == Material.AIR) {
                                        continue;
                                    }

                                    Satchel foundSatchel = Satchel.fromItemStack(item);
                                    if (foundSatchel == null) {
                                        continue;
                                    }

                                    if (satchel.getId().equals(foundSatchel.getId())) {
                                        player.getInventory().setItem(i, satchel.toItemStack());
                                    }
                                    break;
                                }
                                player.closeInventory();
                            }
                            case CLOSE_MENU -> player.closeInventory();
                        }
                    }
                });
            } else if (plugin.getConfig().isList(path + ".slot")) {
                List<Integer> slotList = plugin.getConfig().getIntegerList(path + ".slot");
                for (int slot : slotList) {
                    buttons.put(slot, new Button() {
                        @Override
                        public ItemStack getItem(Player player) {
                            String name = plugin.getConfigYML().getString(path + ".name")
                                    .replace("%satchel-item.name%", satchel.getDisplayName());
                            String materialString = plugin.getConfigYML().getString(path + ".material")
                                    .replace("%satchel-item.material%", satchel.toItemStack().getType().name());
                            Material material = Material.valueOf(materialString);
                            List<String> toReplace = plugin.getConfigYML().getStringList(path + ".lore");
                            ArrayList<String> lore = new ArrayList<>();
                            for (String s : toReplace) {
                                s = s.replace("%satchel-contents%", String.valueOf(satchel.getContents()));
                                s = s.replace("%satchel-capacity%", String.valueOf(satchel.getCapacity()));
                                s = s.replace("%amount%", String.valueOf(amount));
                                lore.add(s);
                            }
                            return new ItemBuilder(material)
                                    .setName(name)
                                    .setLore(lore)
                                    .setGlow(plugin.getConfig().getBoolean(path + ".enchanted"))
                                    .build();
                        }

                        @Override
                        public void onButtonClick(Player player, int slot, ClickType clickType) {
                            String actionName = plugin.getConfig().getString(path + ".action");
                            Action action = Action.valueOf(actionName);
                            switch (action) {
                                case INCREASE_AMOUNT -> {
                                    if (clickType.isLeftClick()) {
                                        if (amount == satchel.getContents()) return;
                                        if (amount < satchel.getContents()) {
                                            amount++;
                                            update(player, ItemCollectMenu.this);
                                        }
                                    } else if (clickType.isRightClick()) {
                                        if (amount > 1) amount--;
                                        update(player, ItemCollectMenu.this);
                                    }
                                }
                                case COLLECT_CONFIRM -> {
                                    if (satchel.getContents() == 0) return;
                                    ItemStack itemStack = new ItemStack(satchel.getTargetMaterial());
                                    itemStack.setAmount(amount);
                                    player.getInventory().addItem(itemStack);
                                    satchel.setContents(satchel.getContents() - amount);
                                    for (int i = 0; i < 9; i++) {
                                        ItemStack item = player.getInventory().getItem(i);
                                        if (item == null || item.getType() == Material.AIR) {
                                            continue;
                                        }

                                        Satchel foundSatchel = Satchel.fromItemStack(item);
                                        if (foundSatchel == null) {
                                            continue;
                                        }

                                        if (satchel.getId().equals(foundSatchel.getId())) {
                                            player.getInventory().setItem(i, satchel.toItemStack());
                                        }
                                        break;
                                    }
                                    player.closeInventory();
                                }
                                case CLOSE_MENU -> player.closeInventory();
                            }
                        }
                    });
                }
            }
        }

        for (int i = 0; i < getSize(player); i++) {
            if(buttons.get(i) == null) {
                buttons.put(i, new Button() {
                    @Override
                    public ItemStack getItem(Player player) {
                        return new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").build();
                    }
                });
            }
        }
        return buttons;
    }

    @Override
    public int getSize(Player player) {
        return plugin.getConfigYML().getInt("menus.collect.size");
    }

}

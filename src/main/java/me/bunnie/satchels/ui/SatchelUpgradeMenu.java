package me.bunnie.satchels.ui;

import me.bunnie.satchels.Satchels;
import me.bunnie.satchels.events.SatchelSellEvent;
import me.bunnie.satchels.events.SatchelUpgradeEvent;
import me.bunnie.satchels.satchel.Satchel;
import me.bunnie.satchels.satchel.upgrade.UpgradeType;
import me.bunnie.satchels.ui.action.Action;
import me.bunnie.satchels.utils.ChatUtils;
import me.bunnie.satchels.utils.ItemBuilder;
import me.bunnie.satchels.utils.ui.menu.Button;
import me.bunnie.satchels.utils.ui.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SatchelUpgradeMenu extends Menu {

    private final Satchels plugin;
    private final Satchel satchel;
    private final DecimalFormat DECIMAL_FORMAT;

    public SatchelUpgradeMenu(Satchel satchel) {
        this.satchel = satchel;
        this.plugin = Satchels.getInstance();
        this.DECIMAL_FORMAT = new DecimalFormat("#,###.#");

    }

    @Override
    public String getTitle(Player player) {
        return ChatUtils.format(
                plugin.getConfigYML().getString("menus.upgrade.title")
                        .replace("%satchel-name%", satchel.getDisplayName()));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for(String key : plugin.getConfig().getConfigurationSection("menus.upgrade.buttons").getKeys(false)) {
            String path = "menus.upgrade.buttons." + key;
            int slot = plugin.getConfig().getInt(path + ".slot");

            buttons.put(slot, new Button() {
                @Override
                public ItemStack getItem(Player player) {
                    String name = plugin.getConfig().getString(path + ".name")
                            .replace("%satchel-item.name%", satchel.getDisplayName());
                    String materialString = plugin.getConfig().getString(path + ".material")
                            .replace("%satchel-item.material%", satchel.toItemStack().getType().name());
                    Material material = Material.valueOf(materialString);
                    List<String> toReplace = plugin.getConfig().getStringList(path + ".lore");
                    ArrayList<String> lore = new ArrayList<>();
                    for(String s : toReplace) {
                        s = s.replace("%satchel-contents%", String.valueOf(satchel.getContents()));
                        s = s.replace("%satchel-capacity%", String.valueOf(satchel.getCapacity()));
                        s = s.replace("%satchel-sellbonus%", String.valueOf(satchel.getSellBonus()));
                        if(satchel.getNextCapacity() == -1) {
                            s = s.replace("%satchel-capacity.next%", "N/A");
                            s = s.replace("%satchel-capacity.next-price%", "0");
                        } else {
                            s = s.replace("%satchel-capacity.next%", String.valueOf(satchel.getNextCapacity()));
                            s = s.replace("%satchel-capacity.next-price%", DECIMAL_FORMAT.format(satchel.getNextCapacityPrice()));
                        }

                        if(satchel.getNextSB() == satchel.getSellBonus()) {
                            s = s.replace("%satchel-sellbonus.next%", "N/A");
                            s = s.replace("%satchel-sellbonus.next-price%", "0");
                        } else {
                            s = s.replace("%satchel-sellbonus.next%", String.valueOf(satchel.getNextSB()));
                            s = s.replace("%satchel-sellbonus.next-price%", DECIMAL_FORMAT.format(satchel.getNextSBPrice()));
                        }

                        s = s.replace("%satchel-value%", String.valueOf(satchel.getValue() * satchel.getSellBonus()));

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
                    String actionString = plugin.getConfig().getString(path + ".action");
                    String[] actionArray = actionString.split(":");
                    String actionName = actionArray[0];
                    String upgradeName = actionArray[1];

                    Action action = Action.valueOf(actionName);
                    UpgradeType upgrade = UpgradeType.valueOf(upgradeName);
                    switch (action) {
                        case UPGRADE -> {

                            plugin.getServer().getPluginManager().callEvent(new SatchelUpgradeEvent(player, satchel, upgrade));
                            update(player, SatchelUpgradeMenu.this);
                        }
                        case CLOSE_MENU -> player.closeInventory();
                    }
                }
            });
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
        return plugin.getConfigYML().getInt("menus.upgrade.size");
    }
}

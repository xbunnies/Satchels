package me.bunnie.satchels.ui;

import me.bunnie.satchels.Satchels;
import me.bunnie.satchels.events.SatchelSellEvent;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SatchelMenu extends Menu {

    private final Satchels plugin;
    private final Satchel satchel;
    private final DecimalFormat DECIMAL_FORMAT;

    public SatchelMenu(Satchel satchel) {
        this.satchel = satchel;
        this.plugin = Satchels.getInstance();
        this.DECIMAL_FORMAT = new DecimalFormat("#,###.#");

    }

    @Override
    public String getTitle(Player player) {
        return ChatUtils.format(
                plugin.getConfigYML().getString("menus.satchel.title")
                        .replace("%satchel-name%", satchel.getDisplayName()));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        for(String key : plugin.getConfig().getConfigurationSection("menus.satchel.buttons").getKeys(false)) {
            String path = "menus.satchel.buttons." + key;
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
                        s = s.replace("%satchel-capacity.next%", String.valueOf(satchel.getNextCapacity()));
                        s = s.replace("%satchel-capacity.next-price%", DECIMAL_FORMAT.format(satchel.getNextCapacityPrice()));
                        s = s.replace("%satchel-value%", String.valueOf(satchel.getValue()));
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
                        case SELL -> {
                            plugin.getServer().getPluginManager().callEvent(new SatchelSellEvent(player, satchel));
                            update(player, SatchelMenu.this);
                        }
                        case UPGRADE -> {
                            update(player, SatchelMenu.this);
                        }
                        case COLLECT -> {
                            player.closeInventory();
                            new ItemCollectMenu(satchel).getInventory(player);
                        }
                        case CLOSE_MENU -> player.closeInventory();
                    }
                }
            });
        }
        return buttons;
    }

    @Override
    public int getSize(Player player) {
        return plugin.getConfigYML().getInt("menus.satchel.size");
    }
}

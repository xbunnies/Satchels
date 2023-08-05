package me.bunnie.satchels.satchel;

import lombok.Data;
import me.bunnie.satchels.Satchels;
import me.bunnie.satchels.utils.ChatUtils;
import me.bunnie.satchels.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Satchel {

    private UUID id;
    private String name;
    private Material targetMaterial;
    private int contents, capacity;
    private double sellBonus;
    private boolean active;

    public Satchel(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.contents = 0;
        this.sellBonus = 1.0;
    }

    public String getDisplayName() {
        ItemMeta itemMeta = toItemStack().getItemMeta();
        if(itemMeta == null) {
            return ChatUtils.fixString(name) + " Satchel";
        }
        return itemMeta.getDisplayName();
    }

    public ItemStack toItemStack() {
        Configuration config = Satchels.getInstance().getConfig();
        List<String> toReplace = config.getStringList("satchels." + name + ".lore");
        ArrayList<String> lore = new ArrayList<>();
        for (String s : toReplace) {
            s = s.replace("%satchel-contents%", String.valueOf(contents));
            s = s.replace("%satchel-capacity%", String.valueOf(capacity));
            s = s.replace("%satchel-sellbonus%", String.valueOf(sellBonus));
            s = s.replace("%satchel-target%", targetMaterial.name());
            s = s.replace("%satchel-value%", String.valueOf(getValue() * getSellBonus()));

            lore.add(s);
        }

        return new ItemBuilder(Material.valueOf(config.getString("satchels." + name + ".material")))
                .setName(config.getString("satchels." + name + ".name"))
                .setLore(lore)
                .setGlow(config.getBoolean("satchels." + name + ".enchanted"))
                .addPDC("satchelId", PersistentDataType.STRING, id.toString())
                .addPDC("satchelName", PersistentDataType.STRING, name)
                .addPDC("satchelTarget", PersistentDataType.STRING, targetMaterial.name())
                .addPDC("satchelContents", PersistentDataType.INTEGER, contents)
                .addPDC("satchelCapacity", PersistentDataType.INTEGER, capacity)
                .addPDC("satchelSellBonus", PersistentDataType.DOUBLE, sellBonus)
                .build();
    }

    public static Satchel fromItemStack(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return null;

        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        String idStr = pdc.get(new NamespacedKey(Satchels.getInstance(), "satchelId"), PersistentDataType.STRING);
        String name = pdc.get(new NamespacedKey(Satchels.getInstance(), "satchelName"), PersistentDataType.STRING);
        if (idStr == null || name == null) {
            return null;
        }

        UUID id = UUID.fromString(idStr);

        Satchel satchel = new Satchel(name);
        satchel.setId(id);
        satchel.setTargetMaterial(
                Material.valueOf(pdc.getOrDefault(new NamespacedKey(Satchels.getInstance(), "satchelTarget"), PersistentDataType.STRING, "")));
        satchel.setContents(pdc.getOrDefault(new NamespacedKey(Satchels.getInstance(), "satchelContents"), PersistentDataType.INTEGER, 0));
        satchel.setCapacity(pdc.getOrDefault(new NamespacedKey(Satchels.getInstance(), "satchelCapacity"), PersistentDataType.INTEGER, 5));
        satchel.setSellBonus(pdc.getOrDefault(new NamespacedKey(Satchels.getInstance(), "satchelSellBonus"), PersistentDataType.DOUBLE, 1.0));
        return satchel;
    }


    public int getNextCapacity() {
        ConfigurationSection capacitySection = Satchels.getInstance().getUpgradesYML().getConfigurationSection("capacity");
        if (capacitySection == null) {
            return -1;
        }

        int highestCapacity = -1;

        for (String key : capacitySection.getKeys(false)) {
            int nextCap = Integer.parseInt(key);
            if (nextCap > capacity && nextCap > highestCapacity) {
                highestCapacity = nextCap;
            }
        }

        return highestCapacity;
    }

    public double getNextSB() {
        ConfigurationSection sellBonusSection = Satchels.getInstance().getUpgradesYML().getConfigurationSection("sellbonus");
        if (sellBonusSection == null) {
            return -1;
        }

        double highestSellBonus = -1;

        for (String key : sellBonusSection.getKeys(false)) {
            String[] parts = key.split("-");
            if (parts.length != 2) {
                continue;
            }

            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);

            double sellBonus = major + (minor / 10.0);

            if (sellBonus > highestSellBonus) {
                highestSellBonus = sellBonus;
            }
        }

        return highestSellBonus;
    }

    public double getNextSBPrice() {
        ConfigurationSection sellBonusSection = Satchels.getInstance().getUpgradesYML().getConfigurationSection("sellbonus");
        if (sellBonusSection == null) {
            return -1;
        }

        double highestSellBonusPrice = -1;

        for (String key : sellBonusSection.getKeys(false)) {
            double price = sellBonusSection.getDouble(key + ".price");
            if (price > highestSellBonusPrice) {
                highestSellBonusPrice = price;
            }
        }

        return highestSellBonusPrice;
    }


    public double getNextCapacityPrice() {
        ConfigurationSection capacitySection = Satchels.getInstance().getUpgradesYML().getConfigurationSection("capacity");
        if (capacitySection == null) {
            return -1;
        }

        for (String key : capacitySection.getKeys(false)) {
            int nextCap = Integer.parseInt(key);
            double price = capacitySection.getDouble(key + ".price");
            if (nextCap > capacity) {
                return price;
            }
        }

        return -1;
    }

    public double getValue() {
        return contents * Satchels.getInstance().getValueProvider().getItemValue(new ItemStack(targetMaterial));
    }

}


package me.bunnie.satchels.satchel;

import me.bunnie.satchels.Satchels;

import java.util.ArrayList;
import java.util.List;

public class SatchelManager {

    private final Satchels plugin;

    public SatchelManager(Satchels plugin) {
        this.plugin = plugin;
    }

    public List<String> getAvailableSatchels() {
        List<String> toReturn = new ArrayList<>();
        for(String key : plugin.getConfig().getConfigurationSection("satchels").getKeys(false)) {
            toReturn.add(key);
        }
        return toReturn;
    }





}

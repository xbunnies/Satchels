package me.bunnie.satchels.hooks.economy;

import org.bukkit.entity.Player;

public interface EconomyProvider {

    void deposit(Player player, double amount);

}

package me.bunnie.satchels.hooks.economy;

import org.bukkit.entity.Player;

public interface EconomyProvider {

    void deposit(Player player, double amount);

    double getPlayerBalance(Player player);

    void withdraw(Player player, double amount);

}

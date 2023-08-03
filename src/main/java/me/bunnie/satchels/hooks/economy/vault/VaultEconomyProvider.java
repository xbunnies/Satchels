package me.bunnie.satchels.hooks.economy.vault;

import me.bunnie.satchels.Satchels;
import me.bunnie.satchels.hooks.economy.EconomyProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;

public class VaultEconomyProvider implements EconomyProvider {

    private final Satchels plugin;
    private Economy economy;

    public VaultEconomyProvider(Satchels plugin) {
        this.plugin = plugin;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) return;
        economy = rsp.getProvider();
        plugin.getLogger().info("Hooked into Vault for EconomyProvider");
    }

    @Override
    public void deposit(Player player, double amount) {
        economy.depositPlayer(player, amount);
    }

    @Override
    public double getPlayerBalance(Player player) {
        return economy.getBalance(player);
    }

    @Override
    public void withdraw(Player player, double amount) {
        economy.withdrawPlayer(player, amount);
    }
}

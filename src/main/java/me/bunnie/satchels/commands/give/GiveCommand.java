package me.bunnie.satchels.commands.give;

import me.bunnie.satchels.Satchels;
import me.bunnie.satchels.satchel.Satchel;
import me.bunnie.satchels.utils.ChatUtils;
import me.bunnie.satchels.utils.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GiveCommand extends SubCommand {

    private final Satchels plugin;

    public GiveCommand(Satchels plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getDescription() {
        return "Gives the online player the provided Satchel!";
    }

    @Override
    public String getSyntax() {
        return "/satchels give <player> <satchel>";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 3) {
            List<String> message = new ArrayList<>();
            message.add("&cUh Oh! It appears you have not provided the correct amount of arguments!");
            message.add("&cUsage: &f" + getSyntax());
            message.forEach(msg -> sender.sendMessage(ChatUtils.format(msg)));
            return;
        }
        String playerName = args[1];
        String satchelName = args[2];

        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) {
            sender.sendMessage(ChatUtils.format("&cCannot Find Player."));
            return;
        }
        if (!plugin.getSatchelManager().getAvailableSatchels().contains(satchelName)) {
            sender.sendMessage(ChatUtils.format("&cCannot Find Satchel."));
            return;
        }

        Satchel satchel = new Satchel(satchelName);
        satchel.setTargetMaterial(Material.valueOf(plugin.getConfigYML().getString("satchels." + satchelName + ".target")));
        satchel.setCapacity(plugin.getConfigYML().getInt("satchels." + satchelName + ".default-capacity"));
        satchel.setSellBonus(plugin.getConfigYML().getDouble("satchels." + satchelName + ".default-sellbonus"));

        String senderMessage = plugin.getConfigYML().getString("messages.on-give.sender")
                .replace("%satchel%", satchel.getDisplayName())
                .replace("%player%", player.getName())
                .replace("%prefix%", plugin.getPrefix());
        String playerMessage = plugin.getConfigYML().getString("messages.on-give.target")
                .replace("%satchel%", satchel.getDisplayName())
                .replace("%prefix%", plugin.getPrefix());


        if (fullInventory(player)) {
            Location location = player.getLocation();
            player.getWorld().dropItem(location, satchel.toItemStack());
            player.sendMessage(ChatUtils.format("&cYour inventory was full! Therefore the item has been dropped!"));
        } else {
            player.getInventory().addItem(satchel.toItemStack());
        }
        player.sendMessage(ChatUtils.format(playerMessage));
        sender.sendMessage(ChatUtils.format(senderMessage));
    }

    public boolean fullInventory(Player player) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if (player.getInventory().getItem(i) == null) {
                return false;
            }
        }
        return true;
    }
}
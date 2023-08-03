package me.bunnie.satchels.commands;

import me.bunnie.satchels.Satchels;
import me.bunnie.satchels.commands.give.GiveCommand;
import me.bunnie.satchels.commands.help.HelpCommand;
import me.bunnie.satchels.satchel.Satchel;
import me.bunnie.satchels.utils.ChatUtils;
import me.bunnie.satchels.utils.command.Command;
import me.bunnie.satchels.utils.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SatchelsCommand extends Command {

    private final Satchels plugin;
    private final List<SubCommand> subCommands;

    public SatchelsCommand(Satchels plugin) {
        super(
                "satchels",
                new String[]{"satchel"},
                "Administrative command for 'Satchels' plugin!",
                "satchels.commands.admin"
        );
        this.plugin = plugin;
        this.subCommands = new ArrayList<>();
        this.subCommands.add(new GiveCommand(plugin));
        this.subCommands.add(new HelpCommand(subCommands));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            new HelpCommand(subCommands).execute(sender, args);
        } else {
            boolean validSubCommand = false;
            for (int i = 0; i < subCommands.size(); i++) {
                if (args[0].equalsIgnoreCase(subCommands.get(i).getName())) {
                    validSubCommand = true;
                    subCommands.get(i).execute(sender, args);
                    break;
                }
            }
            if (!validSubCommand) {
                List<String> message = new ArrayList<>();
                message.add("&c%Uh Oh! You have entered an invalid command!");
                message.add("&c%Refer to &f/" + getName() + " help &cfor reference!");
                message.forEach(msg -> sender.sendMessage(ChatUtils.format(msg)));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            for (SubCommand command : subCommands) {
                options.add(command.getName());
            }
            String argument = args[0];
            StringUtil.copyPartialMatches(argument, options, completions);
            Collections.sort(completions);
        }

        if (args.length == 2) {
                List<String> options = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    options.add(player.getName());
                }
                String argument = args[1];
                StringUtil.copyPartialMatches(argument, options, completions);
                Collections.sort(completions);
        }

        if (args.length == 3) {
            List<String> options = new ArrayList<>(plugin.getSatchelManager().getAvailableSatchels());
            String argument = args[2];
            StringUtil.copyPartialMatches(argument, options, completions);
            Collections.sort(completions);
        }
        return completions;
    }
}

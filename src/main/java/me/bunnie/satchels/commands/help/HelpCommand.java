package me.bunnie.satchels.commands.help;

import me.bunnie.satchels.utils.ChatUtils;
import me.bunnie.satchels.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends SubCommand {

    private final List<SubCommand> subCommands;

    public HelpCommand(List<SubCommand> subCommands) {
        this.subCommands = subCommands;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Displays this message!";
    }

    @Override
    public String getSyntax() {
        return "/satchels help";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<String> message = new ArrayList<>();
        message.add("&7&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        message.add("&r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r #ffb6c1&lᴀ#f7c5d3&lᴠ#efd4e5&lᴀ#e8e3f6&lɪ#cee0f6&lʟ#b1d9f2&lᴀ#93d1ed&lʙ#8ad6db&lʟ#90e5c2&lᴇ #95f3a8&lᴄ#9aff8e&lᴏ#c5eda6&lᴍ#e5e2b1&lᴍ#ffd8bb&lᴀ#eac5bf&lɴ#d9b4c3&lᴅ#c8a2c8&ls");
        message.add("");
        for (SubCommand command : subCommands) {
            message.add("#fcdfff" + command.getSyntax() + " &7- #bce3f9" + command.getDescription());
        }
        message.add("");
        message.add("&7&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        message.forEach(msg -> sender.sendMessage(ChatUtils.format(msg)));
    }
}

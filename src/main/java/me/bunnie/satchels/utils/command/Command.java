package me.bunnie.satchels.utils.command;

import me.bunnie.satchels.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public abstract class Command extends BukkitCommand {

    public Command(String command, String[] aliases, String description, String permission) {
        super(command);
        this.setAliases(Arrays.asList(aliases));
        this.setDescription(description);
        this.setPermission(permission);
        this.setPermissionMessage(ChatUtils.format("&cYou lack permission to use this command!"));

        this.create(command);
    }

    public Command(String command, String[] aliases, String description) {
        super(command);
        this.setAliases(Arrays.asList(aliases));
        this.setDescription(description);
        this.create(command);
    }

    private void create(String command) {
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            CommandMap commandMap = (CommandMap) f.get(Bukkit.getServer());
            commandMap.register(command, this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public CommandMap getCommandMap() {
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            return (CommandMap) f.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void execute(CommandSender sender, String[] args);
    public abstract List<String> onTabComplete(CommandSender sender, String[] args);


    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        execute(sender, args);
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return onTabComplete(sender, args);
    }
}

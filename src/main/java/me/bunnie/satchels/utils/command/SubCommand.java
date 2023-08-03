package me.bunnie.satchels.utils.command;

import org.bukkit.command.CommandSender;

/**
 * The SubCommand class serves as a base abstract class for creating SubCommands.
 * Subclasses must implement the abstract methods defined in this class.
 */
public abstract class SubCommand {

    /**
     * Returns the name of the subcommand.
     *
     * @return The name of the subcommand.
     */
    public abstract String getName();

    /**
     * Returns the description of the subcommand.
     *
     * @return The description of the subcommand.
     */
    public abstract String getDescription();

    /**
     * Returns the syntax of the subcommand.
     *
     * @return The syntax of the subcommand.
     */
    public abstract String getSyntax();

    /**
     * Executes the subcommand.
     *
     * @param sender The CommandSender executing the command.
     * @param args   The arguments passed with the command.
     */
    public abstract void execute(CommandSender sender, String[] args);
}
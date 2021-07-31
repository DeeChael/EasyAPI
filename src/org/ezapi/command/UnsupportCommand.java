package org.ezapi.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class UnsupportCommand extends Command {

    /**
     * You shouldn't create a new Unsupport command
     * @param name command name
     * @param aliases aliases
     */
    public UnsupportCommand(String name, String... aliases) {
        super(name);
        this.setAliases(Arrays.asList(aliases));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, String[] args) throws IllegalArgumentException {
        return Collections.singletonList(" ");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        sender.sendMessage(ChatColor.RED + "Unsupport version");
        return true;
    }

}

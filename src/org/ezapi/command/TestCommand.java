package org.ezapi.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ezapi.utils.ParticleUtils;
import org.ezapi.utils.PlayerUtils;

import java.lang.reflect.InvocationTargetException;

class TestCommand extends Command {

    public TestCommand() {
        super("ezapi");
        try {
            ((CommandMap) Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer())).register("ezapi", this);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location location = new Location(player.getWorld(), player.getLocation().getX() + 12.0, player.getLocation().getY() + 5.0, player.getLocation().getZ() + 20.0);
        }
        return true;
    }

}

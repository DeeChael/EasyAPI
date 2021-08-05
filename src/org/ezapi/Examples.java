package org.ezapi;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.ezapi.chat.ChatMessage;
import org.ezapi.command.EzArgument;
import org.ezapi.command.EzCommand;
import org.ezapi.command.EzCommandManager;
import org.ezapi.command.argument.ArgumentLocation;
import org.ezapi.command.argument.ArgumentPlayer;
import org.ezapi.command.argument.BaseArguments;
import org.ezapi.configuration.AutoReloadFile;
import org.ezapi.module.bossbar.BarColor;
import org.ezapi.module.bossbar.BarStyle;
import org.ezapi.module.bossbar.EzBossBar;
import org.ezapi.module.hologram.TextHologram;
import org.ezapi.module.npc.EzNPC;
import org.ezapi.module.npc.NPCType;
import org.ezapi.module.scoreboard.EzScoreboard;
import org.ezapi.util.FileUtils;

import java.io.File;

public class Examples {

    private static void scoreboardExample(Player player) {
        //Create a new scoreboard
        EzScoreboard scoreboard = new EzScoreboard(new ChatMessage("&c&lExample", false));
        //Make a player can see the scoreboard
        scoreboard.addViewer(player);
        //Add text, the int is witch line, line number higher, position higher
        scoreboard.setText(8, new ChatMessage("&b&l| &bThanks for playing!", false));
        scoreboard.setText(7, new ChatMessage("&b&l|    ", false));
        scoreboard.setText(6, new ChatMessage("&b&l| &bPlayer", false));
        scoreboard.setText(5, new ChatMessage("&b&l| &e{display_name}", false));
        scoreboard.setText(4, new ChatMessage("&b&l|   ", false));
        scoreboard.setText(3, new ChatMessage("&b&l|  ", false));
        scoreboard.setText(2, new ChatMessage("&b&l| ", false));
        scoreboard.setText(1, new ChatMessage("&b&l|&e example.com", false));
        //Remove line witch is 1
        scoreboard.removeText(1);
        //Set title display name
        scoreboard.setTitle(new ChatMessage("New Display", false));
        //Make a player cannot see the scoreboard
        scoreboard.removeViewer(player);
        //Drop the scoreboard
        scoreboard.drop();
    }

    private static void npcExample(Player player) {
        //Create a new NPC
        EzNPC npc = new EzNPC(NPCType.PLAYER, new ChatMessage("I am NPC", false), player.getLocation());
        //Add a viewer
        npc.addViewer(player);
        //Make npc look at player
        npc.look(true);
        //Set npc item
        npc.setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
        npc.setItemInOffHand(new ItemStack(Material.SHIELD));
        npc.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        npc.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        npc.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        npc.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        npc.setData("Notch");
        //Listening to click event
        npc.setOnClick(((target, clickType) -> {
            target.sendMessage("NPC >> You clicked me");
            target.sendMessage("NPC >> You clicked me with " + clickType.name() + " click");
        }));
        //Remove a viewer
        npc.removeViewer(player);
        //Drop the npc
        npc.drop();
    }

    private static void hologramExample(Player player) {

        //Create a new hologram
        TextHologram hologram = new TextHologram(new ChatMessage("I am an example", false), player.getWorld(), player.getLocation());
        //Add a new viewer
        hologram.addViewer(player);
        //Set hologram text
        hologram.setText(new ChatMessage("&c&lNew Text", false));
        //Set hologram position
        hologram.setLocation(player.getLocation().clone().add(0.0, 10.0, 0.0));
        //Remove a viewer
        hologram.removeViewer(player);
        //Drop the hologram
        hologram.drop();
    }

    private static void bossBarExample(Player player) {
        //Create a new boss bar
        EzBossBar bossBar = new EzBossBar(new ChatMessage("Ender Dragon", false));
        //Add a new viewer
        bossBar.addViewer(player);
        //Set boss bar progress
        bossBar.setProgress(0.5f);
        //Set boss bar color
        bossBar.setColor(BarColor.BLUE);
        //Set boss bar style
        bossBar.setStyle(BarStyle.NOTCHED_20);
        //Make sky dark
        bossBar.setDarkenSky(true);
        //Play music to player
        bossBar.setPlayMusic(true);
        //Create fog
        bossBar.setCreateFog(true);
        //Make player cannot see the boss bar
        bossBar.hide(player);
        //Make player can see the boss bar
        bossBar.show(player);
        //Remove a viewer
        bossBar.removeViewer(player);
        //Drop the boss bar
        bossBar.drop();
    }

    private static void commandExample() {
        //Create a new command with name "example"
        EzCommand command = new EzCommand("example");
        //Called with 0 arguments
        command.executes((sender, argument) -> {
            sender.sendMessage(new ChatMessage("You didn't put any arguments!", false));
            return 1;
        });
        //Added first argument
        command.then(new EzCommand("testing")
                //Called with 1 arguments and first argument is "testing"
                .executes((sender, argument) -> {
                    sender.sendMessage(new ChatMessage("You put argument \"testing\"", false));
                    return 1;
                })
                //Added second argument
                .then(new EzArgument(BaseArguments.string(), "name")
                        //Suggestion, same as bukkit api TabCompleter
                        .suggest(((sender, suggestion) -> {
                            suggestion.suggest(sender.getName().toLowerCase());
                            suggestion.suggest("example");
                            return suggestion.buildFuture();
                        }))
                        //Called with 2 arguments and first argument is "testing", second argument is string
                        .executes((sender, argument) -> {
                            sender.sendMessage(new ChatMessage("You put argument \"testing\" and string \"" + /* Get argument input with EzArgumentHelper */ argument.getAsString("name") + "\"", false));
                            return 1;
                        })
                        //Added third argument
                        .then(new EzArgument(BaseArguments.integer(), "blabla")
                                //Suggestion, same as bukkit api TabCompleter
                                .suggest(((sender, suggestion) -> {
                                    suggestion.suggest(sender.getName().toLowerCase());
                                    suggestion.suggest(1);
                                    suggestion.suggest(15);
                                    return suggestion.buildFuture();
                                }))
                                //Called with 3 arguments and first argument is "testing", second argument is string, third argument is integer
                                .executes((sender, argument) -> {
                                    sender.sendMessage(new ChatMessage("You put argument \"testing\" and string \"" + /* Get argument input with EzArgumentHelper */ argument.getAsString("name") + "\", integer " + argument.getAsInteger("blabla"), false));
                                    return 1;
                                })
                        )
                )
        );
        //Added first argument
        //Argument Player will return a List<Player>
        command.then(new EzArgument(ArgumentPlayer.argumentType(), "targets")
                //No executes method, means if only targets argument will throw exception
                //Added second argument
                //ArgumentLocation position allows double, ArgumentBlockLocation position only allows integer.
                .then(new EzArgument(ArgumentLocation.argumentType(), "position")
                        .executes((sender, argument) -> {
                            argument.getAsPlayers("targets").forEach((player -> player.teleport(argument.getAsLocation("position"))));
                            return 1;
                        })
                )
        );
        //You must register your commands in your main class onEnable() method!
        //Register command with prefix "example"
        //You can call this command with "example" (If there is not any other command named "example") or "example:example" (If there is not any other command named "example" and registered with prefix "example")
        EzCommandManager.register("example", command);
    }

    private static void autoReloadFileExample() {
        File file = new File("plugins/EasyAPI/", "example.yml");
        FileUtils.create(file, true);
        AutoReloadFile autoReloadFile = new AutoReloadFile(EasyAPI.getInstance(), file);
        autoReloadFile.onModified(() -> {
            System.out.println("Modified");
            System.out.println("Content: " + FileUtils.readText(file));
        });
        autoReloadFile.onDeleted(() -> {
            System.out.println("Deleted");
        });
    }

}

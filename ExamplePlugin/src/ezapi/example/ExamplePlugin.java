package ezapi.example;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;
import org.ezapi.chat.ChatMessage;
import org.ezapi.command.EzArgument;
import org.ezapi.command.EzCommand;
import org.ezapi.command.argument.EzArgumentTypes;
import org.ezapi.module.hologram.TextHologram;
import org.ezapi.plugin.EasyPlugin;

public final class ExamplePlugin extends EasyPlugin {

    @Override
    public void enable() {
        registerCommand("custom", getExampleCommand());
        sendConsole("Enabled");
    }

    @Override
    public void disable() {
        sendConsole("Disabled");
    }

    private EzCommand getExampleCommand() {
        EzCommand command = new EzCommand("example", 4, "ez-api.example.command.example", PermissionDefault.OP);
        command.executes((sender, argument) -> {
            sender.sendMessage(new ChatMessage("You called a command named \"example\" without arguments", false));
            return 1;
        });
        command.then(new EzArgument(EzArgumentTypes.STRING, "name")
                .executes((sender, argument) -> {
                    sender.sendMessage(new ChatMessage("You called a command named \"example\" with one argument that is string", false));
                    sender.sendMessage(new ChatMessage("The name: " + argument.getAsString("name"), false));
                    return 1;
                })
        );
        command.then(new EzCommand("give")
                .then(new EzArgument(EzArgumentTypes.PLAYERS_SELECTOR, "targets")
                        .then(new EzArgument(EzArgumentTypes.ITEM_STACK, "item")
                                .executes((sender, argument) -> {
                                    int i = 0;
                                    ItemStack itemStack = argument.getAsItemStack("item", 1);
                                    for (Player player : argument.getAsPlayers("targets")) {
                                        player.getInventory().addItem(itemStack);
                                        i++;
                                    }
                                    if (i > 0) {
                                        sender.sendMessage(new ChatMessage("Give item to player successfully", false));
                                    }
                                    return i;
                                })
                                .then(new EzArgument(EzArgumentTypes.integer(1), "amount")
                                        .executes((sender, argument) -> {
                                            int i = 0;
                                            ItemStack itemStack = argument.getAsItemStack("item", argument.getAsInteger("amount"));
                                            for (Player player : argument.getAsPlayers("targets")) {
                                                player.getInventory().addItem(itemStack);
                                                i++;
                                            }
                                            if (i > 0) {
                                                sender.sendMessage(new ChatMessage("Give item to player successfully", false));
                                            }
                                            return i;
                                        })
                                )
                        )
                )
        );
        command.then(new EzCommand("hologram")
                .then(new EzArgument(EzArgumentTypes.STRING, "name")
                        .executes(((sender, argument) -> {
                            if (sender.isPlayer()) {
                                Player player = sender.player();
                                if (player != null) {
                                    Location position = player.getLocation();
                                    TextHologram hologram = new TextHologram(new ChatMessage("I am text", false), position.getWorld(), position);
                                    for (Player target : getOnlinePlayers()) {
                                        hologram.addViewer(target);
                                    }
                                    return 1;
                                }
                            }
                            return 0;
                        }))
                        .then(new EzArgument(EzArgumentTypes.WORLD, "world")
                                .then(new EzArgument(EzArgumentTypes.LOCATION, "position")
                                        .executes(((sender, argument) -> {
                                            Location position = argument.getAsLocation("position");
                                            position.setWorld(argument.getAsWorld("world"));
                                            TextHologram hologram = new TextHologram(new ChatMessage("I am text", false), position.getWorld(), position);
                                            for (Player target : getOnlinePlayers()) {
                                                hologram.addViewer(target);
                                            }
                                            return 1;
                                        }))
                                )
                        )
                )
        );
        return command;
    }

}

package org.ezapi.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.ezapi.chat.ChatMessage;
import org.ezapi.command.EzArgument;
import org.ezapi.command.EzCommand;
import org.ezapi.command.EzCommandManager;
import org.ezapi.command.EzCommandRegistered;
import org.ezapi.command.argument.*;
import org.ezapi.module.hologram.TextHologram;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class HologramCommand {

    private EzCommand ezCommand;

    private boolean registered = false;

    private final Map<String, TextHologram> holograms = new HashMap<>();

    private HologramCommand() {
        this.ezCommand = new EzCommand("hologram");
        ezCommand.then(new EzCommand("create")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .then(new EzArgument(ArgumentChat.argumentType(), "text")
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (!holograms.containsKey(name)) {
                                        Location location = new Location(Bukkit.getWorlds().get(0), 0.0, 100.0, 0.0);
                                        if (sender.isPlayer()) {
                                            location = sender.player().getLocation();
                                        }
                                        String text = argument.getAsChatMessage("text");
                                        TextHologram hologram = new TextHologram(new ChatMessage(text, false), Bukkit.getWorlds().get(0), location);
                                        holograms.put(name, hologram);
                                        return 1;
                                    }
                                    return 0;
                                }))
                        )
                )
        );
        ezCommand.then(new EzCommand("location")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .suggest((sender, suggestion) -> {
                            holograms.keySet().forEach(suggestion::suggest);
                            return suggestion.buildFuture();
                        })
                        .then(new EzArgument(ArgumentWorld.argumentType(), "world")
                                .then(new EzArgument(ArgumentLocation.argumentType(), "location")
                                        .executes(((sender, argument) -> {
                                            String name = argument.getAsString("name");
                                            if (holograms.containsKey(name)) {
                                                Location location = argument.getAsLocation("location");
                                                location.setWorld(argument.getAsWorld("world"));
                                                holograms.get(name).setLocation(location);
                                                return 1;
                                            }
                                            return 0;
                                        }))
                                )
                        )
                )
        );
        ezCommand.then(new EzCommand("add")
                .then(new EzArgument(ArgumentPlayer.argumentType(), "targets")
                        .then(new EzArgument(BaseArguments.string(), "name")
                                .suggest((sender, suggestion) -> {
                                    holograms.keySet().forEach(suggestion::suggest);
                                    return suggestion.buildFuture();
                                })
                                .executes(((sender, argument) -> {
                                    List<Player> targets = argument.getAsPlayers("targets");
                                    if (targets.size() > 0) {
                                        String name = argument.getAsString("name");
                                        if (holograms.containsKey(name)) {
                                            for (Player target : targets) {
                                                holograms.get(name).addViewer(target);
                                            }
                                            return 1;
                                        }
                                    }
                                    return 0;
                                }))
                        )
                )
        );
        ezCommand.then(new EzCommand("remove")
                .then(new EzCommand("player")
                        .then(new EzArgument(ArgumentPlayer.argumentType(), "targets")
                                .then(new EzArgument(BaseArguments.string(), "name")
                                        .suggest((sender, suggestion) -> {
                                            holograms.keySet().forEach(suggestion::suggest);
                                            return suggestion.buildFuture();
                                        })
                                        .executes(((sender, argument) -> {
                                            List<Player> targets = argument.getAsPlayers("targets");
                                            if (targets.size() > 0) {
                                                String name = argument.getAsString("name");
                                                if (holograms.containsKey(name)) {
                                                    for (Player target : targets) {
                                                        holograms.get(name).removeViewer(target);
                                                    }
                                                    return 1;
                                                }
                                            }
                                            return 0;
                                        }))
                                )
                        )
                )
                .then(new EzCommand("hologram")
                        .then(new EzArgument(BaseArguments.string(), "name")
                                .suggest((sender, suggestion) -> {
                                    holograms.keySet().forEach(suggestion::suggest);
                                    return suggestion.buildFuture();
                                })
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (holograms.containsKey(name)) {
                                        holograms.get(name).removeAll();
                                        holograms.remove(name);
                                        return 1;
                                    }
                                    return 0;
                                }))
                        )
                )
        );
    }

    public void register() {
        if (!registered) {
            EzCommandRegistered ezCommandRegistered = EzCommandManager.register(ezCommand);
            //ezCommandRegistered.addAliases("ezapi", "easyapi", "easy-api");
            registered = true;
        }
    }

    public EzCommand getEzCommand() {
        return ezCommand;
    }

}

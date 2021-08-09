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
import org.ezapi.module.npc.EzNPC;
import org.ezapi.module.npc.NPCType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class NPCCommand {

    private EzCommand ezCommand;

    private boolean registered = false;

    private final Map<String, EzNPC> npcs = new HashMap<>();

    //public private
    private NPCCommand() {
        this.ezCommand = new EzCommand("npc");
        ezCommand.then(new EzCommand("create")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .then(new EzCommand("player")
                                .then(new EzArgument(ArgumentChat.argumentType(), "text")
                                        .executes(((sender, argument) -> {
                                            String name = argument.getAsString("name");
                                            if (!npcs.containsKey(name)) {
                                                Location location = new Location(Bukkit.getWorlds().get(0), 0.0, 100.0, 0.0);
                                                if (sender.isPlayer()) {
                                                    location = sender.player().getLocation();
                                                }
                                                String text = argument.getAsChatMessage("text");
                                                EzNPC npc = new EzNPC(NPCType.PLAYER, new ChatMessage(text, false), location);
                                                this.npcs.put(name, npc);
                                                return 1;
                                            }
                                            return 0;
                                        }))
                                )
                        )
                        .then(new EzCommand("villager")
                                .then(new EzArgument(ArgumentChat.argumentType(), "text")
                                        .executes(((sender, argument) -> {
                                            String name = argument.getAsString("name");
                                            if (!npcs.containsKey(name)) {
                                                Location location = new Location(Bukkit.getWorlds().get(0), 0.0, 100.0, 0.0);
                                                if (sender.isPlayer()) {
                                                    location = sender.player().getLocation();
                                                }
                                                String text = argument.getAsChatMessage("text");
                                                EzNPC npc = new EzNPC(NPCType.VILLAGER, new ChatMessage(text, false), location);
                                                this.npcs.put(name, npc);
                                                return 1;
                                            }
                                            return 0;
                                        }))
                                )
                        )
                )
        );
        ezCommand.then(new EzCommand("move")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .suggest((sender, suggestion) -> {
                            npcs.keySet().forEach(suggestion::suggest);
                            return suggestion.buildFuture();
                        })
                        .then(new EzArgument(ArgumentLocation.argumentType(), "position")
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (npcs.containsKey(name)) {
                                        this.npcs.get(name).move(argument.getAsLocation("position"));
                                        return 1;
                                    }
                                    return 0;
                                }))
                        )
                )
        );
        ezCommand.then(new EzCommand("skin")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .suggest((sender, suggestion) -> {
                            npcs.keySet().forEach(suggestion::suggest);
                            return suggestion.buildFuture();
                        })
                        .then(new EzArgument(BaseArguments.string(), "owner")
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (npcs.containsKey(name)) {
                                        String owner = argument.getAsString("owner");
                                        this.npcs.get(name).setData(owner);
                                        return 1;
                                    }
                                    return 0;
                                }))
                        )
                )
        );
        ezCommand.then(new EzCommand("look")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .suggest((sender, suggestion) -> {
                            npcs.keySet().forEach(suggestion::suggest);
                            return suggestion.buildFuture();
                        })
                        .then(new EzArgument(BaseArguments.bool(), "look")
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (npcs.containsKey(name)) {
                                        boolean look = argument.getAsBoolean("look");
                                        this.npcs.get(name).look(look);
                                        return 1;
                                    }
                                    return 0;
                                }))
                        )
                )
        );
        EzArgument typeNameArgument = new EzArgument(BaseArguments.string(), "name");
        typeNameArgument.suggest((sender, suggestion) -> {
            npcs.keySet().forEach(suggestion::suggest);
            return suggestion.buildFuture();
        });
        for (NPCType<?> npcType : NPCType.values()) {
            typeNameArgument.then(new EzCommand(npcType.name())
                    .executes(((sender, argument) -> {
                        String name = argument.getAsString("name");
                        if (npcs.containsKey(name)) {
                            if (this.npcs.get(name).getType() != npcType) {
                                try {
                                    this.npcs.get(name).setType(npcType);
                                } catch (Exception e) {
                                    e.printStackTrace(System.out);
                                }
                            }
                            return 1;
                        }
                        return 0;
                    }))
            );
        }
        ezCommand.then(new EzCommand("type")
                .then(typeNameArgument
                )
        );
        ezCommand.then(new EzCommand("location")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .suggest((sender, suggestion) -> {
                            npcs.keySet().forEach(suggestion::suggest);
                            return suggestion.buildFuture();
                        })
                        .then(new EzArgument(ArgumentLocation.argumentType(), "location")
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (npcs.containsKey(name)) {
                                        Location location = argument.getAsLocation("location");
                                        npcs.get(name).setLocation(location);
                                        return 1;
                                    }
                                    return 0;
                                }))
                        )
                )
        );
        ezCommand.then(new EzCommand("add")
                .then(new EzArgument(ArgumentPlayer.argumentType(), "targets")
                        .then(new EzArgument(BaseArguments.string(), "name")
                                .suggest((sender, suggestion) -> {
                                    npcs.keySet().forEach(suggestion::suggest);
                                    return suggestion.buildFuture();
                                })
                                .executes(((sender, argument) -> {
                                    List<Player> targets = argument.getAsPlayers("targets");
                                    if (targets.size() > 0) {
                                        String name = argument.getAsString("name");
                                        if (npcs.containsKey(name)) {
                                            for (Player target : targets) {
                                                npcs.get(name).addViewer(target);
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
                .then(new EzCommand("npc")
                        .then(new EzArgument(BaseArguments.string(), "name")
                                .suggest((sender, suggestion) -> {
                                    npcs.keySet().forEach(suggestion::suggest);
                                    return suggestion.buildFuture();
                                })
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (npcs.containsKey(name)) {
                                        npcs.get(name).removeAll();
                                        npcs.remove(name);
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

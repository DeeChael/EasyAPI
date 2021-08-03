package org.ezapi.command.defaults;

import org.ezapi.chat.ChatMessage;
import org.ezapi.command.EzArgument;
import org.ezapi.command.EzCommand;
import org.ezapi.command.EzCommandManager;
import org.ezapi.command.EzCommandRegistered;
import org.ezapi.command.argument.*;
import org.ezapi.module.scoreboard.EzScoreboard;

import java.util.HashMap;
import java.util.Map;

final class SBCommand {

    private EzCommand ezCommand;

    private boolean registered = false;

    private final Map<String, EzScoreboard> scoreboards = new HashMap<>();

    private SBCommand() {
        this.ezCommand = new EzCommand("sb");
        ezCommand.then(new EzCommand("create")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .then(new EzArgument(ArgumentChat.argumentType(), "display")
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (!scoreboards.containsKey(name)) {
                                        String text = argument.getAsChatMessage("display");
                                        EzScoreboard scoreboard = new EzScoreboard(new ChatMessage(text, false));
                                        scoreboards.put(name, scoreboard);
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
                                .suggest(((sender, suggestion) -> {
                                    scoreboards.keySet().forEach(suggestion::suggest);
                                    return suggestion.buildFuture();
                                }))
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (scoreboards.containsKey(name)) {
                                        argument.getAsPlayers("targets").forEach(scoreboards.get(name)::addViewer);
                                        return 1;
                                    }
                                    return 0;
                                }))
                        )
                )
        );
        ezCommand.then(new EzCommand("set")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .suggest(((sender, suggestion) -> {
                            scoreboards.keySet().forEach(suggestion::suggest);
                            return suggestion.buildFuture();
                        }))
                        .then(new EzArgument(BaseArguments.integer(), "line")
                                .then(new EzArgument(ArgumentChat.argumentType(), "text")
                                        .executes(((sender, argument) -> {
                                            String name = argument.getAsString("name");
                                            if (scoreboards.containsKey(name)) {
                                                scoreboards.get(name).setText(argument.getAsInteger("line"), new ChatMessage(argument.getAsChatMessage("text"), false));
                                                return 1;
                                            }
                                            return 0;
                                        }))
                                )
                        )
                )
        );
        ezCommand.then(new EzCommand("display")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .suggest(((sender, suggestion) -> {
                            scoreboards.keySet().forEach(suggestion::suggest);
                            return suggestion.buildFuture();
                        }))
                        .then(new EzArgument(ArgumentChat.argumentType(), "text")
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (scoreboards.containsKey(name)) {
                                        scoreboards.get(name).setTitle(new ChatMessage(argument.getAsChatMessage("text"), false));
                                        return 1;
                                    }
                                    return 0;
                                }))
                        )
                )
        );
        ezCommand.then(new EzCommand("remove")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .suggest(((sender, suggestion) -> {
                            scoreboards.keySet().forEach(suggestion::suggest);
                            return suggestion.buildFuture();
                        }))
                        .then(new EzArgument(BaseArguments.integer(), "line")
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (scoreboards.containsKey(name)) {
                                        scoreboards.get(name).removeText(argument.getAsInteger("line"));
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

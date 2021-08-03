package org.ezapi.command.defaults;

import org.ezapi.chat.ChatMessage;
import org.ezapi.command.EzArgument;
import org.ezapi.command.EzCommand;
import org.ezapi.command.EzCommandManager;
import org.ezapi.command.EzCommandRegistered;
import org.ezapi.command.argument.ArgumentChat;
import org.ezapi.command.argument.ArgumentPlayer;
import org.ezapi.command.argument.BaseArguments;
import org.ezapi.module.bossbar.BarColor;
import org.ezapi.module.bossbar.BarStyle;
import org.ezapi.module.bossbar.EzBossBar;

import java.util.HashMap;
import java.util.Map;

final class BBarCommand {

    private EzCommand ezCommand;

    private boolean registered = false;

    private final Map<String, EzBossBar> bossBars = new HashMap<>();

    private BBarCommand() {
        this.ezCommand = new EzCommand("bbar");
        ezCommand.then(new EzCommand("create")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .then(new EzArgument(ArgumentChat.argumentType(), "display")
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (!bossBars.containsKey(name)) {
                                        String text = argument.getAsChatMessage("display");
                                        EzBossBar bossBar = new EzBossBar(new ChatMessage(text, false));
                                        bossBars.put(name, bossBar);
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
                                    bossBars.keySet().forEach(suggestion::suggest);
                                    return suggestion.buildFuture();
                                }))
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (bossBars.containsKey(name)) {
                                        argument.getAsPlayers("targets").forEach(bossBars.get(name)::addViewer);
                                        return 1;
                                    }
                                    return 0;
                                }))
                        )
                )
        );
        EzCommand color = new EzCommand("color");
        EzArgument colorBossBarSelector = new EzArgument(BaseArguments.string(), "name").suggest(((sender, suggestion) -> {
            bossBars.keySet().forEach(suggestion::suggest);
            return suggestion.buildFuture();
        }));
        for (BarColor barColor : BarColor.values()) {
            colorBossBarSelector.then(new EzCommand(barColor.name().toLowerCase())
                    .executes(((sender, argument) -> {
                        String name = argument.getAsString("name");
                        if (bossBars.containsKey(name)) {
                            bossBars.get(name).setColor(barColor);
                            return 1;
                        }
                        return 0;
                    }))
            );
        }
        color.then(colorBossBarSelector);
        EzCommand style = new EzCommand("style");
        EzArgument styleBossBarSelector = new EzArgument(BaseArguments.string(), "name").suggest(((sender, suggestion) -> {
            bossBars.keySet().forEach(suggestion::suggest);
            return suggestion.buildFuture();
        }));
        for (BarStyle barStyle : BarStyle.values()) {
            styleBossBarSelector.then(new EzCommand(barStyle.name().toLowerCase())
                    .executes(((sender, argument) -> {
                        String name = argument.getAsString("name");
                        if (bossBars.containsKey(name)) {
                            bossBars.get(name).setStyle(barStyle);
                            return 1;
                        }
                        return 0;
                    }))
            );
        }
        style.then(styleBossBarSelector);
        ezCommand.then(color);
        ezCommand.then(style);
        ezCommand.then(new EzCommand("progress")
                .then(new EzArgument(BaseArguments.string(), "name")
                        .suggest(((sender, suggestion) -> {
                            bossBars.keySet().forEach(suggestion::suggest);
                            return suggestion.buildFuture();
                        }))
                        .then(new EzArgument(BaseArguments.floatArg(0.0f, 1.0f), "progress")
                                .executes(((sender, argument) -> {
                                    String name = argument.getAsString("name");
                                    if (bossBars.containsKey(name)) {
                                        bossBars.get(name).setProgress(argument.getAsFloat("progress"));
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

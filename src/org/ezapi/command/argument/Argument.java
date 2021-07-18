package org.ezapi.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;

public interface Argument {

    ArgumentType<Object> argument();

    Object get(CommandContext<Object> commandContext, String name);

}

package org.ezapi.command;

import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ezapi.chat.ChatMessage;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.PlayerUtils;
import org.ezapi.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.UUID;

public final class EzSender {

    private final Object commandListenerWrapper;

    public EzSender(Object commandListenerWrapper) {
        if (commandListenerWrapper.getClass() != CommandListenerWrapper()) throw new IllegalArgumentException("The argument did not CommandListenerWrapper");
        this.commandListenerWrapper = commandListenerWrapper;
    }

    public String getName() {
        try {
            return (String) CommandListenerWrapper().getMethod("getName").invoke(commandListenerWrapper);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return "EzSender";
    }

    public boolean isPlayer() {
        try {
            CommandSender commandSender = ((CommandSender) CommandListenerWrapper().getMethod("getBukkitSender").invoke(commandListenerWrapper));
            return commandSender instanceof Player;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Player player() {
        try {
            Object object = CommandListenerWrapper().getMethod("h").invoke(commandListenerWrapper);
            return (Player) object.getClass().getMethod("getBukkitEntity").invoke(object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendMessage(ChatMessage chatMessage) {
        try {
            if (isPlayer()) {
                PlayerUtils.sendMessage(player(), chatMessage);
            } else {
                CommandListenerWrapper().getMethod("sendMessage", IChatBaseComponent(), boolean.class).invoke(commandListenerWrapper, createChatMessage(chatMessage.getText()), false);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static Object createChatMessage(String message) {
        try {
            return ChatMessage().getConstructor(String.class).newInstance(message);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ChatMessage() {
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("ChatMessage");
        } else {
            return ReflectionUtils.getClass("net.minecraft.network.chat.ChatMessage");
        }
    }

    private static Class<?> IChatBaseComponent() {
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("IChatBaseComponent");
        } else {
            return ReflectionUtils.getClass("net.minecraft.network.chat.IChatBaseComponent");
        }
    }

    private static Class<?> CommandListenerWrapper() {
        if (ReflectionUtils.getVersion() <= 15 && ReflectionUtils.getVersion() >= 9) {
            return ReflectionUtils.getNmsClass("CommandListenerWrapper");
        } else {
            return ReflectionUtils.getClass("net.minecraft.commands.CommandListenerWrapper");
        }
    }

}

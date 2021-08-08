package org.ezapi.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ezapi.EasyAPI;
import org.ezapi.chat.ChatMessage;
import org.ezapi.util.PlayerUtils;
import org.ezapi.util.Ref;

import java.lang.reflect.InvocationTargetException;

public final class EzSender {

    private final Object commandListenerWrapper;

    /**
     * You shouldn't create a sender
     * @param commandListenerWrapper command sender
     */
    public EzSender(Object commandListenerWrapper) {
        if (commandListenerWrapper.getClass() != CommandListenerWrapper()) throw new IllegalArgumentException("The argument did not CommandListenerWrapper");
        this.commandListenerWrapper = commandListenerWrapper;
    }

    /**
     * Get sender name
     *
     * @return sender's name
     */
    public String getName() {
        try {
            return (String) CommandListenerWrapper().getMethod("getName").invoke(commandListenerWrapper);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return "EzSender";
    }

    /**
     * Get if sender is a player
     *
     * @return Is sender a player
     */
    public boolean isPlayer() {
        try {
            CommandSender commandSender = ((CommandSender) CommandListenerWrapper().getMethod("getBukkitSender").invoke(commandListenerWrapper));
            return commandSender instanceof Player;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get player
     *
     * @return if sender is player return player or else null
     */
    public Player player() {
        try {
            Object object = CommandListenerWrapper().getMethod("h").invoke(commandListenerWrapper);
            return (Player) object.getClass().getMethod("getBukkitEntity").invoke(object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send message to sender, console default locale is english (us)
     * @param chatMessage message
     */
    public void sendMessage(ChatMessage chatMessage) {
        try {
            if (isPlayer()) {
                PlayerUtils.sendMessage(player(), chatMessage);
            } else {
                CommandListenerWrapper().getMethod("sendMessage", IChatBaseComponent(), boolean.class).invoke(commandListenerWrapper, createChatMessage(chatMessage.getText(EasyAPI.getLanguage())), false);
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
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ChatMessage");
        } else {
            return Ref.getClass("net.minecraft.network.chat.ChatMessage");
        }
    }

    private static Class<?> IChatBaseComponent() {
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("IChatBaseComponent");
        } else {
            return Ref.getClass("net.minecraft.network.chat.IChatBaseComponent");
        }
    }

    private static Class<?> CommandListenerWrapper() {
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("CommandListenerWrapper");
        } else {
            return Ref.getClass("net.minecraft.commands.CommandListenerWrapper");
        }
    }

}

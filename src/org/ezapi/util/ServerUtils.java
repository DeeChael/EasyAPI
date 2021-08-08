package org.ezapi.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;
import org.ezapi.chat.ChatMessage;
import org.ezapi.reflect.EzClass;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class ServerUtils {

    public static double[] getTps() {
        EzClass MinecraftServer = new EzClass(Ref.getNmsOrOld("server.MinecraftServer", "MinecraftServer"));
        EzClass CraftServer = new EzClass(Ref.getObcClass("CraftServer"));
        CraftServer.setInstance(Bukkit.getServer());
        MinecraftServer.setInstance(CraftServer.invokeMethod("getServer", new Class[0], new Object[0]));
        return (double[]) MinecraftServer.getField("recentTps");
    }

    public static void setMotd(String motd) {
        EzClass MinecraftServer = new EzClass(Ref.getNmsOrOld("server.MinecraftServer", "MinecraftServer"));
        EzClass CraftServer = new EzClass(Ref.getObcClass("CraftServer"));
        CraftServer.setInstance(Bukkit.getServer());
        MinecraftServer.setInstance(CraftServer.invokeMethod("getServer", new Class[0], new Object[0]));
        MinecraftServer.invokeMethod("setMotd", new Class[] {String.class}, new Object[] {motd});
    }

    public static void setFavicon(File file) {
        if (!file.isFile() && file.getName().endsWith(".png")) {
            EzClass MinecraftServer = new EzClass(Ref.getNmsOrOld("server.MinecraftServer", "MinecraftServer"));
            EzClass CraftServer = new EzClass(Ref.getObcClass("CraftServer"));
            CraftServer.setInstance(Bukkit.getServer());
            MinecraftServer.setInstance(CraftServer.invokeMethod("getServer", new Class[0], new Object[0]));
            EzClass ServerPing = new EzClass(Ref.getNmsOrOld("network.protocol.status.ServerPing", "ServerPing"));
            ServerPing.setInstance(MinecraftServer.invokeMethod("getServerPing", new Class[0], new Object[0]));
            ByteBuf bytebuf = Unpooled.buffer();
            try {
                BufferedImage bufferedimage = ImageIO.read(file);
                if (bufferedimage.getWidth() == 64 && bufferedimage.getHeight() == 64) {
                    ImageIO.write(bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
                    ByteBuffer bytebuffer = Base64.getEncoder().encode(bytebuf.nioBuffer());
                    ServerPing.invokeMethod("setFavicon", new Class[]{ServerPing.getInstanceClass()}, new Object[]{"data:image/png;base64," + StandardCharsets.UTF_8.decode(bytebuffer)});
                }
            } catch (Exception ignored) {
            } finally {
                bytebuf.release();
            }
        }
    }

    public static void sendMessage(ChatMessage message) {
        sendMessage(message, "en_us");
    }

    public static void sendMessage(ChatMessage message, String locale) {
        Bukkit.getConsoleSender().sendMessage(message.getText(locale));
    }

}

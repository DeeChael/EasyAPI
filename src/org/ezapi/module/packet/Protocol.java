package org.ezapi.module.packet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.mojang.authlib.GameProfile;
import org.ezapi.util.Ref;

/**
 * Forked from https://github.com/aadnk/ProtocolLib/blob/master/TinyProtocol/src/main/java/com/comphenix/tinyprotocol/TinyProtocol.java
 */
public abstract class Protocol {

    private static final AtomicInteger ID = new AtomicInteger(0);

    private static final Method getPlayerHandle = Ref.getMethod(Ref.getObcClass("entity.CraftPlayer"), "getHandle");

    private static final Field getConnection = Ref.getFieldOrOld(Ref.getNmsOrOld("server.level.EntityPlayer", "EntityPlayer"), "b", "playerConnection");
    private static final Field getManager = Ref.getFieldOrOld(Ref.getNmsOrOld("server.network.PlayerConnection", "PlayerConnection"), "a", "networkManager");
    private static final Field getChannel = Ref.getFieldOrOld(Ref.getNmsOrOld("network.NetworkManager", "NetworkManager"), "k", "channel");

    private static final Class<?> minecraftServerClass = Ref.getNmsOrOld("server.MinecraftServer", "MinecraftServer");
    private static final Class<?> serverConnectionClass;
    private static final Field getMinecraftServer = Ref.getField(Ref.getObcClass("CraftServer"), "console");
    private static final Field getServerConnection = Ref.getFieldOrOld(minecraftServerClass, "K", "serverConnection");
    private static final Field getNetworkMarkers;
    private static final Field getChannels;

    static {
        serverConnectionClass = Ref.getNmsOrOld("server.network.ServerConnection", "ServerConnection");
        if (Ref.getVersion() == 12) {
            getNetworkMarkers = Ref.getField(serverConnectionClass, "listeningChannels");
        } else if (Ref.getVersion() >= 12 && Ref.getVersion() <= 15) {
            getNetworkMarkers = Ref.getField(serverConnectionClass, "connectedChannels");
        } else {
            getNetworkMarkers = Ref.getField(serverConnectionClass, "g");
        }
        if (Ref.getVersion() >= 12 && Ref.getVersion() <= 15) {
            getChannels = Ref.getField(serverConnectionClass, "listeningChannels");
        } else {
            getChannels = Ref.getField(serverConnectionClass, "f");
        }
    }

    private static final Class<?> PACKET_LOGIN_IN_START = Ref.getNmsOrOld("network.protocol.login.PacketLoginInStart", "PacketLoginInStart");
    private static final Field getGameProfile = Ref.getField(PACKET_LOGIN_IN_START, "a");

    private Map<String, Channel> channelLookup = new MapMaker().weakValues().makeMap();
    private Listener listener;

    private Set<Channel> uninjectedChannels = Collections.newSetFromMap(new MapMaker().weakKeys().<Channel, Boolean>makeMap());

    private List<Object> networkManagers;

    private List<Channel> serverChannels = Lists.newArrayList();
    private ChannelInboundHandlerAdapter serverChannelHandler;
    private ChannelInitializer<Channel> beginInitProtocol;
    private ChannelInitializer<Channel> endInitProtocol;

    private String handlerName;

    protected volatile boolean closed;
    protected Plugin plugin;

    public Protocol(final Plugin plugin) {
        this.plugin = plugin;

        this.handlerName = getHandlerName();

        registerBukkitEvents();

        try {
            registerChannelHandler();
            registerPlayers(plugin);
            plugin.getLogger().info("Protocol Ready");
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            plugin.getLogger().info("Delaying server channel injection due to late bind.");

            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        registerChannelHandler();
                    } catch (IllegalAccessException ignored) {
                    }
                    registerPlayers(plugin);
                    plugin.getLogger().info("Late bind injection successful.");
                }
            }.runTask(plugin);
        }
    }

    private void createServerChannelHandler() {
        endInitProtocol = new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel channel) throws Exception {
                try {
                    synchronized (networkManagers) {
                        if (!closed) {
                            channel.eventLoop().submit(() -> injectChannelInternal(channel));
                        }
                    }
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Cannot inject incomming channel " + channel, e);
                }
            }

        };

        beginInitProtocol = new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline().addLast(endInitProtocol);
            }

        };

        serverChannelHandler = new ChannelInboundHandlerAdapter() {

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                Channel channel = (Channel) msg;

                channel.pipeline().addFirst(beginInitProtocol);
                ctx.fireChannelRead(msg);
            }

        };
    }

    private void registerBukkitEvents() {
        listener = new Listener() {

            @EventHandler(priority = EventPriority.LOWEST)
            public final void onPlayerLogin(PlayerLoginEvent e) throws InvocationTargetException, IllegalAccessException {
                if (closed)
                    return;

                Channel channel = getChannel(e.getPlayer());

                if (!uninjectedChannels.contains(channel)) {
                    injectPlayer(e.getPlayer());
                }
            }

            @EventHandler
            public final void onPluginDisable(PluginDisableEvent e) {
                if (e.getPlugin().equals(plugin)) {
                    close();
                }
            }

        };

        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    private void registerChannelHandler() throws IllegalAccessException {
        Object mcServer = getMinecraftServer.get(Bukkit.getServer());
        Object serverConnection = getServerConnection.get(mcServer);
        boolean looking = true;

        networkManagers = (List<Object>) getNetworkMarkers.get(serverConnection);
        createServerChannelHandler();

        for (int i = 0; looking; i++) {
            List<ChannelFuture> list = (List<ChannelFuture>) getChannels.get(serverConnection);

            for (ChannelFuture item : list) {
                Channel serverChannel = item.channel();

                serverChannels.add(serverChannel);
                serverChannel.pipeline().addFirst(serverChannelHandler);
                looking = false;
            }
        }
    }

    private void unregisterChannelHandler() {
        if (serverChannelHandler == null)
            return;

        for (Channel serverChannel : serverChannels) {
            final ChannelPipeline pipeline = serverChannel.pipeline();

            serverChannel.eventLoop().execute(() -> {
                try {
                    pipeline.remove(serverChannelHandler);
                } catch (NoSuchElementException ignored) {
                }
            });
        }
    }

    private void registerPlayers(Plugin plugin) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            injectPlayer(player);
        }
    }

    public Object onPacketOutAsync(Player receiver, Channel channel, Object packet) {
        return packet;
    }

    public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
        return packet;
    }

    public void sendPacket(Player player, Object packet) {
        try {
            sendPacket(getChannel(player), packet);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    public void sendPacket(Channel channel, Object packet) {
        channel.pipeline().writeAndFlush(packet);
    }

    public void receivePacket(Player player, Object packet) {
        try {
            receivePacket(getChannel(player), packet);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    public void receivePacket(Channel channel, Object packet) {
        channel.pipeline().context("encoder").fireChannelRead(packet);
    }

    protected String getHandlerName() {
        return "ezapi-" + plugin.getName() + "-" + ID.incrementAndGet();
    }

    public void injectPlayer(Player player) {
        try {
            injectChannelInternal(getChannel(player)).player = player;
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    public void injectChannel(Channel channel) {
        injectChannelInternal(channel);
    }

    private PacketInterceptor injectChannelInternal(Channel channel) {
        try {
            PacketInterceptor interceptor = (PacketInterceptor) channel.pipeline().get(handlerName);

            if (interceptor == null) {
                interceptor = new PacketInterceptor();
                channel.pipeline().addBefore("packet_handler", handlerName, interceptor);
                uninjectedChannels.remove(channel);
            }

            return interceptor;
        } catch (IllegalArgumentException e) {
            return (PacketInterceptor) channel.pipeline().get(handlerName);
        }
    }

    public Channel getChannel(Player player) throws IllegalAccessException, InvocationTargetException {
        Channel channel = channelLookup.get(player.getName());

        if (channel == null) {
            Object connection = getConnection.get(getPlayerHandle.invoke(player));
            Object manager = getManager.get(connection);

            channelLookup.put(player.getName(), channel = (Channel) getChannel.get(manager));
        }

        return channel;
    }

    public void uninjectPlayer(Player player) {
        try {
            uninjectChannel(getChannel(player));
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    public void uninjectChannel(final Channel channel) {
        if (!closed) {
            uninjectedChannels.add(channel);
        }

        channel.eventLoop().execute(() -> channel.pipeline().remove(handlerName));
    }

    public boolean hasInjected(Player player) throws InvocationTargetException, IllegalAccessException {
        return hasInjected(getChannel(player));
    }

    public boolean hasInjected(Channel channel) {
        return channel.pipeline().get(handlerName) != null;
    }

    public final void close() {
        if (!closed) {
            closed = true;

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                uninjectPlayer(player);
            }

            HandlerList.unregisterAll(listener);
            unregisterChannelHandler();
        }
    }

    private final class PacketInterceptor extends ChannelDuplexHandler {

        public volatile Player player;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            final Channel channel = ctx.channel();
            handleLoginStart(channel, msg);

            try {
                msg = onPacketInAsync(player, channel, msg);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error in onPacketInAsync().", e);
            }

            if (msg != null) {
                super.channelRead(ctx, msg);
            }
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            try {
                msg = onPacketOutAsync(player, ctx.channel(), msg);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error in onPacketOutAsync().", e);
            }

            if (msg != null) {
                super.write(ctx, msg, promise);
            }
        }

        private void handleLoginStart(Channel channel, Object packet) throws IllegalAccessException {
            if (PACKET_LOGIN_IN_START.isInstance(packet)) {
                GameProfile profile = (GameProfile) getGameProfile.get(packet);
                channelLookup.put(profile.getName(), channel);
            }
        }
    }

}
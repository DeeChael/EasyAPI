package org.ezapi.module.bossbar;

import org.bukkit.entity.Player;
import org.ezapi.chat.ChatMessage;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.DateUtils;
import org.ezapi.util.PlayerUtils;
import org.ezapi.util.Ref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EzBossBar implements BossBar{

    private ChatMessage title;

    private BarColor color = BarColor.PINK;

    private BarStyle style = BarStyle.PROGRESS;

    private float progress = 1.0f;

    private final Map<Player, EzClass> viewers = new HashMap<>();

    private final List<Player> hasShown = new ArrayList<>();

    private boolean dropped;

    private boolean darkenSky;
    private boolean playMusic;
    private boolean createFog;

    public EzBossBar(ChatMessage title) {
        this.title = title;
    }

    public void setProgress(int percent) {
        if (isDropped()) return;
        setProgress((float) (((double) percent) / 100));
    }

    public void setProgress(float progress) {
        if (isDropped()) return;
        this.progress = Math.min(1.0f, Math.max(progress, 0.0f));
        if (getViewers().size() == 0) return;
        for (Player player : getViewers()) {
            if (!hasShown.contains(player)) continue;
            EzClass PacketPlayOutBoss = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutBoss", "PacketPlayOutBoss"));
            EzClass BossBattle = new EzClass(Ref.getNmsOrOld("world.BossBattle", "BossBattle"));

            BossBattle.setInstance(viewers.get(player).getInstance());

            if (Ref.getVersion() >= 16) {
                BossBattle.invokeMethod("setProgress", new Class[] {float.class}, new Object[] {progress});
            } else {
                BossBattle.invokeMethod("a", new Class[] {float.class}, new Object[] {progress});
            }

            if (Ref.getVersion() >= 16) {
                PacketPlayOutBoss.setInstance(PacketPlayOutBoss.invokeStaticMethod("createUpdateProgressPacket", new Class[] {BossBattle.getInstanceClass()}, new Object[] {BossBattle.getInstance()}));
            } else {
                EzEnum Action = new EzEnum(Ref.getNmsClass("PacketPlayOutBoss$Action"));
                Action.newInstance("UPDATE_PCT");
                PacketPlayOutBoss.setConstructor(Action.getInstanceEnum(), BossBattle.getInstanceClass());
                PacketPlayOutBoss.newInstance(Action.getInstance(), BossBattle.getInstance());
            }
            PlayerUtils.sendPacket(player, PacketPlayOutBoss.getInstance());
        }
    }

    public float getProgress() {
        if (isDropped()) return 0.0f;
        return progress;
    }

    public void setDarkenSky(boolean darkenSky) {
        if (isDropped()) return;
        this.darkenSky = darkenSky;
        updateProperties();
    }

    public void setCreateFog(boolean createFog) {
        if (isDropped()) return;
        this.createFog = createFog;
        updateProperties();
    }

    public void setPlayMusic(boolean playMusic) {
        if (isDropped()) return;
        this.playMusic = playMusic;
        updateProperties();
    }

    private void updateProperties() {
        if (isDropped()) return;
        if (getViewers().size() == 0) return;
        for (Player player : getViewers()) {
            if (!hasShown.contains(player)) continue;
            EzClass PacketPlayOutBoss = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutBoss", "PacketPlayOutBoss"));
            EzClass BossBattle = new EzClass(Ref.getNmsOrOld("world.BossBattle", "BossBattle"));

            BossBattle.setInstance(viewers.get(player).getInstance());

            if (Ref.getVersion() >= 16) {
                BossBattle.invokeMethod("setDarkenSky", new Class[] {boolean.class}, new Object[] {darkenSky});
                BossBattle.invokeMethod("setPlayMusic", new Class[] {boolean.class}, new Object[] {playMusic});
                BossBattle.invokeMethod("setCreateFog", new Class[] {boolean.class}, new Object[] {createFog});
            } else {
                BossBattle.invokeMethod("a", new Class[] {boolean.class}, new Object[] {darkenSky});
                BossBattle.invokeMethod("b", new Class[] {boolean.class}, new Object[] {playMusic});
                BossBattle.invokeMethod("c", new Class[] {boolean.class}, new Object[] {createFog});
            }

            if (Ref.getVersion() >= 16) {
                PacketPlayOutBoss.setInstance(PacketPlayOutBoss.invokeStaticMethod("createUpdatePropertiesPacket", new Class[] {BossBattle.getInstanceClass()}, new Object[] {BossBattle.getInstance()}));
            } else {
                EzEnum Action = new EzEnum(Ref.getNmsClass("PacketPlayOutBoss$Action"));
                Action.newInstance("UPDATE_PROPERTIES");
                PacketPlayOutBoss.setConstructor(Action.getInstanceEnum(), BossBattle.getInstanceClass());
                PacketPlayOutBoss.newInstance(Action.getInstance(), BossBattle.getInstance());
            }
            PlayerUtils.sendPacket(player, PacketPlayOutBoss.getInstance());
        }
    }

    public boolean isDarkenSky() {
        if (isDropped()) return false;
        return darkenSky;
    }

    public boolean isCreateFog() {
        if (isDropped()) return false;
        return createFog;
    }

    public boolean isPlayMusic() {
        if (isDropped()) return false;
        return playMusic;
    }

    @Override
    public BarStyle getStyle() {
        if (isDropped()) return null;
        return this.style;
    }

    @Override
    public void setStyle(BarStyle style) {
        if (isDropped()) return;
        this.style = style;
        if (getViewers().size() == 0) return;
        for (Player player : getViewers()) {
            if (!hasShown.contains(player)) continue;
            EzClass PacketPlayOutBoss = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutBoss", "PacketPlayOutBoss"));
            EzClass BossBattle = new EzClass(Ref.getNmsOrOld("world.BossBattle", "BossBattle"));

            EzEnum BarStyle = this.style.getNms();

            BossBattle.setInstance(viewers.get(player).getInstance());

            BossBattle.invokeMethod("a", new Class[] {BarStyle.getInstanceEnum()}, new Object[] {BarStyle.getInstance()});

            if (Ref.getVersion() >= 16) {
                PacketPlayOutBoss.setInstance(PacketPlayOutBoss.invokeStaticMethod("createUpdateStylePacket", new Class[] {BossBattle.getInstanceClass()}, new Object[] {BossBattle.getInstance()}));
            } else {
                EzEnum Action = new EzEnum(Ref.getNmsClass("PacketPlayOutBoss$Action"));
                Action.newInstance("UPDATE_STYLE");
                PacketPlayOutBoss.setConstructor(Action.getInstanceEnum(), BossBattle.getInstanceClass());
                PacketPlayOutBoss.newInstance(Action.getInstance(), BossBattle.getInstance());
            }
            PlayerUtils.sendPacket(player, PacketPlayOutBoss.getInstance());
        }
    }

    @Override
    public BarColor getColor() {
        if (isDropped()) return null;
        return this.color;
    }

    @Override
    public void setColor(BarColor color) {
        if (isDropped()) return;
        this.color = color;
        if (getViewers().size() == 0) return;
        for (Player player : getViewers()) {
            if (!hasShown.contains(player)) continue;
            EzClass PacketPlayOutBoss = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutBoss", "PacketPlayOutBoss"));
            EzClass BossBattle = new EzClass(Ref.getNmsOrOld("world.BossBattle", "BossBattle"));

            EzEnum BarColor = this.color.getNms();

            BossBattle.setInstance(viewers.get(player).getInstance());

            BossBattle.invokeMethod("a", new Class[] {BarColor.getInstanceEnum()}, new Object[] {BarColor.getInstance()});

            if (Ref.getVersion() >= 16) {
                PacketPlayOutBoss.setInstance(PacketPlayOutBoss.invokeStaticMethod("createUpdateStylePacket", new Class[] {BossBattle.getInstanceClass()}, new Object[] {BossBattle.getInstance()}));
            } else {
                EzEnum Action = new EzEnum(Ref.getNmsClass("PacketPlayOutBoss$Action"));
                Action.newInstance("UPDATE_STYLE");
                PacketPlayOutBoss.setConstructor(Action.getInstanceEnum(), BossBattle.getInstanceClass());
                PacketPlayOutBoss.newInstance(Action.getInstance(), BossBattle.getInstance());
            }
            PlayerUtils.sendPacket(player, PacketPlayOutBoss.getInstance());
        }
    }

    @Override
    public ChatMessage getTitle() {
        if (isDropped()) return null;
        return title;
    }

    @Override
    public void setTitle(ChatMessage title) {
        if (isDropped()) return;
        this.title = title;
        if (getViewers().size() == 0) return;
        for (Player player : getViewers()) {
            if (!hasShown.contains(player)) continue;
            EzClass PacketPlayOutBoss = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutBoss", "PacketPlayOutBoss"));
            EzClass BossBattle = new EzClass(Ref.getNmsOrOld("world.BossBattle", "BossBattle"));
            EzClass IChatBaseComponent = new EzClass(Ref.getNmsOrOld("network.chat.IChatBaseComponent", "IChatBaseComponent"));
            EzClass ChatMessage = new EzClass(Ref.getNmsOrOld("network.chat.ChatMessage", "ChatMessage"));

            ChatMessage.setConstructor(String.class);
            ChatMessage.newInstance(this.title.getText(player));

            BossBattle.setInstance(viewers.get(player).getInstance());

            BossBattle.invokeMethod("a", new Class[] {IChatBaseComponent.getInstanceClass()}, new Object[] {ChatMessage.getInstance()});

            if (Ref.getVersion() >= 16) {
                PacketPlayOutBoss.setInstance(PacketPlayOutBoss.invokeStaticMethod("createUpdateNamePacket", new Class[] {BossBattle.getInstanceClass()}, new Object[] {BossBattle.getInstance()}));
            } else {
                EzEnum Action = new EzEnum(Ref.getNmsClass("PacketPlayOutBoss$Action"));
                Action.newInstance("UPDATE_NAME");
                PacketPlayOutBoss.setConstructor(Action.getInstanceEnum(), BossBattle.getInstanceClass());
                PacketPlayOutBoss.newInstance(Action.getInstance(), BossBattle.getInstance());
            }
            PlayerUtils.sendPacket(player, PacketPlayOutBoss.getInstance());
        }
    }

    @Override
    public void addViewer(Player player) {
        if (isDropped()) return;
        if (!viewers.containsKey(player)) {
            EzClass MinecraftKey = new EzClass(Ref.getNmsOrOld("resources.MinecraftKey", "MinecraftKey"));
            EzClass BossBattleCustom = new EzClass(Ref.getNmsOrOld("world.BossBattleCustom", "BossBattleCustom"));
            EzClass BossBattle = new EzClass(Ref.getNmsOrOld("world.BossBattle", "BossBattle"));
            EzClass IChatBaseComponent = new EzClass(Ref.getNmsOrOld("network.chat.IChatBaseComponent", "IChatBaseComponent"));
            EzClass ChatMessage = new EzClass(Ref.getNmsOrOld("network.chat.ChatMessage", "ChatMessage"));

            EzEnum BarColor = this.color.getNms();
            EzEnum BarStyle = this.style.getNms();

            ChatMessage.setConstructor(String.class);
            ChatMessage.newInstance(this.title.getText(player));

            MinecraftKey.setConstructor(String.class, String.class);
            String date = DateUtils.now().replace(" ", "-").replace(":", "-");
            MinecraftKey.newInstance(player.getName().toLowerCase(), "ez" + date.substring(0, Math.min(13, date.length())));

            BossBattleCustom.setConstructor(MinecraftKey.getInstanceClass(), IChatBaseComponent.getInstanceClass());
            BossBattleCustom.newInstance(MinecraftKey.getInstance(), ChatMessage.getInstance());

            BossBattle.setInstance(BossBattleCustom.getInstance());

            if (Ref.getVersion() >= 16) {
                BossBattle.invokeMethod("setProgress", new Class[] {float.class}, new Object[] {progress});
                BossBattle.invokeMethod("setDarkenSky", new Class[] {boolean.class}, new Object[] {darkenSky});
                BossBattle.invokeMethod("setPlayMusic", new Class[] {boolean.class}, new Object[] {playMusic});
                BossBattle.invokeMethod("setCreateFog", new Class[] {boolean.class}, new Object[] {createFog});
            } else {
                BossBattle.invokeMethod("a", new Class[] {float.class}, new Object[] {progress});
                BossBattle.invokeMethod("a", new Class[] {boolean.class}, new Object[] {darkenSky});
                BossBattle.invokeMethod("b", new Class[] {boolean.class}, new Object[] {playMusic});
                BossBattle.invokeMethod("c", new Class[] {boolean.class}, new Object[] {createFog});
            }
            BossBattle.invokeMethod("a", new Class[] {BarColor.getInstanceEnum()}, new Object[] {BarColor.getInstance()});
            BossBattle.invokeMethod("a", new Class[] {BarStyle.getInstanceEnum()}, new Object[] {BarStyle.getInstance()});

            viewers.put(player, BossBattleCustom);

            show(player);
        }

    }

    @Override
    public void removeViewer(Player player) {
        if (isDropped()) return;
        if (viewers.containsKey(player)) {
            if (hasShown.contains(player)) {
                hide(player);
            }
            viewers.remove(player);
        }
    }

    @Override
    public void show(Player player) {
        if (isDropped()) return;
        if (!viewers.containsKey(player)) return;
        if (hasShown.contains(player)) return;
        add(player, true);
        hasShown.add(player);
    }

    @Override
    public void hide(Player player) {
        if (isDropped()) return;
        if (!viewers.containsKey(player)) return;
        if (!hasShown.contains(player)) return;
        add(player, false);
        hasShown.remove(player);
    }

    private void add(Player player, boolean show) {
        if (isDropped()) return;
        if (!viewers.containsKey(player)) return;
        if (viewers.containsKey(player)) {
            if ((hasShown.contains(player) && !show) || (!hasShown.contains(player) && show)) {
                EzClass PacketPlayOutBoss = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutBoss", "PacketPlayOutBoss"));
                EzClass BossBattle = new EzClass(Ref.getNmsOrOld("world.BossBattle", "BossBattle"));
                BossBattle.setInstance(viewers.get(player).getInstance());
                if (Ref.getVersion() >= 16) {
                    PacketPlayOutBoss.setInstance(PacketPlayOutBoss.invokeStaticMethod(show ? "createAddPacket" : "createRemovePacket", new Class[]{BossBattle.getInstanceClass()}, new Object[]{BossBattle.getInstance()}));
                } else {
                    EzEnum Action = new EzEnum(Ref.getNmsClass("PacketPlayOutBoss$Action"));
                    Action.newInstance(show ? "ADD" : "REMOVE");
                    PacketPlayOutBoss.setConstructor(Action.getInstanceEnum(), BossBattle.getInstanceClass());
                    PacketPlayOutBoss.newInstance(Action.getInstance(), BossBattle.getInstance());
                }
                PlayerUtils.sendPacket(player, PacketPlayOutBoss.getInstance());
            }
        }
    }

    @Override
    public List<Player> getViewers() {
        if (isDropped()) return new ArrayList<>();
        return new ArrayList<>(viewers.keySet());
    }

    @Override
    public void removeAll() {
        if (isDropped()) return;
        if (getViewers().size() == 0) return;
        for (Player player : getViewers()) {
            removeViewer(player);
        }
    }

    @Override
    public void drop() {
        if (!dropped) {
            removeAll();
            dropped = true;
        }
    }

    @Override
    public boolean isDropped() {
        return dropped;
    }

}

package org.ezapi.module.scoreboard;

import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import org.ezapi.chat.ChatMessage;
import org.ezapi.reflect.EzClass;
import org.ezapi.reflect.EzEnum;
import org.ezapi.util.ColorUtils;
import org.ezapi.util.PlayerUtils;
import org.ezapi.util.Ref;
import org.ezapi.util.StringUtils;

import java.util.*;

public final class EzScoreboard implements Scoreboard {

    private static int nextInt = 0;

    private ChatMessage title;

    private boolean dropped = false;

    private final Map<Player, List<EzClass>> viewers = new HashMap<>();

    private final Map<Integer,ChatMessage> texts = new HashMap<>();

    public EzScoreboard(ChatMessage title) {
        this.title = title;
    }

    public static int getNextInt() {
        int i = nextInt;
        nextInt++;
        return i;
    }

    public ChatMessage getText(int line) {
        if (isDropped()) return ChatMessage.NULL;
        return texts.getOrDefault(line, ChatMessage.NULL);
    }

    private boolean check(int newLine, ChatMessage text) {
        boolean contains = false;
        for (int i = 0; i < texts.size(); i++) {
            if (!texts.containsKey(i)) continue;
            ChatMessage message = texts.get(i);
            if (message.equals(text)) {
                if (i != newLine) {
                    texts.remove(i);
                    texts.put(newLine, text);
                    return true;
                }
                contains = true;
            }
        }
        return !contains;
    }

    public void setText(int line, ChatMessage text) {
        if (isDropped()) return;
        if (!check(line, text)) return;
        if (texts.containsKey(line)) {
            removeText(line);
        }
        for (Player player : getViewers()) {
            String id = (String) this.viewers.get(player).get(0).getInstance();
            EzClass PacketPlayOutScoreboardScore = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutScoreboardScore", "PacketPlayOutScoreboardScore"));
            EzEnum Action = new EzEnum(Ref.getNmsOrOld("server.ScoreboardServer$Action", "ScoreboardServer$Action"));
            if (Ref.getVersion() >= 16) {
                Action.newInstance("a");
            } else {
                Action.newInstance("CHANGE");
            }
            PacketPlayOutScoreboardScore.setConstructor(Action.getInstanceEnum(), String.class, String.class, int.class);
            PacketPlayOutScoreboardScore.newInstance(Action.getInstance(), id, text.getText(player).substring(0, 40), line);
            PlayerUtils.sendPacket(player, PacketPlayOutScoreboardScore.getInstance());
        }
        this.texts.put(line, text);
    }

    public void removeText(int line) {
        if (isDropped()) return;
        if (!texts.containsKey(line)) return;
        for (Player player : getViewers()) {
            String id = (String) this.viewers.get(player).get(0).getInstance();
            EzClass PacketPlayOutScoreboardScore = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutScoreboardScore", "PacketPlayOutScoreboardScore"));
            EzEnum Action = new EzEnum(Ref.getNmsOrOld("server.ScoreboardServer$Action", "ScoreboardServer$Action"));
            if (Ref.getVersion() >= 16) {
                Action.newInstance("b");
            } else {
                Action.newInstance("REMOVE");
            }
            PacketPlayOutScoreboardScore.setConstructor(Action.getInstanceEnum(), String.class, String.class, int.class);
            PacketPlayOutScoreboardScore.newInstance(Action.getInstance(), id, texts.get(line).getText(player).substring(0, 40), line);
            PlayerUtils.sendPacket(player, PacketPlayOutScoreboardScore.getInstance());
        }
        this.texts.remove(line);
    }

    private void reload() {
        List<Player> players = new ArrayList<>(getViewers());
        for (Player player : players) {
            removeAll();
            addViewer(player);
        }
    }

    @Override
    public ChatMessage getTitle() {
        if (isDropped()) return ChatMessage.NULL;
        return title;
    }

    @Override
    public void setTitle(ChatMessage title) {
        if (isDropped()) return;
        this.title = title;
        for (Player player : getViewers()) {
            EzClass PacketPlayOutScoreboardObjective = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutScoreboardObjective", "PacketPlayOutScoreboardObjective"));
            EzClass ScoreboardObjective = viewers.get(player).get(2);
            EzClass IChatBaseComponent = new EzClass(Ref.getNmsOrOld("network.chat.IChatBaseComponent", "IChatBaseComponent"));
            EzClass ChatMessage = new EzClass(Ref.getNmsOrOld("network.chat.ChatMessage", "ChatMessage"));
            ChatMessage.setConstructor(String.class);
            ChatMessage.newInstance(this.title.getText(player).substring(0, 40));
            ScoreboardObjective.invokeMethod("setDisplayName", new Class[] {IChatBaseComponent.getInstanceClass()}, new Object[] {ChatMessage.getInstance()});
            PacketPlayOutScoreboardObjective.setConstructor(ScoreboardObjective.getInstanceClass(), int.class);
            PacketPlayOutScoreboardObjective.newInstance(ScoreboardObjective.getInstance(), 2);
            PlayerUtils.sendPacket(player, PacketPlayOutScoreboardObjective.getInstance());
        }
    }

    @Override
    public void addViewer(Player player) {
        if (isDropped()) return;
        if (!viewers.containsKey(player)) {
            String id = player.getName().toLowerCase().substring(0, Math.min(10, player.getName().length())) + getNextInt();
            EzClass Scoreboard = new EzClass(Ref.getNmsOrOld("world.scores.Scoreboard", "Scoreboard"));
            EzClass ScoreboardObjective = new EzClass(Ref.getNmsOrOld("world.scores.ScoreboardObjective", "ScoreboardObjective"));
            EzClass IScoreboardCriteria = new EzClass(Ref.getNmsOrOld("world.scores.criteria.IScoreboardCriteria", "IScoreboardCriteria"));
            EzEnum EnumScoreboardHealthDisplay = new EzEnum(Ref.getNmsOrOld("world.scores.criteria.IScoreboardCriteria$EnumScoreboardHealthDisplay", "IScoreboardCriteria$EnumScoreboardHealthDisplay"));
            EzClass IChatBaseComponent = new EzClass(Ref.getNmsOrOld("network.chat.IChatBaseComponent", "IChatBaseComponent"));
            EzClass ChatMessage = new EzClass(Ref.getNmsOrOld("network.chat.ChatMessage", "ChatMessage"));
            EzClass ScoreboardTeam = new EzClass(Ref.getNmsOrOld("world.scores.ScoreboardTeam", "ScoreboardTeam"));
            ChatMessage.setConstructor(String.class);
            ChatMessage.newInstance(this.title.getText(player).substring(0, 40));
            if (Ref.getVersion() == 9 || Ref.getVersion() == 10) {
                IScoreboardCriteria.setInstance(IScoreboardCriteria.getStaticField("b"));
                EnumScoreboardHealthDisplay.newInstance("INTEGER");
            } else if (Ref.getVersion() >= 11 && Ref.getVersion() <= 15) {
                IScoreboardCriteria.setInstance(IScoreboardCriteria.getStaticField("DUMMY"));
                EnumScoreboardHealthDisplay.newInstance("INTEGER");
            } else if (Ref.getVersion() >= 16) {
                IScoreboardCriteria.setInstance(IScoreboardCriteria.getStaticField("a"));
                EnumScoreboardHealthDisplay.newInstance("a");
            } else {
                return;
            }
            Scoreboard.newInstance();
            ScoreboardObjective.setInstance(Scoreboard.invokeMethod("registerObjective", new Class[] {String.class, IScoreboardCriteria.getInstanceClass(), IChatBaseComponent.getInstanceClass(), EnumScoreboardHealthDisplay.getInstanceEnum()}, new Object[] {id, IScoreboardCriteria.getInstance(), ChatMessage.getInstance(), EnumScoreboardHealthDisplay.getInstance()}));

            ScoreboardTeam.setConstructor(Scoreboard.getInstanceClass(), String.class);
            ScoreboardTeam.newInstance(Scoreboard.getInstance(), id);

            EzClass PacketPlayOutScoreboardObjective = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutScoreboardObjective", "PacketPlayOutScoreboardObjective"));
            PacketPlayOutScoreboardObjective.setConstructor(ScoreboardObjective.getInstanceClass(), int.class);
            PacketPlayOutScoreboardObjective.newInstance(ScoreboardObjective.getInstance(), 0);

            EzClass PacketPlayOutScoreboardDisplayObjective = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutScoreboardDisplayObjective", "PacketPlayOutScoreboardDisplayObjective"));
            PacketPlayOutScoreboardDisplayObjective.setConstructor(int.class, ScoreboardObjective.getInstanceClass());
            PacketPlayOutScoreboardDisplayObjective.newInstance(1, ScoreboardObjective.getInstance());

            EzClass PacketPlayOutScoreboardTeam = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutScoreboardTeam", "PacketPlayOutScoreboardTeam"));
            if (Ref.getVersion() >= 16) {
                PacketPlayOutScoreboardTeam.setConstructor(String.class, int.class, Optional.class, Collection.class);
                PacketPlayOutScoreboardTeam.newInstance(id, 0, Optional.empty(), ImmutableList.of());
            } else {
                PacketPlayOutScoreboardTeam.setConstructor(ScoreboardTeam.getInstanceClass(), int.class);
                PacketPlayOutScoreboardTeam.newInstance(ScoreboardTeam.getInstance(), 0);
            }

            PlayerUtils.sendPacket(player, PacketPlayOutScoreboardObjective.getInstance());
            PlayerUtils.sendPacket(player, PacketPlayOutScoreboardDisplayObjective.getInstance());
            PlayerUtils.sendPacket(player, PacketPlayOutScoreboardTeam.getInstance());

            for (int line : this.texts.keySet()) {
                EzClass PacketPlayOutScoreboardScore = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutScoreboardScore", "PacketPlayOutScoreboardScore"));
                EzEnum Action = new EzEnum(Ref.getNmsOrOld("server.ScoreboardServer$Action", "ScoreboardServer$Action"));
                if (Ref.getVersion() >= 16) {
                    Action.newInstance("a");
                } else {
                    Action.newInstance("CHANGE");
                }
                ChatMessage text = texts.get(line);
                PacketPlayOutScoreboardScore.setConstructor(Action.getInstanceEnum(), String.class, String.class, int.class);
                PacketPlayOutScoreboardScore.newInstance(Action.getInstance(), id, text.getText(player), line);
                PlayerUtils.sendPacket(player, PacketPlayOutScoreboardScore.getInstance());
            }

            List<EzClass> list = new ArrayList<>();
            EzClass idClass = new EzClass(String.class);
            idClass.setInstance(id);
            list.add(idClass);
            list.add(Scoreboard);
            list.add(ScoreboardObjective);
            list.add(ScoreboardTeam);
            viewers.put(player, list);
        }
    }

    @Override
    public void removeViewer(Player player) {
        if (isDropped()) return;
        if (viewers.containsKey(player)) {
            String id = (String) viewers.get(player).get(0).getInstance();
            EzClass ScoreboardObjective = viewers.get(player).get(2);
            EzClass ScoreboardTeam = viewers.get(player).get(3);

            EzClass PacketPlayOutScoreboardTeam = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutScoreboardTeam", "PacketPlayOutScoreboardTeam"));
            if (Ref.getVersion() >= 16) {
                PacketPlayOutScoreboardTeam.setConstructor(String.class, int.class, Optional.class, Collection.class);
                PacketPlayOutScoreboardTeam.newInstance(id, 1, Optional.empty(), ImmutableList.of());
            } else {
                PacketPlayOutScoreboardTeam.setConstructor(ScoreboardTeam.getInstanceClass(), int.class);
                PacketPlayOutScoreboardTeam.newInstance(ScoreboardObjective.getInstance(), 1);
            }

            EzClass PacketPlayOutScoreboardObjective = new EzClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutScoreboardObjective", "PacketPlayOutScoreboardObjective"));
            PacketPlayOutScoreboardObjective.setConstructor(ScoreboardObjective.getInstanceClass(), int.class);
            PacketPlayOutScoreboardObjective.newInstance(ScoreboardObjective.getInstance(), 1);

            PlayerUtils.sendPacket(player, PacketPlayOutScoreboardTeam.getInstance());

            PlayerUtils.sendPacket(player, PacketPlayOutScoreboardObjective.getInstance());
            viewers.remove(player);
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

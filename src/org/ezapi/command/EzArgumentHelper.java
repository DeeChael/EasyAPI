package org.ezapi.command;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.context.CommandContext;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.ezapi.command.argument.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class EzArgumentHelper {

    private final CommandContext<Object> commandContext;

    protected EzArgumentHelper(CommandContext<Object> commandContext) {
        this.commandContext = commandContext;
    }

    public String getAsString(String argumentName) {
        return StringArgumentType.getString(commandContext, argumentName);
    }

    public Integer getAsInteger(String argumentName) {
        return IntegerArgumentType.getInteger(commandContext, argumentName);
    }

    public Boolean getAsBoolean(String argumentName) {
        return BoolArgumentType.getBool(commandContext, argumentName);
    }

    public Double getAsDouble(String argumentName) {
        return DoubleArgumentType.getDouble(commandContext, argumentName);
    }

    public Long getAsLong(String argumentName) {
        return LongArgumentType.getLong(commandContext, argumentName);
    }

    public Float getAsFloat(String argumentName) {
        return FloatArgumentType.getFloat(commandContext, argumentName);
    }

    public List<Player> getAsPlayers(String argumentName) {
        List<Player> list = new ArrayList<>();
        Collection<Object> collection = (Collection<Object>) ArgumentPlayer.instance().get(commandContext, argumentName);
        if (collection != null) {
            for (Object object : collection) {
                Player player = (Player) ArgumentEntities.nmsEntityToBukkitEntity(object);
                if (player != null) list.add(player);
            }
        }
        return list;
    }

    public Location getAsLocation(String argumentName) {
        EzSender ezSender = new EzSender(commandContext.getSource());
        if (ezSender.isPlayer()) {
            return ArgumentLocation.vec3DToLocation(ezSender.player().getWorld(), ArgumentLocation.instance().get(commandContext, argumentName));
        } else {
            return ArgumentLocation.vec3DToLocation(ArgumentLocation.instance().get(commandContext, argumentName));
        }

    }

    public World getAsWorld(String argumentName) {
        return ArgumentWorld.worldServerToBukkitWorld(ArgumentWorld.instance().get(commandContext, argumentName));
    }

    public Enchantment getAsEnchantment(String argumentName) {
        return ArgumentEnchantment.nmsEnchantmentToBukkitEnchantment(ArgumentEnchantment.instance().get(commandContext, argumentName));
    }

    public String getAsChatMessage(String argumentName) {
        return ArgumentChat.chatToString(ArgumentChat.instance().get(commandContext, argumentName));
    }

    public Location getAsBlockLocation(String argumentName) {
        EzSender ezSender = new EzSender(commandContext.getSource());
        if (ezSender.isPlayer()) {
            return ArgumentBlockLocation.blockPositionToLocation(ezSender.player().getWorld(), ArgumentBlockLocation.instance().get(commandContext, argumentName));
        } else {
            return ArgumentBlockLocation.blockPositionToLocation(ArgumentBlockLocation.instance().get(commandContext, argumentName));
        }
    }

    public ItemStack getAsItemStack(String argumentName, int amount) {
        return ArgumentItemStack.nmsItemStackToBukkitItemStack(ArgumentItemStack.instance().get(commandContext, argumentName), amount);
    }

    public Material getAsBlock(String argumentName) {
        return ArgumentBlock.nmsBlockToMaterial(ArgumentBlock.instance().get(commandContext, argumentName));
    }

    public List<Entity> getAsEntities(String argumentName) {
        List<Entity> list = new ArrayList<>();
        Collection<Object> collection = (Collection<Object>) ArgumentPlayer.instance().get(commandContext, argumentName);
        if (collection != null) {
            for (Object object : collection) {
                Entity entity = ArgumentEntities.nmsEntityToBukkitEntity(object);
                if (entity != null) list.add(entity);
            }
        }

        return list;
    }

    public Entity getAsEntity(String argumentName) {
        return ArgumentEntities.nmsEntityToBukkitEntity(ArgumentEntity.instance().get(commandContext, argumentName));
    }

    public List<OfflinePlayer> getAsOfflinePlayer(String argumentName) {
        List<OfflinePlayer> list = new ArrayList<>();
        for (GameProfile gameProfile : ArgumentOfflinePlayer.instance().get(commandContext, argumentName)) {
            list.add(Bukkit.getOfflinePlayer(gameProfile.getId()));
        }
        return list;
    }

    public Attribute getAsAttribute(String argumentName) {
        return ArgumentAttribute.nmsAttributeBaseToBukkitAttribute(ArgumentAttribute.instance().get(commandContext, argumentName));
    }

    public PotionEffectType getAsPotionEffectType(String argumentName) {
        return ArgumentPotionEffectType.mobEffectListToPotionEffectType(ArgumentPotionEffectType.instance().get(commandContext, argumentName));
    }

    public EntityType getAsEntityType(String argumentName) {
        return ArgumentEntityType.nmsEntityTypesToBukkitEntityType(ArgumentEntityType.instance().get(commandContext, argumentName));
    }

    public Particle getAsParticle(String argumentName) {
        return ArgumentParticle.nmsParticleToBukkitParticle(ArgumentParticle.instance().get(commandContext, argumentName));
    }

    public ChatColor getAsChatColor_Bungee(String argumentName) {
        return ChatColor.getByChar(ArgumentChatColor.getColorChar(ArgumentChatColor.instance().get(commandContext, argumentName)));
    }

    public org.bukkit.ChatColor getAsChatColor_Bukkit(String argumentName) {
        return org.bukkit.ChatColor.getByChar(ArgumentChatColor.getColorChar(ArgumentChatColor.instance().get(commandContext, argumentName)));
    }

    public UUID getAsUUID(String argumentName) {
        return ArgumentUUID.instance().get(commandContext, argumentName);
    }

    public JsonObject getAsNBTTag(String argumentName) {
        return ArgumentNBTTag.instance().get(commandContext, argumentName);
    }

}

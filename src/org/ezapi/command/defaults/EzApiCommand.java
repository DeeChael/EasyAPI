package org.ezapi.command.defaults;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.ezapi.chat.ChatMessage;
import org.ezapi.command.EzArgument;
import org.ezapi.command.EzCommand;
import org.ezapi.command.EzCommandManager;
import org.ezapi.command.EzCommandRegistered;
import org.ezapi.command.argument.*;
import org.ezapi.util.BuildingUtils;
import org.ezapi.util.LocationUtils;
import org.ezapi.util.PlayerUtils;

final class EzApiCommand {

    private EzCommand ezCommand;

    private boolean registered = false;

    private EzApiCommand() {
        this.ezCommand = new EzCommand("ez-api", 4, "ez-api.command.ez-api", PermissionDefault.OP);
        ezCommand.then(new EzCommand("reload", 4, "ez-api.command.ez-api.reload", PermissionDefault.OP)
                .executes(((sender, argument) -> {
                    sender.sendMessage(new ChatMessage("ez-api.message.command.ez-api.reload.success", true));
                    return 1;
                }))
        );
        ezCommand.then(new EzCommand("test", 4, "ez-api.command.ez-api.test", PermissionDefault.OP)
                .then(new EzCommand("api")
                        .executes(((sender, argument) -> {
                            int i = 0;
                            if (sender.player() != null) {
                                PlayerUtils.skin(sender.player(), "Notch");
                                i++;
                            }
                            return i;
                        }))
                        .then(new EzArgument(BaseArguments.string(), "name")
                                .executes(((sender, argument) -> {
                                    int i = 0;
                                    if (sender.player() != null) {
                                        PlayerUtils.skin(sender.player(), argument.getAsString("name"));
                                        i++;
                                    }
                                    return i;
                                }))
                        )
                )
                .then(new EzCommand("give")
                        .then(new EzArgument(ArgumentPlayer.argumentType(), "targets")
                                .then(new EzArgument(ArgumentItemStack.argumentType(), "item")
                                        .executes((sender, argument) -> {
                                            int i = 0;
                                            for (Player player : argument.getAsPlayers("targets")) {
                                                player.getInventory().addItem(argument.getAsItemStack("item", 1));
                                                i++;
                                            }
                                            return i;
                                        })
                                        .then(new EzArgument(BaseArguments.integer(1, 64), "amount")
                                                .executes((sender, argument) -> {
                                                    int i = 0;
                                                    for (Player player : argument.getAsPlayers("targets")) {
                                                        player.getInventory().addItem(argument.getAsItemStack("item", argument.getAsInteger("amount")));
                                                        i++;
                                                    }
                                                    return i;
                                                })
                                        )
                                )
                        )
                )
                .then(new EzCommand("fill")
                        .then(new EzArgument(ArgumentBlockLocation.argumentType(), "from")
                                .then(new EzArgument(ArgumentBlockLocation.argumentType(), "to")
                                        .then(new EzArgument(ArgumentBlock.argumentType(), "block")
                                                .executes((sender, argument) -> {
                                                    int i = 0;
                                                    Player player = sender.player();
                                                    if (player != null) {
                                                        Location from = argument.getAsBlockLocation("from");
                                                        Location to = argument.getAsBlockLocation("to");
                                                        Material block = argument.getAsBlock("block");
                                                        BuildingUtils.fill(from, to, block);
                                                    }
                                                    return i;
                                                })
                                                .then(new EzCommand("fill")
                                                        .executes((sender, argument) -> {
                                                            int i = 0;
                                                            Player player = sender.player();
                                                            if (player != null) {
                                                                Location from = argument.getAsBlockLocation("from");
                                                                Location to = argument.getAsBlockLocation("to");
                                                                Material block = argument.getAsBlock("block");
                                                                BuildingUtils.fill(from, to, block);
                                                            }
                                                            return i;
                                                        })
                                                )
                                                .then(new EzCommand("hollow")
                                                        .executes((sender, argument) -> {
                                                            int i = 0;
                                                            Player player = sender.player();
                                                            if (player != null) {
                                                                Location from = argument.getAsBlockLocation("from");
                                                                Location to = argument.getAsBlockLocation("to");
                                                                Material block = argument.getAsBlock("block");
                                                                BuildingUtils.hollow(from, to, block);
                                                            }
                                                            return i;
                                                        })
                                                )
                                                .then(new EzCommand("outline")
                                                        .executes((sender, argument) -> {
                                                            int i = 0;
                                                            Player player = sender.player();
                                                            if (player != null) {
                                                                Location from = argument.getAsBlockLocation("from");
                                                                Location to = argument.getAsBlockLocation("to");
                                                                Material block = argument.getAsBlock("block");
                                                                BuildingUtils.outline(from, to, block);
                                                            }
                                                            return i;
                                                        })
                                                )
                                                .then(new EzCommand("line")
                                                        .executes((sender, argument) -> {
                                                            int i = 0;
                                                            Player player = sender.player();
                                                            if (player != null) {
                                                                Location from = argument.getAsBlockLocation("from");
                                                                Location to = argument.getAsBlockLocation("to");
                                                                Material block = argument.getAsBlock("block");
                                                                BuildingUtils.drawALine(from, to, 0, block, true);
                                                            }
                                                            return i;
                                                        })
                                                        .then(new EzArgument(BaseArguments.doubleArg(0.0), "radius")
                                                                .executes((sender, argument) -> {
                                                                    int i = 0;
                                                                    Player player = sender.player();
                                                                    if (player != null) {
                                                                        Location from = argument.getAsBlockLocation("from");
                                                                        Location to = argument.getAsBlockLocation("to");
                                                                        Material block = argument.getAsBlock("block");
                                                                        BuildingUtils.drawALine(from, to, argument.getAsDouble("radius"), block, true);
                                                                    }
                                                                    return i;
                                                                }).then(new EzArgument(BaseArguments.bool(), "filled")
                                                                        .executes((sender, argument) -> {
                                                                            int i = 0;
                                                                            Player player = sender.player();
                                                                            if (player != null) {
                                                                                Location from = argument.getAsBlockLocation("from");
                                                                                Location to = argument.getAsBlockLocation("to");
                                                                                Material block = argument.getAsBlock("block");
                                                                                BuildingUtils.drawALine(from, to, argument.getAsDouble("radius"), block, argument.getAsBoolean("filled"));
                                                                            }
                                                                            return i;
                                                                        })
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .then(new EzCommand("summon")
                        .then(new EzArgument(ArgumentEntityType.argumentType(), "entity")
                                .executes((sender, argument) -> {
                                    int i = 0;
                                    Player player = sender.player();
                                    if (player != null) {
                                        Location location = player.getLocation();
                                        LocationUtils.summonEntity(location, argument.getAsEntityType("entity"));
                                        i++;
                                    }
                                    return i;
                                })
                                .then(new EzArgument(ArgumentLocation.argumentType(), "position")
                                        .executes((sender, argument) -> {
                                            int i = 0;
                                            Player player = sender.player();
                                            if (player != null) {
                                                Location location = player.getLocation();
                                                LocationUtils.summonEntity(argument.getAsLocation("position"), argument.getAsEntityType("entity"));
                                                i++;
                                            }
                                            return i;
                                        })
                                )
                        )
                )
                .then(new EzCommand("effect")
                        .then(new EzArgument(ArgumentEntities.argumentType(), "targets")
                                .then(new EzCommand("give")
                                        .then(new EzArgument(ArgumentPotionEffectType.argumentType(), "effect")
                                                .executes((sender, argument) -> {
                                                    int i = 0;
                                                    for (Entity entity : argument.getAsEntities("targets")) {
                                                        PotionEffectType potionEffectType = argument.getAsPotionEffectType("effect");
                                                        if (entity instanceof LivingEntity) {
                                                            LivingEntity livingEntity = (LivingEntity) entity;
                                                            livingEntity.addPotionEffect(new PotionEffect(potionEffectType, 30, 0));
                                                            i++;
                                                        }
                                                    }
                                                    return i;
                                                })
                                                .then(new EzArgument(BaseArguments.integer(0 , 1000000), "time")
                                                        .executes((sender, argument) -> {
                                                            int i = 0;
                                                            for (Entity entity : argument.getAsEntities("targets")) {
                                                                PotionEffectType potionEffectType = argument.getAsPotionEffectType("effect");
                                                                if (entity instanceof LivingEntity) {
                                                                    LivingEntity livingEntity = (LivingEntity) entity;
                                                                    livingEntity.addPotionEffect(new PotionEffect(potionEffectType, argument.getAsInteger("time"), 0));
                                                                    i++;
                                                                }
                                                            }
                                                            return i;
                                                        }).then(new EzArgument(BaseArguments.integer(0 , 255), "level")
                                                                .executes((sender, argument) -> {
                                                                    int i = 0;
                                                                    for (Entity entity : argument.getAsEntities("targets")) {
                                                                        PotionEffectType potionEffectType = argument.getAsPotionEffectType("effect");
                                                                        if (entity instanceof LivingEntity) {
                                                                            LivingEntity livingEntity = (LivingEntity) entity;
                                                                            livingEntity.addPotionEffect(new PotionEffect(potionEffectType, argument.getAsInteger("time"), argument.getAsInteger("level")));
                                                                            i++;
                                                                        }
                                                                    }
                                                                    return i;
                                                                })
                                                                .then(new EzArgument(BaseArguments.bool(), "particles")
                                                                        .executes((sender, argument) -> {
                                                                            int i = 0;
                                                                            for (Entity entity : argument.getAsEntities("targets")) {
                                                                                PotionEffectType potionEffectType = argument.getAsPotionEffectType("effect");
                                                                                if (entity instanceof LivingEntity) {
                                                                                    LivingEntity livingEntity = (LivingEntity) entity;
                                                                                    livingEntity.addPotionEffect(new PotionEffect(potionEffectType, argument.getAsInteger("time"), argument.getAsInteger("level"), true, argument.getAsBoolean("particles")));
                                                                                    i++;
                                                                                }
                                                                            }
                                                                            return i;
                                                                        })
                                                                )
                                                        )
                                                )
                                        )
                                )
                                .then(new EzCommand("clear")
                                        .executes((sender, argument) -> {
                                            int i = 0;
                                            for (Entity entity : argument.getAsEntities("targets")) {
                                                if (entity instanceof LivingEntity) {
                                                    LivingEntity livingEntity = (LivingEntity) entity;
                                                    for (PotionEffect potionEffect : livingEntity.getActivePotionEffects()) {
                                                        livingEntity.removePotionEffect(potionEffect.getType());
                                                    }
                                                    i++;
                                                }
                                            }
                                            return i;
                                        })
                                        .then(new EzArgument(ArgumentPotionEffectType.argumentType(), "effect")
                                                .executes((sender, argument) -> {
                                                    int i = 0;
                                                    for (Entity entity : argument.getAsEntities("targets")) {
                                                        PotionEffectType potionEffectType = argument.getAsPotionEffectType("effect");
                                                        if (entity instanceof LivingEntity) {
                                                            LivingEntity livingEntity = (LivingEntity) entity;
                                                            livingEntity.removePotionEffect(potionEffectType);
                                                            i++;
                                                        }
                                                    }
                                                    return i;
                                                })
                                        )
                                )
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

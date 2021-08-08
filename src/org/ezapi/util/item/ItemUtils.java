package org.ezapi.util.item;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Map.Entry;

public final class ItemUtils {

    public static ItemStack asBukkitCopy(Object nmsItemStack) {
        try {
            return Reflection_Class.asBukkitCopy(nmsItemStack);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return new ItemStack(Material.AIR);
    }

    public static Object asNMSCopy(ItemStack bukkitItemStack) {
        try {
            return Reflection_Class.asNMSCopy(bukkitItemStack);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    public static void spawnUnpickItem(ItemStack itemStack, Location location) {
        if (Ref.getVersion() < 9) return;
        spawnUnpickItem(itemStack, location, Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    public static void spawnUnpickItem(ItemStack itemStack, Location location, Player... whoCanSee) {
        if (Ref.getVersion() < 9) return;
        if (whoCanSee.length > 0) {
            Object entityItem = Reflection_Class.createEntityItem(itemStack, location);
            Reflection_Class.setPickUpDelayTo32767(entityItem);
            int id = Reflection_Class.getId(entityItem);
            Object packetPlayOutSpawnEntity = Reflection_Class.createPacketPlayOutSpawnEntity(entityItem, 2);
            Object packetPlayOutEntityMetadata = Reflection_Class.createPacketPlayOutEntityMetadata(id, Reflection_Class.getDataWatcher(entityItem), true);
            for (Player player : whoCanSee) {
                Reflection_Class.sendPacket(player, packetPlayOutSpawnEntity);
                Reflection_Class.sendPacket(player, packetPlayOutEntityMetadata);
            }
        }
    }

    public static void spawnPickItemOnlySee(ItemStack itemStack, Location location, Player player) {
        if (Ref.getVersion() < 9) return;
        Item item = location.getWorld().dropItem(location, itemStack);
        Object entityItem = Reflection_Class.getEntityItem(item);
        int id = Reflection_Class.getId(entityItem);
        Reflection_Class.setOwner(entityItem, player);
        Object packet = Reflection_Class.createPacketPlayOutEntityDestroy(id);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != player) {
                Reflection_Class.sendPacket(p, packet);
            }
        }
    }

    public static void spawnPickItem(ItemStack itemStack, Location location, Player player) {
        if (Ref.getVersion() < 9) return;
        Item item = location.getWorld().dropItem(location, itemStack);
        Object entityItem = Reflection_Class.getEntityItem(item);
        Reflection_Class.setOwner(entityItem, player);
    }

    public static void spawnPickItem(ItemStack itemStack, Location location, Player whoCanPickUp, Player... whoCanSee) {
        if (Ref.getVersion() < 9) return;
        Item item = location.getWorld().dropItem(location, itemStack);
        Object entityItem = Reflection_Class.getEntityItem(item);
        int id = Reflection_Class.getId(entityItem);
        Reflection_Class.setOwner(entityItem, whoCanPickUp);
        Object packet = Reflection_Class.createPacketPlayOutEntityDestroy(id);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!ArrayUtils.contains(whoCanSee, p)) {
                if (p != whoCanPickUp) {
                    Reflection_Class.sendPacket(p, packet);
                }
            }
        }
    }

    public static ItemStack newItemStack(Material type, String displayName) {
        ItemStack itemStack = new ItemStack(type);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(ColorUtils.translate(displayName));
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static ItemStack setLore(ItemStack itemStack, String... lore) {
        return setLore(itemStack, new ArrayList<>(Arrays.asList(lore)));
    }

    public static ItemStack setLore(ItemStack itemStack, List<String> list) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setLore(list);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static ItemStack unbreakable(ItemStack itemStack, boolean unbreakable) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setUnbreakable(unbreakable);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static ItemStack enchant(ItemStack itemStack, Enchantment enchantment, int level) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            if (level <= 0) {
                if (itemMeta.hasEnchant(enchantment)) {
                    itemMeta.removeEnchant(enchantment);
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
            itemMeta.addEnchant(enchantment, level, true);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static ItemStack enchant(ItemStack itemStack, Collection<Entry<Enchantment,Integer>> entries) {
        for (Entry<Enchantment,Integer> entry : entries) {
            enchant(itemStack, entry.getKey(), entry.getValue());
        }
        return itemStack;
    }

    public static ItemStack enchant(ItemStack itemStack, Entry<Enchantment,Integer>... entries) {
        return enchant(itemStack, new ArrayList<>(Arrays.asList(entries)));
    }

    public static ItemStack toItemStack(String jsonObjectString) {
        if (!(Ref.getVersion() <= 16 && Ref.getVersion() >= 9)) {
            return new ItemStack(Material.AIR);
        }
        if (JsonUtils.isJsonObject(jsonObjectString)) {
            return toItemStack(new JsonParser().parse(jsonObjectString).getAsJsonObject());
        }
        return new ItemStack(Material.AIR);
    }

    public static ItemStack toItemStack(JsonObject jsonObject) {
        if (!(Ref.getVersion() <= 16 && Ref.getVersion() >= 9)) {
            return new ItemStack(Material.AIR);
        }
        ItemStack itemStack = new ItemStack(Material.valueOf(jsonObject.get("type").getAsString().toUpperCase()));
        itemStack.setAmount(jsonObject.get("amount").getAsInt());
        if (jsonObject.has("nbt")) {
            Object nmsItemStack = null;
            try {
                nmsItemStack = Reflection_Class.asNMSCopy(itemStack);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            try {
                Reflection_Class.setTag(nmsItemStack, toNBTTagCompound(jsonObject.getAsJsonObject("nbt")));
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            try {
                return Reflection_Class.asBukkitCopy(nmsItemStack);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return itemStack;
    }

    public static String toJsonObjectString(ItemStack itemStack) {
        return new Gson().toJson(toJsonObject(itemStack));
    }

    public static JsonObject toJsonObject(ItemStack itemStack) {
        if (!(Ref.getVersion() <= 16 && Ref.getVersion() >= 9)) {
            return new JsonObject();
        }
        Object nmsItemStack = null;
        try {
            nmsItemStack = Reflection_Class.asNMSCopy(itemStack);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", itemStack.getType().name().toLowerCase());
        jsonObject.addProperty("amount", itemStack.getAmount());
        try {
            if (Reflection_Class.hasTag(nmsItemStack)) {
                jsonObject.add("nbt", parseNBTTagCompound(Reflection_Class.getTag(nmsItemStack)));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static Object toNBTTagCompound(JsonObject jsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object nbtTagCompound = null;
        try {
            nbtTagCompound = Reflection_Class.newNBTTagCompound();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement jsonElement = entry.getValue();
            if (jsonElement.isJsonObject()) {
                Reflection_Class.set(nbtTagCompound, key, toNBTTagCompound(jsonElement.getAsJsonObject()));
            } else if (jsonElement.isJsonArray()) {
                Reflection_Class.set(nbtTagCompound, key, toNBTTagList(jsonElement.getAsJsonArray()));
            } else if (jsonElement.getAsJsonPrimitive().isString()) {
                String value = jsonElement.getAsString();
                if (value.contains("$")) {
                    String[] splitted = value.split("\\$");
                    switch (splitted[0]) {
                        case "string":
                            String content = splitted[1];
                            if (StringUtils.count(value, "$") > 1) {
                                StringBuilder stringBuilder = new StringBuilder(content);
                                for (int i = 2; i < splitted.length; i++) {
                                    stringBuilder.append("$").append(splitted[i]);
                                }
                                content = stringBuilder.toString();
                            }
                            Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagString(), String.class, content));
                            break;
                        case "int":
                            if (StringUtils.count(value, "$") == 1) {
                                int i = Integer.parseInt(splitted[1]);
                                Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagInt(), int.class, i));
                            }
                            break;
                        case "long":
                            if (StringUtils.count(value, "$") == 1) {
                                long l = Long.parseLong(splitted[1]);
                                Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagLong(), long.class, l));
                            }
                            break;
                        case "float":
                            if (StringUtils.count(value, "$") == 1) {
                                float f = Float.parseFloat(splitted[1]);
                                Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagFloat(), float.class, f));
                            }
                            break;
                        case "double":
                            if (StringUtils.count(value, "$") == 1) {
                                double d = Double.parseDouble(splitted[1]);
                                Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagDouble(), double.class, d));
                            }
                            break;
                        case "short":
                            if (StringUtils.count(value, "$") == 1) {
                                short s = Short.parseShort(splitted[1]);
                                Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagShort(), short.class, s));
                            }
                            break;
                        case "byte":
                            if (StringUtils.count(value, "$") == 1) {
                                byte b = Byte.parseByte(splitted[1]);
                                Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagByte(), byte.class, b));
                            }
                            break;
                        case "int_array":
                            if (StringUtils.count(value, "$") == 1) {
                                JsonArray intJsonArray = new JsonParser().parse(splitted[1]).getAsJsonArray();
                                List<Integer> intList = new ArrayList<>();
                                for (JsonElement jsonElement1 : intJsonArray) {
                                    intList.add(jsonElement1.getAsInt());
                                }
                                Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagIntArray(), List.class, intList));
                            }
                            break;
                        case "long_array":
                            if (StringUtils.count(value, "$") == 1) {
                                JsonArray longJsonArray = new JsonParser().parse(splitted[1]).getAsJsonArray();
                                List<Long> longList = new ArrayList<>();
                                for (JsonElement jsonElement1 : longJsonArray) {
                                    longList.add(jsonElement1.getAsLong());
                                }
                                Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagLongArray(), List.class, longList));
                            }
                            break;
                        case "byte_array":
                            if (StringUtils.count(value, "$") == 1) {
                                JsonArray byteJsonArray = new JsonParser().parse(splitted[1]).getAsJsonArray();
                                List<Byte> byteList = new ArrayList<>();
                                for (JsonElement jsonElement1 : byteJsonArray) {
                                    byteList.add(jsonElement1.getAsByte());
                                }
                                Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagByteArray(), List.class, byteList));
                            }
                            break;
                    }
                }
            }
        }
        return nbtTagCompound;
    }

    private static AbstractList<?> toNBTTagList(JsonArray jsonArray) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AbstractList<Object> nbtTagList = null;
        try {
            nbtTagList = (AbstractList<Object>) Reflection_Class.newNBTTagList();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        for (JsonElement jsonElement : jsonArray) {
            if (jsonElement.isJsonObject()) {
                nbtTagList.add(toNBTTagCompound(jsonElement.getAsJsonObject()));
            } else if (jsonElement.isJsonArray()) {
                nbtTagList.add(toNBTTagList(jsonElement.getAsJsonArray()));
            } else if (jsonElement.getAsJsonPrimitive().isString()) {
                String value = jsonElement.getAsString();
                if (value.contains("$")) {
                    String[] splitted = value.split("\\$");
                    switch (splitted[0]) {
                        case "string":
                            String content = splitted[1];
                            if (StringUtils.count(value, "$") > 1) {
                                StringBuilder stringBuilder = new StringBuilder(content);
                                for (int i = 2; i < splitted.length; i++) {
                                    stringBuilder.append("$").append(splitted[i]);
                                }
                                content = stringBuilder.toString();
                            }
                            nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagString(), String.class, content));
                            break;
                        case "int":
                            if (StringUtils.count(value, "$") == 1) {
                                int i = Integer.parseInt(splitted[1]);
                                nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagInt(), int.class, i));
                            }
                            break;
                        case "long":
                            if (StringUtils.count(value, "$") == 1) {
                                long l = Long.parseLong(splitted[1]);
                                nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagLong(), long.class, l));
                            }
                            break;
                        case "float":
                            if (StringUtils.count(value, "$") == 1) {
                                float f = Float.parseFloat(splitted[1]);
                                nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagFloat(), float.class, f));
                            }
                            break;
                        case "double":
                            if (StringUtils.count(value, "$") == 1) {
                                double d = Double.parseDouble(splitted[1]);
                                nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagDouble(), double.class, d));
                            }
                            break;
                        case "short":
                            if (StringUtils.count(value, "$") == 1) {
                                short s = Short.parseShort(splitted[1]);
                                nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagShort(), short.class, s));
                            }
                            break;
                        case "byte":
                            if (StringUtils.count(value, "$") == 1) {
                                byte b = Byte.parseByte(splitted[1]);
                                nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagByte(), byte.class, b));
                            }
                            break;
                        case "int_array":
                            if (StringUtils.count(value, "$") == 1) {
                                JsonArray intJsonArray = new JsonParser().parse(splitted[1]).getAsJsonArray();
                                List<Integer> intList = new ArrayList<>();
                                for (JsonElement jsonElement1 : intJsonArray) {
                                    intList.add(jsonElement1.getAsInt());
                                }
                                nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagIntArray(), List.class, intList));
                            }
                            break;
                        case "long_array":
                            if (StringUtils.count(value, "$") == 1) {
                                JsonArray longJsonArray = new JsonParser().parse(splitted[1]).getAsJsonArray();
                                List<Long> longList = new ArrayList<>();
                                for (JsonElement jsonElement1 : longJsonArray) {
                                    longList.add(jsonElement1.getAsLong());
                                }
                                nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagLongArray(), List.class, longList));
                            }
                            break;
                        case "byte_array":
                            if (StringUtils.count(value, "$") == 1) {
                                JsonArray byteJsonArray = new JsonParser().parse(splitted[1]).getAsJsonArray();
                                List<Byte> byteList = new ArrayList<>();
                                for (JsonElement jsonElement1 : byteJsonArray) {
                                    byteList.add(jsonElement1.getAsByte());
                                }
                                nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagByteArray(), List.class, byteList));
                            }
                            break;
                    }
                }
            }
        }
        return nbtTagList;
    }

    private static JsonObject parseNBTTagCompound(Object nbtTagCompound) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        JsonObject nbtJsonObject = new JsonObject();
        for (String key : Reflection_Class.getKeys(nbtTagCompound)) {
            Object nbtBase = Reflection_Class.get(nbtTagCompound, key);
            if (nbtBase.getClass().equals(Reflection_Class.NBTTagString())) {
                nbtJsonObject.addProperty(key, "string$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(nbtBase) : nbtBase.getClass().getMethod("asString").invoke(nbtBase)));
            } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagInt())) {
                nbtJsonObject.addProperty(key, "int$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(nbtBase) : nbtBase.getClass().getMethod("asInt").invoke(nbtBase)));
            } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagLong())) {
                nbtJsonObject.addProperty(key, "long$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(nbtBase) : nbtBase.getClass().getMethod("asLong").invoke(nbtBase)));
            } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagShort())) {
                nbtJsonObject.addProperty(key, "short$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(nbtBase) : nbtBase.getClass().getMethod("asShort").invoke(nbtBase)));
            } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagByte())) {
                nbtJsonObject.addProperty(key, "byte$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(nbtBase) : nbtBase.getClass().getMethod("asByte").invoke(nbtBase)));
            } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagFloat())) {
                nbtJsonObject.addProperty(key, "float$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(nbtBase) : nbtBase.getClass().getMethod("asFloat").invoke(nbtBase)));
            } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagDouble())) {
                nbtJsonObject.addProperty(key, "double$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(nbtBase) : nbtBase.getClass().getMethod("asDouble").invoke(nbtBase)));
            } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagIntArray())) {
                int[] ints = (int[]) (Ref.getVersion() < 16 ? Reflection_Class.getData(nbtBase) : nbtBase.getClass().getMethod("getInts").invoke(nbtBase));
                StringBuilder stringBuilder = new StringBuilder("[");
                for (int i : ints) {
                    stringBuilder.append(i).append(",");
                }
                String string = stringBuilder.substring(0, stringBuilder.length()-1) + "]";
                nbtJsonObject.addProperty(key, "int_array$" + string);
            } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagLongArray())) {
                long[] longs = (long[]) (Ref.getVersion() < 16 ? (Ref.getVersion() < 11 ? nbtBase.getClass().getMethod("d").invoke(nbtBase) : nbtBase.getClass().getMethod("getLongs").invoke(nbtBase)) : nbtBase.getClass().getMethod("getLongs").invoke(nbtBase));
                StringBuilder stringBuilder = new StringBuilder("[");
                for (long l : longs) {
                    stringBuilder.append(l).append(",");
                }
                String string = stringBuilder.substring(0, stringBuilder.length()-1) + "]";
                nbtJsonObject.addProperty(key, "long_array$" + string);
            } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagByteArray())) {
                byte[] by = (byte[]) (Ref.getVersion() < 16 ? Reflection_Class.getData(nbtBase) : nbtBase.getClass().getMethod("getBytes").invoke(nbtBase));
                StringBuilder stringBuilder = new StringBuilder("[");
                for (byte b : by) {
                    stringBuilder.append(b).append(",");
                }
                String string = stringBuilder.substring(0, stringBuilder.length()-1) + "]";
                nbtJsonObject.addProperty(key, "byte_array$" + string);
            } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagCompound())) {
                nbtJsonObject.add(key, parseNBTTagCompound(nbtBase));
            } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagList())) {
                AbstractList<?> nbtTagList = (AbstractList<?>) nbtBase;
                if (nbtTagList.size() > 0) {
                    nbtJsonObject.add(key, parseNBTTagList(nbtTagList));
                }
            }
        }
        return nbtJsonObject;
    }

    private static JsonArray parseNBTTagList(AbstractList<?> nbtTagList) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        JsonArray jsonArray = new JsonArray();
        for (Object base : nbtTagList) {
            if (base.getClass().equals(Reflection_Class.NBTTagString())) {
                jsonArray.add("string$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(base) : base.getClass().getMethod("asString").invoke(base)));
            } else if (base.getClass().equals(Reflection_Class.NBTTagInt())) {
                jsonArray.add("int$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(base) : base.getClass().getMethod("asInt").invoke(base)));
            } else if (base.getClass().equals(Reflection_Class.NBTTagLong())) {
                jsonArray.add("long$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(base) : base.getClass().getMethod("asLong").invoke(base)));
            } else if (base.getClass().equals(Reflection_Class.NBTTagShort())) {
                jsonArray.add("short$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(base) : base.getClass().getMethod("asShort").invoke(base)));
            } else if (base.getClass().equals(Reflection_Class.NBTTagByte())) {
                jsonArray.add("byte$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(base) : base.getClass().getMethod("asByte").invoke(base)));
            } else if (base.getClass().equals(Reflection_Class.NBTTagFloat())) {
                jsonArray.add("float$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(base) : base.getClass().getMethod("asFloat").invoke(base)));
            } else if (base.getClass().equals(Reflection_Class.NBTTagDouble())) {
                jsonArray.add("double$" + (Ref.getVersion() < 16 ? Reflection_Class.getData(base) : base.getClass().getMethod("asDouble").invoke(base)));
            } else if (base.getClass().equals(Reflection_Class.NBTTagIntArray())) {
                int[] ints = (int[]) (Ref.getVersion() < 16 ? Reflection_Class.getData(base) : base.getClass().getMethod("getInts").invoke(base));
                StringBuilder stringBuilder = new StringBuilder("[");
                for (int i : ints) {
                    stringBuilder.append(i).append(",");
                }
                String string = stringBuilder.substring(0, stringBuilder.length()-1) + "]";
                jsonArray.add("int_array$" + string);
            } else if (base.getClass().equals(Reflection_Class.NBTTagLongArray())) {
                long[] longs = (long[]) (Ref.getVersion() < 16 ? (Ref.getVersion() < 11 ? base.getClass().getMethod("d").invoke(base) : base.getClass().getMethod("getLongs").invoke(base)) : base.getClass().getMethod("getLongs").invoke(base));
                StringBuilder stringBuilder = new StringBuilder("[");
                for (long l : longs) {
                    stringBuilder.append(l).append(",");
                }
                String string = stringBuilder.substring(0, stringBuilder.length()-1) + "]";
                jsonArray.add("long_array$" + string);
            } else if (base.getClass().equals(Reflection_Class.NBTTagByteArray())) {
                byte[] by = (byte[]) (Ref.getVersion() < 16 ? Reflection_Class.getData(base) : base.getClass().getMethod("getBytes").invoke(base));
                StringBuilder stringBuilder = new StringBuilder("[");
                for (byte b : by) {
                    stringBuilder.append(b).append(",");
                }
                String string = stringBuilder.substring(0, stringBuilder.length()-1) + "]";
                jsonArray.add("byte_array$" + string);
            } else if (base.getClass().equals(Reflection_Class.NBTTagCompound())) {
                jsonArray.add(parseNBTTagCompound(base));
            } else if (base.getClass().equals(Reflection_Class.NBTTagList())) {
                jsonArray.add(parseNBTTagList((AbstractList<?>) base));
            }
        }
        return jsonArray;
    }

    private static class Reflection_Class {

        private static Set<String> getKeys(Object nbtTagCompound) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            return (Set<String>) NBTTagCompound().getMethod("getKeys").invoke(nbtTagCompound);
        }

        private static Object get(Object nbtTagCompound, String key) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            return NBTTagCompound().getMethod("get", String.class).invoke(nbtTagCompound, key);
        }

        private static void set(Object nbtTagCompound, String key, Object nbtBase) {
            try {
                NBTTagCompound().getMethod("set", String.class, NBTBase()).invoke(nbtTagCompound, key, nbtBase);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        private static Object getData(Object nbtBaseNotCompoundOrList) throws NoSuchFieldException, IllegalAccessException {
            Field field = nbtBaseNotCompoundOrList.getClass().getDeclaredField("data");
            field.setAccessible(true);
            return field.get(nbtBaseNotCompoundOrList);
        }

        private static Object createData(Class<?> clazz, Class<?> anotherClass, Object data) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            Constructor<?> constructor = clazz.getDeclaredConstructor(anotherClass);
            constructor.setAccessible(true);
            return constructor.newInstance(data);
        }

        private static void setTag(Object nmsItemStack, Object nbtTagCompound) {
            try {
                ItemStack().getMethod("setTag", NBTTagCompound()).invoke(nmsItemStack, nbtTagCompound);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        private static boolean hasTag(Object nmsItemStack) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            return (boolean) ItemStack().getMethod("hasTag").invoke(nmsItemStack);
        }

        private static Object getTag(Object nmsItemStack) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            return ItemStack().getMethod("getTag").invoke(nmsItemStack);
        }

        public static Class<?> CraftItemStack() {
            return Ref.getObcClass("inventory.CraftItemStack");
        }

        public static Class<?> ItemStack() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("ItemStack");
            } else {
                return Ref.getClass("net.minecraft.world.item.ItemStack");
            }
        }

        public static Object newNBTTagCompound() throws InstantiationException, IllegalAccessException {
            return NBTTagCompound().newInstance();
        }

        public static Object newNBTTagList() throws InstantiationException, IllegalAccessException {
            return NBTTagList().newInstance();
        }

        public static Class<?> NBTTagCompound() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTTagCompound");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTTagCompound");
            }
        }

        public static Class<?> NBTTagString() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTTagString");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTTagString");
            }
        }

        public static Class<?> NBTTagInt() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTTagInt");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTTagInt");
            }
        }

        public static Class<?> NBTTagLong() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTTagLong");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTTagLong");
            }
        }

        public static Class<?> NBTTagShort() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTTagShort");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTTagShort");
            }
        }

        public static Class<?> NBTTagByte() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTTagByte");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTTagByte");
            }
        }

        public static Class<?> NBTTagFloat() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTTagFloat");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTTagFloat");
            }
        }

        public static Class<?> NBTTagDouble() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTTagDouble");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTTagDouble");
            }
        }

        public static Class<?> NBTTagIntArray() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTTagIntArray");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTTagIntArray");
            }
        }

        public static Class<?> NBTTagLongArray() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTTagLongArray");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTTagLongArray");
            }
        }

        public static Class<?> NBTTagByteArray() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTTagByteArray");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTTagByteArray");
            }
        }

        public static Class<?> NBTTagList() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTTagList");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTTagList");
            }
        }

        public static Class<?> NBTBase() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("NBTBase");
            } else {
                return Ref.getClass("net.minecraft.nbt.NBTBase");
            }
        }

        private static Object asNMSCopy(ItemStack itemStack) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            return CraftItemStack().getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);
        }

        private static ItemStack asBukkitCopy(Object nmsItemStack) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            return (ItemStack) CraftItemStack().getMethod("asBukkitCopy", ItemStack()).invoke(null, nmsItemStack);
        }

        private static Class<?> EntityItem() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("EntityItem");
            } else {
                return Ref.getClass("net.minecraft.world.entity.item.EntityItem");
            }
        }

        private static int getId(Object nmsEntity) {
            try {
                return (int) nmsEntity.getClass().getMethod("getId").invoke(nmsEntity);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return 0;
        }

        private static void setOwner(Object nmsEntityItem, Player player) {
            try {
                nmsEntityItem.getClass().getMethod("setOwner", UUID.class).invoke(nmsEntityItem, player.getUniqueId());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                try {
                    nmsEntityItem.getClass().getMethod("b", UUID.class).invoke(nmsEntityItem, player.getUniqueId());
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
            }
        }

        private static Class<?> PacketPlayOutEntityDestroy() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("PacketPlayOutEntityDestroy");
            } else {
                return Ref.getClass("net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy");
            }
        }

        private static Class<?> PacketPlayOutSpawnEntity() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("PacketPlayOutSpawnEntity");
            } else {
                return Ref.getClass("net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity");
            }
        }

        private static Class<?> PacketPlayOutEntityMetadata() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("PacketPlayOutEntityMetadata");
            } else {
                return Ref.getClass("net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata");
            }
        }

        private static Class<?> Packet() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("Packet");
            } else {
                return Ref.getClass("net.minecraft.network.protocol.Packet");
            }
        }

        private static Class<?> PlayerConnection() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("PlayerConnection");
            } else {
                return Ref.getClass("net.minecraft.server.network.PlayerConnection");
            }
        }

        private static Object getDataWatcher(Object nmsEntity) {
            try {
                return nmsEntity.getClass().getMethod("getDataWatcher").invoke(nmsEntity);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static Object createPacketPlayOutEntityDestroy(int... entityID) {
            try {
                return PacketPlayOutEntityDestroy().getConstructor(entityID.getClass()).newInstance(new Object[] { entityID });
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
            }
            return null;
        }

        private static Class<?> Entity() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("Entity");
            } else {
                return Ref.getClass("net.minecraft.world.entity.Entity");
            }
        }

        private static Object createPacketPlayOutSpawnEntity(Object entity, int i) {
            try {
                return PacketPlayOutSpawnEntity().getConstructor(Entity(), int.class).newInstance(entity, i);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static Object createPacketPlayOutEntityMetadata(int entityId, Object dataWatcher, boolean bool) {
            try {
                return PacketPlayOutEntityMetadata().getConstructor(int.class, DataWatcher(), boolean.class).newInstance(entityId, dataWatcher, bool);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
            }
            return null;
        }

        private static Class<?> DataWatcher() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("DataWatcher");
            } else {
                return Ref.getClass("net.minecraft.network.syncher.DataWatcher");
            }
        }

        private static void sendPacket(Player player, Object packet) {
            Class<?> CraftPlayer = player.getClass();
            try {
                Object EntityPlayer = CraftPlayer.getMethod("getHandle");
                Class<?> EntityPlayerClass = EntityPlayer.getClass();
                for (Field field : EntityPlayerClass.getDeclaredFields()) {
                    if (field.getType().equals(PlayerConnection())) {
                        Object PlayerConnection = field.get(EntityPlayer);
                        PlayerConnection().getMethod("sendPacket", Packet()).invoke(PlayerConnection, packet);
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            }
        }

        private static Object getEntityItem(Item item) {
            Class<?> CraftItemClass = item.getClass();
            try {
                return CraftItemClass.getMethod("getHandle").invoke(item);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
            }
            return null;
        }

        private static Class<?> World() {
            if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
                return Ref.getNmsClass("World");
            } else {
                return Ref.getClass("net.minecraft.world.level.World");
            }
        }

        private static void setPickUpDelayTo32767(Object entityItem) {
            try {
                entityItem.getClass().getMethod("q").invoke(entityItem);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        private static Object nmsWorld(World world) {
            try {
                return world.getClass().getMethod("getHandle").invoke(world);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static Object createEntityItem(ItemStack itemStack, Location location) {
            try {
                return EntityItem().getConstructor(World(), double.class, double.class, double.class, ItemStack()).newInstance(nmsWorld(location.getWorld()), location.getX(), location.getY(), location.getZ(), asNMSCopy(itemStack));
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
    
    
}

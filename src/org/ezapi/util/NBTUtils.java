package org.ezapi.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class NBTUtils {

    public static void setNBT(ItemStack itemStack, JsonObject nbtTagCompound) {
        try {
            Object nmsItemStack = Reflection_Class.asNMSCopy(itemStack);
            Reflection_Class.setTag(nmsItemStack, toNBTTagCompound(nbtTagCompound));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
        }
    }

    public static Object toNBTTagCompound(JsonObject jsonObject) {
        try {
            Object nbtTagCompound = null;
            try {
                nbtTagCompound = Reflection_Class.newNBTTagCompound();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                JsonElement jsonElement = entry.getValue();
                if (jsonElement.isJsonObject()) {
                    Reflection_Class.set(nbtTagCompound, key, toNBTTagCompound(jsonElement.getAsJsonObject()));
                } else if (jsonElement.isJsonArray()) {
                    Reflection_Class.set(nbtTagCompound, key, toNBTTagList(jsonElement.getAsJsonArray()));
                } else if (jsonElement.isJsonPrimitive()) {
                    if (jsonElement.getAsJsonPrimitive().isString()) {
                        Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagString(), String.class, jsonElement.getAsString()));
                    } else if (jsonElement.getAsJsonPrimitive().isNumber()) {
                        Number number = jsonElement.getAsNumber();
                        if (number instanceof Integer) {
                            Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagInt(), int.class, jsonElement.getAsInt()));
                        } else if (number instanceof Double) {
                            Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagDouble(), double.class, jsonElement.getAsDouble()));
                        } else if (number instanceof Short) {
                            Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagShort(), short.class, jsonElement.getAsShort()));
                        } else if (number instanceof Long) {
                            Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagLong(), long.class, jsonElement.getAsLong()));
                        } else if (number instanceof Float) {
                            Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagFloat(), float.class, jsonElement.getAsDouble()));
                        } else if (number instanceof Byte) {
                            Reflection_Class.set(nbtTagCompound, key, Reflection_Class.createData(Reflection_Class.NBTTagByte(), byte.class, jsonElement.getAsByte()));
                        }
                    }
                }
            }
            return nbtTagCompound;
        } catch (Exception ignored) {
        }
        return null;
    }

    public static Object toNBTTagList(JsonArray jsonArray) {
        try {
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
                } else if (jsonElement.isJsonPrimitive()) {
                    if (jsonElement.getAsJsonPrimitive().isString()) {
                        nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagString(), String.class, jsonElement.getAsString()));
                    } else if (jsonElement.getAsJsonPrimitive().isNumber()) {
                        Number number = jsonElement.getAsNumber();
                        if (number instanceof Integer) {
                            nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagInt(), int.class, jsonElement.getAsInt()));
                        } else if (number instanceof Double) {
                            nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagDouble(), double.class, jsonElement.getAsDouble()));
                        } else if (number instanceof Short) {
                            nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagShort(), short.class, jsonElement.getAsShort()));
                        } else if (number instanceof Long) {
                            nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagLong(), long.class, jsonElement.getAsLong()));
                        } else if (number instanceof Float) {
                            nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagFloat(), float.class, jsonElement.getAsDouble()));
                        } else if (number instanceof Byte) {
                            nbtTagList.add(Reflection_Class.createData(Reflection_Class.NBTTagByte(), byte.class, jsonElement.getAsByte()));
                        }
                    }
                }
            }
            return nbtTagList;
        } catch (Exception ignored) {
        }
        return null;
    }

    public static JsonObject parseNBTTagCompound(Object nbtTagCompound) {
        try {
            JsonObject nbtJsonObject = new JsonObject();
            for (String key : Reflection_Class.getKeys(nbtTagCompound)) {
                Object nbtBase = Reflection_Class.get(nbtTagCompound, key);
                if (nbtBase.getClass().equals(Reflection_Class.NBTTagString())) {
                    nbtJsonObject.addProperty(key, (String) Reflection_Class.getData(nbtBase));
                } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagInt())) {
                    nbtJsonObject.addProperty(key, (Integer) Reflection_Class.getData(nbtBase));
                } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagLong())) {
                    nbtJsonObject.addProperty(key, (Long) Reflection_Class.getData(nbtBase));
                } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagShort())) {
                    nbtJsonObject.addProperty(key, (Short) Reflection_Class.getData(nbtBase));
                } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagByte())) {
                    nbtJsonObject.addProperty(key, (Byte) Reflection_Class.getData(nbtBase));
                } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagFloat())) {
                    nbtJsonObject.addProperty(key, (Float) Reflection_Class.getData(nbtBase));
                } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagDouble())) {
                    nbtJsonObject.addProperty(key, (Double) Reflection_Class.getData(nbtBase));
                } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagIntArray())) {
                    int[] ints = (int[]) Reflection_Class.getData(nbtBase);
                    JsonArray array = new JsonArray();
                    for (int i : ints) {
                        array.add(i);
                    }
                    nbtJsonObject.add(key, array);
                } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagLongArray())) {
                    long[] longs = (long[]) Reflection_Class.getData(nbtBase);
                    JsonArray array = new JsonArray();
                    for (long l : longs) {
                        array.add(l);
                    }
                    nbtJsonObject.add(key, array);
                } else if (nbtBase.getClass().equals(Reflection_Class.NBTTagByteArray())) {
                    byte[] by = (byte[]) Reflection_Class.getData(nbtBase);
                    JsonArray array = new JsonArray();
                    for (byte b : by) {
                        array.add(b);
                    }
                    nbtJsonObject.add(key, array);
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
        } catch (Exception ignored) {
        }
        return new JsonObject();
    }

    public static JsonArray parseNBTTagList(Object nmsNbtTaglist) {
        try {
            AbstractList<?> nbtTagList = (AbstractList<?>) nmsNbtTaglist;
            JsonArray jsonArray = new JsonArray();
            for (Object base : nbtTagList) {
                if (base.getClass().equals(Reflection_Class.NBTTagString())) {
                    jsonArray.add((String) Reflection_Class.getData(base));
                } else if (base.getClass().equals(Reflection_Class.NBTTagInt())) {
                    jsonArray.add((Integer) Reflection_Class.getData(base));
                } else if (base.getClass().equals(Reflection_Class.NBTTagLong())) {
                    jsonArray.add((Long) Reflection_Class.getData(base));
                } else if (base.getClass().equals(Reflection_Class.NBTTagShort())) {
                    jsonArray.add((Short) Reflection_Class.getData(base));
                } else if (base.getClass().equals(Reflection_Class.NBTTagByte())) {
                    jsonArray.add((Byte) Reflection_Class.getData(base));
                } else if (base.getClass().equals(Reflection_Class.NBTTagFloat())) {
                    jsonArray.add((Float) Reflection_Class.getData(base));
                } else if (base.getClass().equals(Reflection_Class.NBTTagDouble())) {
                    jsonArray.add((Double) Reflection_Class.getData(base));
                } else if (base.getClass().equals(Reflection_Class.NBTTagIntArray())) {
                    int[] ints = (int[]) Reflection_Class.getData(base);
                    JsonArray array = new JsonArray();
                    for (int i : ints) {
                        array.add(i);
                    }
                    jsonArray.add(array);
                } else if (base.getClass().equals(Reflection_Class.NBTTagLongArray())) {
                    long[] longs = (long[]) Reflection_Class.getData(base);
                    JsonArray array = new JsonArray();
                    for (long l : longs) {
                        array.add(l);
                    }
                    jsonArray.add(array);
                } else if (base.getClass().equals(Reflection_Class.NBTTagByteArray())) {
                    byte[] by = (byte[]) Reflection_Class.getData(base);
                    JsonArray array = new JsonArray();
                    for (byte b : by) {
                        array.add(b);
                    }
                    jsonArray.add(array);
                } else if (base.getClass().equals(Reflection_Class.NBTTagCompound())) {
                    jsonArray.add(parseNBTTagCompound(base));
                } else if (base.getClass().equals(Reflection_Class.NBTTagList())) {
                    jsonArray.add(parseNBTTagList(base));
                }
            }
            return jsonArray;
        } catch (Exception ignored) {
        }
        return null;
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
        
    }
    
}

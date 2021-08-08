package org.ezapi.storage.sql;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import org.ezapi.reflect.EzClass;
import org.ezapi.util.Ref;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

final class Test {

    public static void main(String[] args) throws SQLException, IOException {
        /*
        Mysql mysql = new Mysql("127.0.0.1", 3306, "minecraft", "ezapi", "root", "xxxxxxxxxxxx");
        SqlContext sqlContext = new SqlContext();
        sqlContext.setString("test", "1");
        mysql.set("a", sqlContext);
        SqlContext got = mysql.get("a");
        String aaaa = got.getString("test");
        System.out.println(Integer.parseInt(aaaa));
        mysql.close();
        */
        /*
        MongoDB mongoDB = new MongoDB("127.0.0.1", 27017, "root", "xxxxxxxxxxxx", "admin", "test");
        StorageContext storageContext = new StorageContext();
        StorageContext friendA = new StorageContext();
        friendA.setString("name", "Gerry5126");
        StorageContext friendB = new StorageContext();
        friendB.setString("name", "jianslys");
        storageContext.setContextList("friends", Arrays.asList(friendA, friendB));
        mongoDB.set("DeeChael", Collections.singletonList(storageContext));
        System.out.println(mongoDB.get("DeeChael").get(0));
        mongoDB.close();
        */

        //EzClass Reflection examples
        EzClass ChatMessage = new EzClass(Ref.getNmsOrOld("network.chat.ChatMessage", "ChatMessage"));
        ChatMessage.setConstructor(String.class);
        ChatMessage.newInstance("Testing");
        Object chatMessageEz = ChatMessage.getInstance();

        //Java Original Reflection examples
        Class<?> clazz = Ref.getNmsOrOld("network.chat.ChatMessage", "ChatMessage");
        if (clazz != null) {
            try {
                Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
                constructor.setAccessible(true);
                Object chatMessage = constructor.newInstance("Testing");
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}

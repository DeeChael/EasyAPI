package org.ezapi.storage.sql;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;

import java.io.File;
import java.io.IOException;
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
    }

}

package common;

import java.io.FileInputStream;
import java.util.Properties;

public class GlobalSettings {

    public static Properties prop = getProperties();

    public static String mySqlUrl = prop.getProperty("mySqlUrl", "jdbc:mysql://127.0.0.1:3306/yhtest?rewriteBatchedStatements=true");

    public static String mySqlUserName = prop.getProperty("mySqlUserName", "root");

    public static String mySqlPassword = prop.getProperty("mySqlPassword", "");

    public static String projectName = prop.getProperty("projectName", "gwcomtest");

    public static String publicData = prop.getProperty("publicData","");

    public static String bort = prop.getProperty("bort", "b");

    public static String testEnv = prop.getProperty("testEnv","api-qa.yonghuivip.com");

    public static Properties getProperties() {
        Properties prop = new Properties();
        try {
            FileInputStream file = new FileInputStream("prop.properties");
            prop.load(file);
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }
}

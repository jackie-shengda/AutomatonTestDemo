package common;

import java.io.FileInputStream;
import java.util.Properties;

public class GlobalSettings {

    public static Properties prop = getProperties();

    //数据库地址
    public static String mySqlUrl = prop.getProperty("mySqlUrl", "jdbc:mysql://127.0.0.1:3306/yhtest?rewriteBatchedStatements=true");

    //数据库用户名
    public static String mySqlUserName = prop.getProperty("mySqlUserName", "root");

    //数据库密码
    public static String mySqlPassword = prop.getProperty("mySqlPassword", "");

    //项目名称
    public static String projectName = prop.getProperty("projectName", "gwcomtest");

    //公共数据服务
    public static String publicData = prop.getProperty("publicData","");

    //环境说明，develop、release、prelaunch、master什么的，跟在项目名称后面，标记当前项目运行环境
    public static String bort = prop.getProperty("bort", "b");

    //测试环境，develop、release、prelaunch、master等等
    public static String testEnv = prop.getProperty("testEnv","api-qa.yonghuivip.com");

    //rebase开关，为true时rebase
    public static String isRebase = prop.getProperty("isRebase","false");

    //登录令牌，动态刷新用
    public static String access_token = prop.getProperty("access_token","601933");

    public static String getAccess_token() {
        return access_token;
    }

    public static void setAccess_token(String access_token) {
        GlobalSettings.access_token = access_token;
    }

    public static String getMySqlUrl() {
        return mySqlUrl;
    }

    public static void setMySqlUrl(String mySqlUrl) {
        GlobalSettings.mySqlUrl = mySqlUrl;
    }

    public static String getMySqlUserName() {
        return mySqlUserName;
    }

    public static void setMySqlUserName(String mySqlUserName) {
        GlobalSettings.mySqlUserName = mySqlUserName;
    }

    public static String getMySqlPassword() {
        return mySqlPassword;
    }

    public static void setMySqlPassword(String mySqlPassword) {
        GlobalSettings.mySqlPassword = mySqlPassword;
    }

    public static String getPublicData() {
        return publicData;
    }

    public static void setPublicData(String publicData) {
        GlobalSettings.publicData = publicData;
    }

    public static String getProjectName() {
        return projectName;
    }

    public static void setProjectName(String projectName) {
        GlobalSettings.projectName = projectName;
    }

    public static String getBort() {
        return bort;
    }

    public static void setBort(String bort) {
        GlobalSettings.bort = bort;
    }

    public static String getTestEnv() {
        return testEnv;
    }

    public static void setTestEnv(String testEnv) {
        GlobalSettings.testEnv = testEnv;
    }

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

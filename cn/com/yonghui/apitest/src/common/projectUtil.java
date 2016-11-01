package common;

import org.apache.log4j.Logger;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class projectUtil {

    private static Logger logger = Logger.getLogger(projectUtil.class.getName());

    public boolean checkParameter(List<String> lstExList, Set<String> keys) {

        for (String strKey : keys) {
            if (lstExList.contains(strKey)) {
                lstExList.remove(strKey);
            }
        }
        if (lstExList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isFileExists(String path) {
        boolean res = false;

        File file = new File(path);
        if (file.exists()) {
            res = true;
        }

        return res;
    }

    public static String formatName(String classname) {
        int dotNum = classname.indexOf(".");
        if (dotNum > 0) {
            classname = classname.substring(classname.lastIndexOf(".") + 1,
                    classname.length());
        }
        return classname;
    }

    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);

        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    @BeforeSuite
    public void setUpBeforeSuite() {
        projectUtil.delFolder("./results/comdata");
        projectUtil.createFolder("./results/comdata");
    }

    public static boolean isSubFileExists(String root, String fileName) {

        File file = new File(root);
        File[] subFile = file.listFiles();
        boolean flag = false;

        int i = 0;
        while (i < subFile.length) {
            if (subFile[i].isDirectory()) {
                File f = new File(subFile[i].toString() + "/" + fileName);
                if (f.exists()) {
                    flag = true;
                    break;
                }
            } else {
                flag = false;
            }
        }

        return flag;
    }

    public static void copyfiles(String resDir, String tgtDir) {
        try {
            String cmdStr = "cmd /c robocopy " + resDir + " " + tgtDir
                    + " /s /E";
            Runtime.getRuntime().exec(cmdStr);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath);
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);
                delFolder(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }

    public static void copy(String src, String des) {
        File file1 = new File(src);
        File[] fs = file1.listFiles();
        File file2 = new File(des);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        for (File f : fs) {
            if (f.isFile()) {
                fileCopy(f.getPath(), des + "\\" + f.getName()); // 调用文件拷贝的方法
            } else if (f.isDirectory()) {
                copy(f.getPath(), des + "\\" + f.getName());
            }
        }

    }

    /**
     * 文件拷贝的方法
     */
    public static void fileCopy(String src, String des) {

        BufferedReader br = null;
        PrintStream ps = null;

        try {
            File urlFile = new File(src);
            InputStream in = new FileInputStream(urlFile);
            OutputStream out = new FileOutputStream(new File(des));

            byte buff[] = new byte[32 * 1024];
            int j = 0;
            while ((j = in.read(buff)) != -1) {

                out.write(buff, 0, j);
            }

            in.close();
            out.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            try {
                if (br != null)
                    br.close();
                if (ps != null)
                    ps.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> getWindTowerFileList(String path) {
        ArrayList<String> fileList = new ArrayList<String>();
        File file = new File(path);
        String[] arrFile = file.list();
        if (arrFile != null) {
            for (int i = 0; i < arrFile.length; i++) {
                if (arrFile[i].startsWith(".")) {
                    continue;
                }
                fileList.add(path + File.separator + arrFile[i]);
            }
            return fileList;
        }
        return null;
    }

    /*
    //output json file
    public static void ouputJson(JSONObject obj, String filePath) {
        try {
            FileWriter file = new FileWriter(filePath);
            file.write(obj.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getJsonFileString(String filePath) throws UnsupportedEncodingException, FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object jsonObj = parser.parse(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));
        String str = jsonObj.toString();

        return str;
    }
    */
    public static Date log(String strLog) {
        Date date = new Date();
        String time = new SimpleDateFormat("yyyyMMdd-HHmmss").format(date);
        logger.info(time + ": " + strLog);
        Reporter.log(time + ": " + strLog);
        return date;
    }

    public static void logSpan(String strMethod, Date startTime, Date endTime) {
        String time = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        long timespan = (endTime.getTime() - startTime.getTime());
        Reporter.log(time + ":" + strMethod + " takes " + timespan + "ms.");
        logger.info(time + ":" + strMethod + " takes " + timespan + "ms.");
    }

    public static String errDesc(int errorCode) {
        try {
            boolean blfg = false;
            Integer integer = new Integer(errorCode);
            String strCode = integer.toString();
            FileInputStream in = new FileInputStream("GRNDBErrorCode.ini");
            // 指定读取文件时以UTF-8的格式读取
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));
            String str;
            while ((str = br.readLine()) != null) {
                String[] arrItem = str.split(",");
                if (arrItem[1].toString().trim().equals(strCode)) {
                    blfg = true;
                    in = null;
                    return "Error code:" + errorCode + ". Error description:" + arrItem[0];
                }
            }
            if (!blfg) {
                return "Cannot find the error description.";
            }
        } catch (Exception e) {
            e.printStackTrace();// 打印错误信息
        }
        return "Cannot find the error description.";

    }

    public static void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static int stringToInt(String intstr) {
        Integer integer;
        integer = Integer.valueOf(intstr);
        return integer.intValue();
    }


    public static String intToString(int value) {
        Integer integer = new Integer(value);
        return integer.toString();
    }

    public static float stringToFloat(String floatstr) {
        Float floatee;
        floatee = Float.valueOf(floatstr);
        return floatee.floatValue();
    }

    public static String floatToString(float value) {
        Float floatee = new Float(value);
        return floatee.toString();
    }

    public static java.sql.Date stringToDate(String dateStr) {
        return java.sql.Date.valueOf(dateStr);
    }

    public static String dateToString(java.sql.Date datee) {
        return datee.toString();
    }

}

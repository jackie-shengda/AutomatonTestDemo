package common;

import org.junit.Assert;
import org.testng.Reporter;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileOperateUtil {
	
	/**
	 * 复制单个文件
	 *
	 * @param srcFileName
	 *            待复制的文件名
	 * @param destFileName
	 *            目标文件名
	 * @param overlay
	 *            如果目标文件存在，是否覆盖
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public  static boolean copyFile(String srcFileName, String destFileName, boolean overlay) {
	    // 判断原文件是否存在
	    File srcFile = new File(srcFileName);
	    if (!srcFile.exists()) {
	        System.out.println("复制文件失败：原文件" + srcFileName + "不存在！");
			Reporter.log("复制文件失败：原文件" + srcFileName + "不存在！");
	        return false;
	    } else if (!srcFile.isFile()) {
	        System.out.println("复制文件失败：" + srcFileName + "不是一个文件！");
			Reporter.log("复制文件失败：" + srcFileName + "不是一个文件！");
	        return false;
	    }
	    // 判断目标文件是否存在
	    File destFile = new File(destFileName);
	    if (destFile.exists()) {
	        // 如果目标文件存在，而且复制时允许覆盖。
	        if (overlay) {
	            // 删除已存在的目标文件，无论目标文件是目录还是单个文件
	            System.out.println("目标文件已存在，准备删除它！");
				Reporter.log("目标文件已存在，准备删除它！");
	            if (!destFile.delete()) {
	                System.out.println("复制文件失败：删除目标文件" + destFileName + "失败！");
					Reporter.log("复制文件失败：删除目标文件" + destFileName + "失败！");
	                return false;
	            }
	        } else {
	            System.out.println("复制文件失败：目标文件" + destFileName + "已存在！");
				Reporter.log("复制文件失败：目标文件" + destFileName + "已存在！");
	            return false;
	        }
	    } else {
	        if (!destFile.getParentFile().exists()) {
	            // 如果目标文件所在的目录不存在，则创建目录
	            System.out.println("目标文件所在的目录不存在，准备创建它！");
				Reporter.log("目标文件所在的目录不存在，准备创建它！");
	            if (!destFile.getParentFile().mkdirs()) {
	                System.out.println("复制文件失败：创建目标文件所在的目录失败！");
					Reporter.log("复制文件失败：创建目标文件所在的目录失败！");
	                return false;
	            }
	        }
	    }
	    // 准备复制文件
	    int byteread = 0;// 读取的位数
	    InputStream in = null;
	    OutputStream out = null;
	    try {
	        // 打开原文件
	        in = new FileInputStream(srcFile);
	        // 打开连接到目标文件的输出流
	        out = new FileOutputStream(destFile);
	        byte[] buffer = new byte[1024];
	        // 一次读取1024个字节，当byteread为-1时表示文件已经读完
	        while ((byteread = in.read(buffer)) != -1) {
	            // 将读取的字节写入输出流
	            out.write(buffer, 0, byteread);
	        }
	        System.out.println("复制单个文件" + srcFileName + "至" + destFileName
	                + "成功！");
			Reporter.log("复制单个文件" + srcFileName + "至" + destFileName
					+ "成功！");
	        return true;
	    } catch (Exception e) {
	        System.out.println("复制文件失败：" + e.getMessage());
			Reporter.log("复制文件失败：" + e.getMessage());
	        return false;
	    } finally {
	        // 关闭输入输出流，注意先关闭输出流，再关闭输入流
	        if (out != null) {
	            try {
	                out.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        if (in != null) {
	            try {
	                in.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	public  static void copyFile(File sourcefile,File targetFile) throws IOException{

		//新建文件输入流并对它进行缓冲
		FileInputStream input=new FileInputStream(sourcefile);
		BufferedInputStream inbuff=new BufferedInputStream(input);

		//新建文件输出流并对它进行缓冲
		FileOutputStream out=new FileOutputStream(targetFile);
		BufferedOutputStream outbuff=new BufferedOutputStream(out);

		//缓冲数组
		byte[] b=new byte[1024*5];
		int len=0;
		while((len=inbuff.read(b))!=-1){
			outbuff.write(b, 0, len);
		}

		//刷新此缓冲的输出流
		outbuff.flush();

		//关闭流
		inbuff.close();
		outbuff.close();
		out.close();
		input.close();


	}

	public  static void copyDirectiory(String sourceDir,String targetDir) throws IOException {

		//新建目标目录

		(new File(targetDir)).mkdirs();

		//获取源文件夹当下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();

		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				//源文件
				File sourceFile = file[i];
				//目标文件
				File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());

				copyFile(sourceFile, targetFile);

			}


			if (file[i].isDirectory()) {
				//准备复制的源文件夹
				String dir1 = sourceDir + file[i].getName();
				//准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();

				copyDirectiory(dir1, dir2);
			}
		}
	}

	// 校验文件
	public static boolean compareFile(String BaseFile, String ResultFile) throws IOException {
		boolean result = true;
		Reporter.log("Begin to checkResult-----!");
		BaseFile.replace("\\", "//");
		ResultFile.replace("\\", "//");
		File fileExpect = new File(BaseFile);
		if (fileExpect.exists())
		{
			InputStreamReader readExpect = new InputStreamReader(new FileInputStream(fileExpect), StandardCharsets.UTF_8);//考虑到编码格式
			BufferedReader brExpect = new BufferedReader(readExpect);

			File fileActual = new File(ResultFile);
			InputStreamReader readActual = new InputStreamReader(new FileInputStream(fileActual), StandardCharsets.UTF_8);//考虑到编码格式
			BufferedReader brActual = new BufferedReader(readActual);

			String lineTxtExpect = null;
			String lineTxtActual = null;
			int lineNumber = 1;
			int ErrorCount = 0;
			while ((lineTxtExpect = brExpect.readLine()) != null && (lineTxtActual = brActual.readLine()) != null) {
				if(lineTxtExpect.contains("\"now\":"))	continue;
				if (!lineTxtExpect.equals(lineTxtActual)) {
					if(lineNumber != 1) {                 // 第一行数据不校验
						ErrorCount++;
						if (ErrorCount < 30) {
							Reporter.log("第  " + lineNumber + " 行比对结果不一致！");
							Reporter.log("lineTxtExpect: " + lineTxtExpect );
							Reporter.log("lineTxtActual: " + lineTxtActual );
						}
					}
				}
				lineNumber++;
			}
			if (ErrorCount > 0) {
				if (ErrorCount > 30) {
					Reporter.log("超过30行结果不一致，不再导出日志！");
				}
				Assert.fail("共有--" + ErrorCount + "--行结果不一致！！");
				result = false;
			}
			Reporter.log("End checkResult-----!");
			return result;
		} else {
			Reporter.log("校验文件" + BaseFile + "不存在");
			System.out.println("校验文件" + BaseFile + "不存在");
			return false;
		}
	}

	//快速创建案例
	public static void createTests(String xmlFile, String caseFile, String method) throws Exception{
		File xml = new File(xmlFile);
		File cases = new File(caseFile);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(xml,true)));
		BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(cases)));
		String testName = br2.readLine();
		while (testName!=null){
			bw.write("<test name=\""+testName+"\" junit=\"false\" annotations=\"JDK\">");
			bw.newLine();
			bw.write("<parameter name=\"testName\" value=\""+testName+"\"/>");
			bw.newLine();
			bw.write("<classes>\n" +
					"            <class name=\"test."+method+"\"/>\n" +
					"        </classes>\n" +
					"    </test>");
			bw.newLine();
			bw.flush();
			testName = br2.readLine();
		}
		br2.close();
		bw.close();
	}

	public static void rebase(String dirName) throws Exception{
		File dir = new File("E:/testcase/"+dirName);
		if(dir.exists()){
			File[] files = dir.listFiles();
			for(int i=0;i<files.length;i++){
				String result = files[i].getAbsolutePath()+"\\result.txt";
				String baseline = files[i].getAbsolutePath()+"\\baseline.txt";
				copyFile(result,baseline,true);
			}
		}
	}

	public static void createSQL(String source, String target) throws Exception{
		File file = new File(target);
		if(!file.exists()){
			file.createNewFile();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(source)));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target,true)));
		String line = br.readLine();
		while(line!=null){
			if(line.startsWith("INSERT INTO `t_suggest_sku`") && line.contains("VALUES ('9109',")){
				bw.write(line+"\r\n");
			}
			line = br.readLine();
		}
		bw.flush();
		bw.close();
	}

	public static void main(String args[]) throws Exception{
//		createTests("D:\\workspace\\YH-API-Test\\cn\\com\\yonghui\\apitest\\src\\test-xml\\debug.xml","C:\\Users\\admin\\Desktop\\case.txt","restAPITest");
		createSQL("C:\\Users\\wing324\\Desktop\\工作相关\\北京到店数据.sql","C:\\Users\\wing324\\Desktop\\工作相关\\北京到店数据new.sql");
	}
}

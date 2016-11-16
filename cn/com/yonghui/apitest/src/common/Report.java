package common;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
 
public class Report implements IReporter{
    static String mySqlUrl=GlobalSettings.mySqlUrl;  
    static String mySqlUserName=GlobalSettings.mySqlUserName;    
    static String mySqlPassword=GlobalSettings.mySqlPassword;     
    String strBatchNo="";
    
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        List<ITestResult> list = new ArrayList<ITestResult>();
        for (ISuite suite : suites) {          
            Map<String, ISuiteResult> suiteResults = suite.getResults();
            for (ISuiteResult suiteResult : suiteResults.values()) {
                ITestContext testContext = suiteResult.getTestContext();           
                IResultMap passedTests = testContext.getPassedTests();
                IResultMap failedTests = testContext.getFailedTests();
                IResultMap skippedTests = testContext.getSkippedTests();
                IResultMap failedConfig = testContext.getFailedConfigurations();   
                list.addAll(this.listTestResult(passedTests));
                list.addAll(this.listTestResult(failedTests));
                list.addAll(this.listTestResult(skippedTests));
                list.addAll(this.listTestResult(failedConfig));
            }          
        }
        this.getBatchNo(list);
        this.sort(list);
        this.outputResult(list, outputDirectory+"/test.txt");
    }
     
    private ArrayList<ITestResult> listTestResult(IResultMap resultMap){
        Set<ITestResult> results = resultMap.getAllResults();    
        return new ArrayList<ITestResult>(results);
    }
    
    private void getBatchNo(List<ITestResult> list){
    	Connection conn=getConn();
    	Date now = new Date(); 
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
    	String strToday = dateFormat.format(now); 
    
    	  try {
    		  ResultSet rst=null;
    		  PreparedStatement prest = (PreparedStatement) conn.prepareStatement("select batch_no from test_log where project_name='"+GlobalSettings.projectName+GlobalSettings.bort+"' and module_name ='"+list.get(0).getMethod().getXmlTest().getSuite().getName()+"' and end_time>'"+strToday+"' order by end_time desc");
    		  rst=prest.executeQuery();
    		  if(rst.wasNull()){
    			  strBatchNo=strToday+"_1"; 
    		  }else{
    			  String lastBatch="";
    			  String blfg="false";
	    		  while(rst.next()){
	    			  lastBatch=rst.getString("batch_no");
	    			  blfg="true";
	    			  break;
	    		  }
	    		  if(blfg.equals("false") && lastBatch.length()==0){
	    			  strBatchNo=strToday+"_1";
	    		  }else{
	    			  if(lastBatch.startsWith("2")){
	    			  String[] arr=lastBatch.split("_");
	    			  Integer integer;
	    			  integer = Integer.valueOf(arr[1]);
	    			   int intNo=  integer.intValue();
	    			   strBatchNo=strToday+"_"+(intNo+1);
	    			  }else{
	    				  strBatchNo=strToday+"_1"; 
	    			  }
	    				  
	    		  }
	    		  rst.close();
    		  }
    	  }catch (Exception ex) {    
              ex.printStackTrace();    
          }finally{    
              close(conn);      
          }
    }
    
    private void sort(List<ITestResult> list){
        Collections.sort(list, new Comparator<ITestResult>() {
            @Override
            public int compare(ITestResult r1, ITestResult r2) {
                if(r1.getStartMillis()>r2.getStartMillis()){
                    return 1;
                }else{
                    return -1;
                }              
            }
        });
    }
     
    private void outputResult(List<ITestResult> list, String path){      
            Connection conn=getConn();
            
            	String strInsert="";
            	strInsert="insert into test_log(project_name,module_name,test_name,begin_time,end_time,last_time,status,description,batch_no) values(?,?,?,?,?,?,?,?,?)";
            	 
                try {          
                      PreparedStatement prest = (PreparedStatement) conn.prepareStatement(strInsert);          
   
                      for (ITestResult result : list) {
                         prest.setString(1, GlobalSettings.projectName+GlobalSettings.bort);
                         prest.setString(2, result.getMethod().getXmlTest().getSuite().getName());
                         prest.setString(3, result.getMethod().getXmlTest().getName());
                         Timestamp startTime=new Timestamp(result.getStartMillis());
                         Timestamp endTime=new Timestamp(result.getEndMillis());
                         prest.setTimestamp(4, startTime);
                         prest.setTimestamp(5, endTime);
                         Float tempLast=(float) (result.getEndMillis()-result.getStartMillis());
                         Float lastTime=tempLast/1000;
                         prest.setString(6,lastTime.toString()); 
                         prest.setString(7, this.getStatus(result.getStatus())); 
                         prest.setString(8, result.getMethod().getDescription());
                         prest.setString(9, strBatchNo); 
                         prest.execute();

                         conn.commit();  
 
                      }           
                } catch (Exception ex) {    
                    ex.printStackTrace();    
                }finally{    
                    close(conn);      
                }
    }
     
    private String getStatus(int status){
        String statusString = null;
        switch (status) {      
        case 1:
            statusString = "SUCCESS";
            break;
        case 2:
            statusString = "FAILURE";
            break;
        case 3:
            statusString = "SKIP";
            break;
        default:           
            break;
        }
        return statusString;
    }

    
    /** 
     * 创建连接 
     * @return 
     */  
    public static Connection getConn(){  
        try {          
                Class.forName("com.mysql.jdbc.Driver");          
                Connection conn = (Connection) DriverManager.getConnection(mySqlUrl, mySqlUserName, mySqlPassword);       
                conn.setAutoCommit(false);    
                return conn;  
        } catch (Exception ex) {    
            ex.printStackTrace();    
        }
        return null;  
    }  
    /** 
     * 关闭连接 
     * @return 
     */  
    public static void close(Connection conn){  
         try {    
             if(conn!=null){  
                 conn.close();    
             }  
         } catch (SQLException e) {    
             e.printStackTrace();    
         }  
    }  
 
}


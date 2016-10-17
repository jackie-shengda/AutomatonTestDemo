package jsonutility;

import com.google.gson.Gson;
import common.GlobalSettings;
import common.projectUtil;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class JsonDataProvider{

	public String jsonPath;
	String strTestsuite;
	String strTestcase;
	
    private static Logger logger = Logger.getLogger(JsonDataProvider.class.getName());
    public JsonDataProvider(String testsuite, String testcase){
    	
    	strTestsuite=testsuite;
    	strTestcase=testcase;
    	jsonPath=GlobalSettings.publicData+"/"+testsuite+"/"+testcase;
    }

    public Map<String,String> ConvertToMap(String strPath){
    	Map<String,Object> inputMap = new HashMap<String, Object>();
    	Map<String,String> tempMap=new HashMap<String, String>();
    	try
    	{
	    	Gson gson = new Gson();
	    	 File inputFile = new File(strPath);
	         if (inputFile.exists()){
	             String strInput = new String(Files.readAllBytes(Paths.get(strPath)), StandardCharsets.UTF_8);
	             	            
	             inputMap = gson.fromJson(strInput, Map.class);
	             inputMap.forEach((key,value)->{
	            	 if(value instanceof Map)
	            	 {
	            		 tempMap.put(key, gson.toJson(value));
	            	 }
	            	 else
	            	 {
	            		 if(value.toString().trim().startsWith("["))
	            		 {
	            			 tempMap.put(key, gson.toJson(value));
	            		 }
	            		 else
	            		 {
	            			 tempMap.put(key, value.toString());
	            		 }
	            		
	            	 }            		 
	             });
	             return  tempMap;
	         }
	         else {
	             System.out.println(strPath + "does not exist.");
	             //handle
	         }
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
		return tempMap;  	
    }
    
    public Set<String> getJsonKeys(String strFile,String ChildName){
    	Map<String,Map<String,Object>> tempMap = new HashMap<String, Map<String,Object>>();
    	File inputFile = new File(strFile);
    	Gson gson = new Gson();
    	try
    	{
	        if (inputFile.exists()){ 
	    	String strInput = new String(Files.readAllBytes(Paths.get(strFile)), StandardCharsets.UTF_8);       	            
	         tempMap = gson.fromJson(strInput, Map.class);
	         return tempMap.get(ChildName).keySet();
	        }
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
		return null;
    	
    }
    
    public String getJsonChild(String strFile,String ChildName){
    	String strReturn="";
    	Map<String,Object> tempMap = new HashMap<String, Object>();
    	Gson gson = new Gson();
    	try
    	{
   	 	File inputFile = new File(strFile);
        if (inputFile.exists()){
            String strInput = new String(Files.readAllBytes(Paths.get(strFile)), StandardCharsets.UTF_8);       	            
            tempMap = gson.fromJson(strInput, Map.class);
            strReturn=gson.toJson(tempMap.get(ChildName));
        }
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
		return strReturn;
    	
    }
    
    public void checkResult(){
    	try
    	{
    		projectUtil.log("begin checkResult");
	    	String strExpectWTG=getJsonChild(jsonPath+"/baseline.json","wtgGroupObject");
	    	String strActualWTG=getJsonChild(jsonPath+"/results_"+GlobalSettings.bort+".json","wtgGroupObject");
	    	JSONAssert.assertEquals(strExpectWTG, strActualWTG, true);

	    	String strExpectJS=getJsonChild(jsonPath+"/baseline.json","cmpCtlObject");
	    	String strActualJS=getJsonChild(jsonPath+"/results_"+GlobalSettings.bort+".json","cmpCtlObject");
	    	JSONAssert.assertEquals(strExpectJS, strActualJS, true);
	    	projectUtil.log("end checkResult");
    	}catch (Exception e) {
    		System.out.print(e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    public void checkResult2(){
    	try
    	{
    		projectUtil.log("begin checkResult");	
	    	String strExpectJS=getJsonChild(jsonPath+"/baseline.json","cmpCtlObject");
	    	String strActualJS=getJsonChild(jsonPath+"/results_"+GlobalSettings.bort+".json","cmpCtlObject");
	    	JSONAssert.assertEquals(strExpectJS, strActualJS, true);
	    	
	    	String strExpectWTG=getJsonChild(jsonPath+"/baseline.json","WTGs");
	    	String strActualWTG=getJsonChild(jsonPath+"/results_"+GlobalSettings.bort+".json","WTGs");
	    	Assert.assertEquals(strActualWTG, strExpectWTG);
	    	projectUtil.log("end checkResult");
    	}catch (Exception e) {
    		System.out.print(e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    public void checkResult(String strObjectName){
    	try
    	{
    		projectUtil.log("begin checkResult");
	    	String strExpect=getJsonChild(jsonPath+"/baseline.json",strObjectName);
	    	String strActual=getJsonChild(jsonPath+"/results_"+GlobalSettings.bort+".json",strObjectName);
	    	JSONAssert.assertEquals(strExpect, strActual, true);

    	}catch (Exception e) {
    		System.out.print(e.getMessage());
    		e.printStackTrace();
    	}
    }
    
    public void checkResult2(String strObjectName,String strObjectName2){
    	try
    	{
    		projectUtil.log("begin checkResult");
	    	String strExpect=getJsonChild(jsonPath+"/baseline.json",strObjectName);
	    	String strActual=getJsonChild(jsonPath+"/results_"+GlobalSettings.bort+".json",strObjectName);
	    	JSONAssert.assertEquals(strExpect, strActual, true);
	    	
	    	String strExpect2=getJsonChild(jsonPath+"/baseline.json","WTGs");
	    	String strActual2=getJsonChild(jsonPath+"/results_"+GlobalSettings.bort+".json",strObjectName2);
	    	JSONAssert.assertEquals(strExpect2, strActual2, true);
	    	projectUtil.log("end checkResult");

    	}catch (Exception e) {
    		System.out.print(e.getMessage());
    		e.printStackTrace();
    	}
    }
    public Iterator<Object[]> getData(){
    	List<Object[]> toIter = new ArrayList<Object[]>();
    	File f=new File(jsonPath+"/input"+GlobalSettings.bort+".json");
    	if(f.exists()){
    		Map<String,String> Input=ConvertToMap(jsonPath+"/input"+GlobalSettings.bort+".json");
    		toIter.add(new Object[] { Input } );
    	}else{
    		Map<String,String> Input=ConvertToMap(jsonPath+"/input.json");
    		toIter.add(new Object[] { Input } );
    	}
		projectUtil.log("begin to run "+strTestcase);
    	return  toIter.iterator(); 	
    }
}
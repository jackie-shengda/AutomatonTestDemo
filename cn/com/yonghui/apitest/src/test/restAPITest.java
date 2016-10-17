package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.FileOperateUtil;
import common.GlobalSettings;
import common.httpClient.HttpClientUtil;
import common.httpClient.ResponseInfo;
import common.projectUtil;
import jsonutility.JsonDataProvider;
import model.caseModel;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Jackie on 2016/10/8.
 */
public class restAPITest {
    String strModuleName = "";
    String strTestName = "";

    @Parameters({"testName", "ModuleName"})
    @BeforeClass
    public void setUpBeforeClass(String testName, String ModuleName) throws Exception {
        strTestName = testName;
        strModuleName = ModuleName;
        projectUtil.log("------------------------------------------ Start to run " + strTestName + "------------------------------------------------");
        projectUtil.log("********** 测试环境："+GlobalSettings.testEnv);
        projectUtil.delFolder("./results/" + strModuleName);
        projectUtil.createFolder("./results/" + strModuleName);
        projectUtil.createFolder("./results/" + strModuleName + "/" + strTestName);

    }

    @AfterClass
    public void tearDownAfterClass() throws Exception {
        projectUtil.log("------------------------------------------- Finish " + strTestName + "----------------------------------------------------");
    }

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @AfterMethod
    public void tearDown() throws Exception {
        Thread.sleep(2000);
    }

    @DataProvider(name = "dp")
    public Iterator<Object[]> dataFortestMethod(Method method) throws IOException {
        JsonDataProvider jdp = new JsonDataProvider(strModuleName, strTestName);
        return jdp.getData();
    }

    @Test(groups = {"restAPITest"}, dataProvider = "dp")
    public void t_restAPITest(Map<String, String> inputMap) throws IOException, JSONException {

        projectUtil.log("********** 案例描述："+inputMap.get("description"));
        //httpClient初始化
        HttpClientUtil httpClient = new HttpClientUtil();
        ResponseInfo responseInfo = null;

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser parser = new JsonParser();

        caseModel caseInfo = new caseModel();
        String caseDetails = new String(Files.readAllBytes(Paths.get(GlobalSettings.publicData + strModuleName + "/" + strTestName + "/input.json")), StandardCharsets.UTF_8);
        caseInfo = gson.fromJson(caseDetails,caseModel.class);
        List<caseModel.StepsBean> steps = caseInfo.getSteps();
        sort(steps);        //step排序

        for(int i=0;i<steps.size();i++) {
            caseModel.StepsBean step = steps.get(i);
            projectUtil.log("********** 开始步骤"+step.getStep()+": "+step.getDescription());
            projectUtil.log("Request method: "+step.getMethod());
            String url = step.getUrl();
            if (url.contains("apitest.yonghuivip.com")
                    ||url.contains("api-qa.yonghuivip.com")
                    ||url.contains("test.yonghuivip.com")) {
                url = url.replace("apitest.yonghuivip.com", "{host}").replace("api-qa.yonghuivip.com", "{host}").replace("test.yonghuivip.com", "{host}");
            }
            url = url.replace("{host}", GlobalSettings.testEnv);
            projectUtil.log("Request URL: "+url);
            switch (step.getMethod()){
                case "POST":
                    if (step.getParameterFilename()!=null) {
                        String requestMessage = new String(Files.readAllBytes(Paths.get(GlobalSettings.publicData + strModuleName + "/" + strTestName + "/"+step.getParameterFilename()+".json")), StandardCharsets.UTF_8);
                        projectUtil.log("RequestBody: "+requestMessage);
                        responseInfo = httpClient.executePost(url, requestMessage);
                    } else {
                        responseInfo = httpClient.executePost(url);
                    }
                    break;
                case "GET":
                    responseInfo = httpClient.executeGet(url);
                    break;
                case "DELETE":
                    responseInfo = httpClient.executeDelete(url);
                    break;
                default:
                    break;
            }

            //校验response状态
            if(responseInfo.status!=200){
                Assert.fail("返回状态："+responseInfo.status);
            }
            projectUtil.log("Response code: " + responseInfo.status);

            JsonObject json = parser.parse(responseInfo.content).getAsJsonObject();
            String result = gson.toJson(json);
            projectUtil.log("Response message:\r\n" + result);

            //结果写入和比对
            String basePath = GlobalSettings.publicData + strModuleName + "/" + strTestName + "/baseline/step_"+step.getStep()+".txt";
            String resultPath = GlobalSettings.publicData + strModuleName + "/" + strTestName + "/result/step_"+step.getStep()+".txt";
//
            File file = new File(resultPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            fw.write(result);
            fw.flush();
            fw.close();

            //rebase
            FileOperateUtil.copyFile(resultPath,basePath,true);

//            String expectValue = new String(Files.readAllBytes(Paths.get(basePath)), StandardCharsets.UTF_8);
//            String actualValue = new String(Files.readAllBytes(Paths.get(resultPath)), StandardCharsets.UTF_8);
            if(step.getIsCheckResult().equals("1")){
                if(!FileOperateUtil.compareFile(basePath, resultPath)){
                    Assert.fail();
                }
            }
//        JSONAssert.assertEquals(expectValue,actualValue,false);
        }

    }

    public static void sort(List<caseModel.StepsBean> stepsBeanList){
        Collections.sort(stepsBeanList, new Comparator<caseModel.StepsBean>() {
            @Override
            public int compare(caseModel.StepsBean o1, caseModel.StepsBean o2) {
                Integer a = Integer.parseInt(o1.getStep());
                Integer b = Integer.parseInt(o2.getStep());
                return a.compareTo(b);
            }
        });
    }
/*
    public static void setDescription(ITest annotation, Class testClass, Constructor testConstructor, Method testMethod, String desc){
        annotation.setDescription(desc);
    }*/
}

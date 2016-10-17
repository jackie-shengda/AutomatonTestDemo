package model;

import java.util.List;

/**
 * Created by Jackie on 2016/10/10.
 */
public class caseModel {

    /**
     * user : jackie
     * description :
     * steps : [{"step":"1","description":"","method":"GET","url":"","parameterFilename":"","isCheckResult":""}]
     */

    private String user;            //用户名
    private String description;     //案例描述
    /**
     * step : 1
     * description :
     * method : GET
     * url :
     * parameterFilename :
     * isCheckResult :
     */

    private List<StepsBean> steps;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<StepsBean> getSteps() {
        return steps;
    }

    public void setSteps(List<StepsBean> steps) {
        this.steps = steps;
    }

    public static class StepsBean {
        private String step;                //步骤顺序
        private String description;         //步骤描述
        private String method;              //请求方法
        private String url;                 //请求url
        private String parameterFilename;   //参数文件名称，程序默认在当前案例文件夹下查找
        private String isCheckResult;       //是否校验当前步骤结果

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getParameterFilename() {
            return parameterFilename;
        }

        public void setParameterFilename(String parameterFilename) {
            this.parameterFilename = parameterFilename;
        }

        public String getIsCheckResult() {
            return isCheckResult;
        }

        public void setIsCheckResult(String isCheckResult) {
            this.isCheckResult = isCheckResult;
        }
    }
}

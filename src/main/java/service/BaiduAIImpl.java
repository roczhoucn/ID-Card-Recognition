package service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.Img2Base64Util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ZhouPeng on 2018/11/29.
 */
public class BaiduAIImpl {

    public String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                + "grant_type=client_credentials"
                + "&client_id=" + ak
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            JSONObject jsonObject = JSON.parseObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

    public JSONObject getIdCardInfo(String pic_path, boolean isFront,String token) throws IOException {
        String api_path = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
        Img2Base64Util img2Base64Util = new Img2Base64Util();
        String imgStr = URLEncoder.encode(img2Base64Util.getImgStr(pic_path),"utf-8");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("detect_direction",true);
        params.put("id_card_side",isFront?"front":"back");
        params.put("image",imgStr);
        params.put("detect_risk",true);
//        String cardInfoStr = executePostByUsual(api_path,params,token);
        String cardInfoStr = "{\"direction\":0,\"log_id\":4171799747212420381,\"image_status\":\"normal\",\"words_result\":{\"住址\":{\"location\":{\"width\":1175,\"top\":2032,\"left\":622,\"height\":254},\"words\":\"山东省青岛市市南区贵州路69号3\"},\"出生\":{\"location\":{\"width\":941,\"top\":1787,\"left\":645,\"height\":113},\"words\":\"19811101\"},\"姓名\":{\"location\":{\"width\":410,\"top\":1306,\"left\":649,\"height\":133},\"words\":\"迟旭光\"},\"公民身份号码\":{\"location\":{\"width\":1561,\"top\":2617,\"left\":1099,\"height\":129},\"words\":\"37021419811101451X\"},\"性别\":{\"location\":{\"width\":86,\"top\":1561,\"left\":641,\"height\":100},\"words\":\"男\"},\"民族\":{\"location\":{\"width\":78,\"top\":1578,\"left\":1239,\"height\":90},\"words\":\"汉\"}},\"words_result_num\":6,\"risk_type\":\"normal\"}";
        System.out.println(cardInfoStr);
        JSONObject cardInfoJson = JSON.parseObject(cardInfoStr);
        return cardInfoJson;
    }

    public String executePostByUsual(String actionURL, HashMap<String, Object> parameters,String token) throws IOException {
        actionURL = actionURL + "?access_token=" + token;
        String response = "";
        try{
            URL url = new URL(actionURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //发送post请求需要下面两行
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");;
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求数据内容
            String requestContent = "";
            Set<String> keys = parameters.keySet();
            for(String key : keys){
                requestContent = requestContent + key + "=" + parameters.get(key) + "&";
            }
            requestContent = requestContent.substring(0, requestContent.lastIndexOf("&"));
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
            //使用write(requestContent.getBytes())是为了防止中文出现乱码
            ds.write(requestContent.getBytes());
            ds.flush();
            try{
                //获取URL的响应
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                String s = "";
                String temp = "";
                while((temp = reader.readLine()) != null){
                    s += temp;
                }
                response = s;
                reader.close();
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("No response get!!!");
            }
            ds.close();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Request failed!");
        }
        return response;
    }

    public HashMap parseCardInfo(JSONObject cardInfoJson){
        HashMap cardMapInfo = new HashMap();
        cardMapInfo.put("direction",cardInfoJson.getString("direction"));
        cardMapInfo.put("log_id",cardInfoJson.getString("log_id"));
        cardMapInfo.put("image_status",cardInfoJson.getString("image_status"));
        cardMapInfo.put("risk_type",cardInfoJson.getString("risk_type"));
        cardMapInfo.put("address",cardInfoJson.getJSONObject("words_result").getJSONObject("地址").getString("words"));
        cardMapInfo.put("birth",cardInfoJson.getJSONObject("words_result").getJSONObject("出生").getString("words"));
        cardMapInfo.put("name",cardInfoJson.getJSONObject("words_result").getJSONObject("姓名").getString("words"));
        cardMapInfo.put("id_no",cardInfoJson.getJSONObject("words_result").getJSONObject("公民身份号码").getString("words"));
        cardMapInfo.put("sex",cardInfoJson.getJSONObject("words_result").getJSONObject("性别").getString("words"));
        cardMapInfo.put("nation",cardInfoJson.getJSONObject("words_result").getJSONObject("民族").getString("words"));
        return cardMapInfo;
    }

}

import com.alibaba.fastjson.JSONObject;
import com.roczhou.Constants;
import com.roczhou.service.BaiduAIImpl;
import com.roczhou.service.DbOperation;
import com.roczhou.service.LocalOperation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ZhouPeng on 2018/11/29.
 * 批量读取文件夹中的身份证扫描件，
 * 使用百度AI 身份证识别接口读取身份证信息
 * 使用Druid连接池，将数据存入Mysql
 */
public class IDCardRecognition {

    public static void main(String[] args) throws IOException, SQLException {
        //本地文件操作相关业务实现类
        LocalOperation localOperation = new LocalOperation();
        //百度AI接口相关业务实现类
        BaiduAIImpl baiduAI = new BaiduAIImpl();
        //数据库操作相关接口实现类
        DbOperation dbOperation = new DbOperation();

        //image_names:获取源文件中文件的名称列表（相对路径）
        ArrayList<String> image_names;
        image_names = localOperation.readFolder(Constants.IMG_FOLDER_PATH);
        //使用应用的AK和SK 获取百度AI接口的AuthToken
        String baiduToken = baiduAI.getAuth(Constants.BAIDUAI_KEY,Constants.BAIDUAI_SECRET);
        for(String s :image_names){
            //使用百度AI接口获取身份证信息（百度返回的源格式）
            JSONObject cardInfo = baiduAI.getIdCardInfo(s,true,baiduToken);
            //提取身份证信息中的有效字段
            HashMap<String,String> cardInfoMap = baiduAI.parseCardInfo(cardInfo);
            //将读取出的身份证信息存入数据库
            dbOperation.saveCardInfo2DB(cardInfoMap);
        }
    }
}

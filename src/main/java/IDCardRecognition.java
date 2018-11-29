import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import service.BaiduAIImpl;
import service.DbOperation;
import service.LocalOperation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ZhouPeng on 2018/11/29.
 */
public class IDCardRecognition {
    public static final String IMG_FOLDER_PATH = ".\\card_imgs\\";
    public static final String BAIDUAI_KEY = "";
    public static final String BAIDUAI_SECRET = "";

    public static void main(String[] args) throws IOException, SQLException {
        LocalOperation localOperation = new LocalOperation();
        BaiduAIImpl baiduAI = new BaiduAIImpl();
        DbOperation dbOperation = new DbOperation();

        ArrayList<String> image_names;
        image_names = localOperation.readFolder(IMG_FOLDER_PATH);
        String baiduToken = baiduAI.getAuth(BAIDUAI_KEY,BAIDUAI_SECRET);
        for(String s :image_names){
            System.out.println(s);
            JSONObject cardInfo = baiduAI.getIdCardInfo(s,true,baiduToken);
            HashMap<String,String> cardInfoMap = baiduAI.parseCardInfo(cardInfo);
            dbOperation.saveCardInfo2DB(cardInfoMap);
        }
    }
}

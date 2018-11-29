import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import service.BaiduAIImpl;
import service.DbOperation;
import service.LocalOperation;

import java.util.ArrayList;

/**
 * Created by ZhouPeng on 2018/11/29.
 */
public class IDCardRecognition {
    public static final String IMG_FOLDER_PATH = ".\\card_imgs\\";
    public static final String BAIDUAI_KEY = "ayZ5QN4hACkm8u89pqF26enf";
    public static final String BAIDUAI_SECRET = "YNVzS9EVEIy7pDqt4sr3FnrG3dWFWDhQ";

    public static void main(String[] args){
        LocalOperation localOperation = new LocalOperation();
        BaiduAIImpl baiduAI = new BaiduAIImpl();
        DbOperation dbOperation = new DbOperation();

        ArrayList<String> image_names;
        image_names = localOperation.readFolder(IMG_FOLDER_PATH);

        for(String s :image_names){
            System.out.println(s);
            String token = baiduAI.getAuth(BAIDUAI_KEY,BAIDUAI_SECRET);

        }
    }
}

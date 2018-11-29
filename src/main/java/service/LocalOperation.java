package service;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ZhouPeng on 2018/11/29.
 */
public class LocalOperation {
    public ArrayList<String> readFolder(String folderPath) {
        ArrayList<String> filenames = new ArrayList<String>();
        File file = new File(folderPath);
        File[] array = file.listFiles();

        for (int i = 0; i < array.length; i++) {
            if (array[i].isDirectory()) {
                readFolder(array[i].toString());
            } else if (array[i].isFile()) {
                filenames.add(array[i].getPath());
            }
        }
        return filenames;
    }
}

package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Repository.*;

public class Stage implements Serializable {

    /**
     * filePath ->  blobPath
     */
    private Map<String, String> blobs = new TreeMap<>();

    public Map<String, String> getBlobs() {
        return blobs;
    }

    /**
     * add blob into object
     * @param blob
     */
    public void add(Blob blob) {
        File file = blob.getBlobSaveFileName();
        Utils.writeObject(file, blob);
        blobs.put(blob.getPath(), blob.getId());
    }

    /*
    * 删除blob文件，同时再这个stage中删除
    * */
    public void delete(Blob blob) {
        File file = blob.getBlobSaveFileName();
        file.delete();
        blobs.remove(blob.getPath());
    }

    public static void saveAddStage() {
        Utils.writeObject(ADDSTAGE, addStage);
    }

    public static void saveRemoveStage() {
        Utils.writeObject(REMOVESTAGE, removeStage);
    }

    public static void clearAddStage() {
        addStage = new Stage();
        saveAddStage();
    }

    public static void clearRemoveStage() {
        removeStage = new Stage();
        saveRemoveStage();
    }
}

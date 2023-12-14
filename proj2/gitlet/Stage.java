package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Repository.*;

public class Stage implements Serializable {

    /**
     * filePath ->  blobId
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
    * Delete the blob file and delete it again in the stage
    * */
    public void delete(Blob blob) {
        File file = blob.getBlobSaveFileName();
        file.delete();
        blobs.remove(blob.getPath());
    }

    public void delete(String path) {
        blobs.remove(path);
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

    public static void clearStage() {
        clearAddStage();
        clearRemoveStage();
    }

    public boolean keyExist(String key) {
        return blobs.containsKey(key);
    }

    public boolean valueExist(String value) {
        return blobs.containsValue(value);
    }
}

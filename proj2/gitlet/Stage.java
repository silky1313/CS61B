package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static gitlet.Repository.*;

public class Stage implements Serializable {

    /**
     * filePath ->  blobPath
     */
    private Map<String, String> blobs = new HashMap<>();

    public Map<String, String> getBlobs() {
        return blobs;
    }

    /**
     * add blob into object
     * @param blob
     */
    public void add(Blob blob) {
        File file = blob.getBlobSaveFileName();
        Utils.writeObject(file, blob.getBytes());
        blobs.put(blob.getPath(), blob.getId());
    }

    public void delete(Blob blob) {
        File file = blob.getBlobSaveFileName();
        file.delete();
        blobs.remove(blob.getPath());
    }

    public static void saveAddStage(Stage stage) {
        Utils.writeObject(ADDSTAGE, stage);
    }

    public static void saveRemoveStage(Stage stage) {
        Utils.writeObject(REMOVESTAGE, stage);
    }
}

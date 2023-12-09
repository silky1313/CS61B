package gitlet;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import static gitlet.Repository.OBJECTS;

public class Blob implements Serializable {
    private final String id;

    private final byte[] bytes;

    private final File fileName;

    private final String filePath;

    private final File blobSaveFileName;


    public Blob(File fileName) {
        this.fileName = fileName;
        this.bytes = readFile();
        this.filePath = fileName.getPath();
        this.id = generateID();
        this.blobSaveFileName = generateBlobSaveFileName();
    }

    public String getId() {
        return id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getPath() {
        return filePath;
    }

    public File getBlobSaveFileName() {
        return blobSaveFileName;
    }

    public File getFileName(){
        return fileName;
    }

    private byte[] readFile() {
        return Utils.readContents(fileName);
    }

    private String generateID() {
        return Utils.sha1(filePath, bytes);
    }


    private File generateBlobSaveFileName() {
        return Utils.join(OBJECTS, id);
    }

    /*
     * save blob into objects
     */
    public void save() {
        Utils.writeObject(blobSaveFileName, this);
    }

    /*
     *  get blob from object then store into cwd;
     */
    public void reSave() {
        Utils.writeContents(getFileName(), new String(bytes, StandardCharsets.UTF_8));
    }
}

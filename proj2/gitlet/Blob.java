package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Repository.OBJECTS;
import static gitlet.Utils.*;
import static gitlet.Utils.writeObject;

public class Blob implements Serializable {
    /**
     * hashI
     */
    private String id;

    /**
     * content
     */
    private byte[] bytes;

    private File fileName;

    private String filePath;

    private File blobSaveFileName;


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
        return readContents(fileName);
    }

    private String generateID() {
        return sha1(filePath, bytes);
    }


    private File generateBlobSaveFileName() {
        return join(OBJECTS, id);
    }

    public void save() {
        writeObject(blobSaveFileName, this);
    }


}

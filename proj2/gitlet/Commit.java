package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    /**
     * filePath ->  blobPath
     */
    private Map<String, String> blobs = new HashMap<>();

    private List<String> parents;

    private Date currentTime;

    private String id;

    private File commitSaveFileName;

    private String timeStamp;

    public String getMessage() {
        return message;
    }

    public Map<String, String> getBlobs() {
        return blobs;
    }

    public List<String> getParents() {
        return parents;
    }

    public String getId() {
        return id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
    /* TODO: fill in the rest of this class. */

    public Commit() {
        this.currentTime = new Date(0);
        this.timeStamp = dateToTimeStamp(currentTime);
        this.message = "initial commit";
        this.blobs = new HashMap<>();
        this.parents = new ArrayList<>();
        this.id = generateId();
        this.commitSaveFileName = Utils.join(Repository.OBJECTS, this.id);
    }

    public void save() {
        Utils.writeObject(commitSaveFileName, this);
    }
    
    public String generateId() {
        return Utils.sha1(getMessage(), getBlobs().toString(), getParents().toString(), getTimeStamp());
    }

    public static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }
}

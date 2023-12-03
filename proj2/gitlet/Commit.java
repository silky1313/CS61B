package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
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

    private Date date;

    private List<String> parents = new ArrayList<>();

    /**
     * key = file's name
     * value = sha1(name)
     */
    private Map<String, String> blobs = new HashMap<>();

    /**
     * just use this function when you gitlet commit
     */
    public Commit(String message, Date date) {
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getParents() {
        return parents;
    }

    public void setParents(List<String> parents) {
        this.parents = parents;
    }

    public Map<String, String> getBlobs() {
        return blobs;
    }

    public void setBlobs(Map<String, String> blobs) {
        this.blobs = blobs;
    }
}

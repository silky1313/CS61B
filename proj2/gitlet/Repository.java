package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.RepositoryUtils.*;
import static gitlet.Utils.*;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /*
     *   .gitlet
     *      |--objects
     *      |--refs
     *      |    |--heads
     *      |         |--master
     *      |--HEAD
     *		|--stage
     * 		|	 |--add-stage
     * 		|	 |--remove-stage
     */
    public static final File OBJECTS = join(GITLET_DIR, "objects");
    public static final File HEADS = join(GITLET_DIR, "refs", "heads");
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    public static final File ADDSTAGE = join(GITLET_DIR, "stage", "add-stage");
    public static final File REMOVESTAGE = join(GITLET_DIR, "stage", "remove-stage");

    public static Commit curCommit;
    public static Stage addStage;
    public static Stage removeStage;

    /* TODO: fill in the rest of this class. */
    public static void init() throws IOException {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        FileUtils.createDir(OBJECTS);
        FileUtils.createDir(HEADS);
        FileUtils.createFile(HEAD);
        FileUtils.createFile(ADDSTAGE);
        FileUtils.createFile(REMOVESTAGE);

        RepositoryUtils.initCommit();
        Utils.writeContents(HEAD, "master");
        Utils.writeContents(Utils.join(HEADS, "master"), curCommit.getId());
    }

    public static void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            exit("File does not exist.");
        }
        Blob blob = new Blob(file);

        removeStage = getRemoveStage();
        addStage = getAddStage();
        curCommit = getCurCommit();

        if (removeStage.getBlobs().containsValue(blob.getId())) {
            removeStage.delete(blob);
            Stage.saveRemoveStage(removeStage);
        }

        if (!curCommit.getBlobs().containsValue(blob.getId())) {
            if (addStage.getBlobs().containsValue(blob.getId())) {
                return;
            } else {
                addStage.delete(blob);
                addStage.add(blob);
                Stage.saveAddStage(addStage);
            }
        } else {
            if(addStage.getBlobs().containsValue(blob.getId())) {
                addStage.delete(blob);
                Stage.saveAddStage(addStage);
            }
        }
    }
}

package gitlet;

import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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

    /* TODO: fill in the rest of this class. */

    /**
     * 1. generate .gitlet dir
     * 2. generate an initial commit.
     * 3. create new branch master, point to this commit
     * 3. storage master in heads(local) repository,
     * 4. HEAD should point to this commit.
     * <p>
     * fail: if .gitlet exits,
     * "A Gitlet version-control system already exists in the current directory."
     */
    public static void init() throws IOException {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }

        initGitlet();

        String message = "initial commit";
        Date date = new Date(0);
        HashMap<String, String> hashMap = new HashMap<>();
        Commit initialCommit = new Commit(message, date);

        /*
         * 1. First generate a hash value for this commit
         * 2. And serialize and save it into blobs, the file name is the hash value
         * 3. Then save this hash value in the master
         * 4. Save this hash value in head
         */
        String id = generateID(initialCommit);

        File commitFile = createFileUseSuffix("objects", "blobs", id);
        writeObject(commitFile, initialCommit);

        File masterFile = createFileUseSuffix("refs", "heads", "master");
        writeContents(masterFile, id);

        File HEADFile = createFileUseSuffix("HEAD");
        writeContents(HEADFile, id);
    }

    /**
     * .gitlet
     * |--objects
     * |    |-- blobs
     * |--refs
     * |    |--heads
     * |         |--master
     * |--HEAD
     * |--stage
     * |	 |--addstage
     * |	 |--removestage
     */
    private static void initGitlet() throws IOException {
        createDirUseSuffix("objects", "blobs");
        createFileUseSuffix("refs", "heads", "master");
        createFileUseSuffix("HEAD");
        createFileUseSuffix("stage", "addstage");
        createFileUseSuffix("stage", "removestage");
    }

    private static File createDirUseSuffix(String... agres) throws IOException {
        File objects = Utils.join(GITLET_DIR.getPath(), agres);
        if (objects.exists()) {
            return objects;
        }
        return Utils.createDir(objects);
    }

    private static File createFileUseSuffix(String... agres) throws IOException {
        File objects = Utils.join(GITLET_DIR.getPath(), agres);
        if (objects.exists()) {
            return objects;
        }
        return Utils.createFile(objects);
    }

    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    private static String generateID(Commit initialCommit) {
        return Utils.sha1(dateToTimeStamp(initialCommit.getDate()), initialCommit.getMessage(), initialCommit.getParents().toString(),
                initialCommit.getBlobs().toString());
    }
}

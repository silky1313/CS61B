package gitlet;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gitlet.Repository.*;
import static gitlet.Stage.*;
import static gitlet.Utils.*;

public class RepositoryUtils {
    /**
     * init commit
     */
    public static void initCommit() {
        Commit commit = new Commit();
        curCommit = commit;
        commit.save();
    }

    public static void exit(String msg) {
        System.out.println(msg);
        System.exit(0);
    }

    public static Commit getCurCommit() {
        String curCommitName = Utils.readContentsAsString(HEAD);
        String id = Utils.readContentsAsString(Utils.join(HEADS, curCommitName));
        return Utils.readObject(Utils.join(OBJECTS, id), Commit.class);
    }

    public static Stage getRemoveStage() {
        if (REMOVESTAGE.length() == 0) {
            return new Stage();
        }
        return Utils.readObject(REMOVESTAGE, Stage.class);
    }

    public static Stage getAddStage() {
        if (ADDSTAGE.length() == 0) {
            return new Stage();
        }
        return Utils.readObject(ADDSTAGE, Stage.class);
    }

    public static boolean checkNullStage() {
        return addStage.getBlobs().isEmpty() && removeStage.getBlobs().isEmpty();
    }

    public static void createNewCommit(String message) {
        Map<String, String> addStageBlobs = addStage.getBlobs();
        Map<String, String> removeStageBlobs = removeStage.getBlobs();
        Map<String, String> curCommitBlobs = curCommit.getBlobs();
        Map<String, String> newCommitBlobs = new HashMap<>();
        List<String> newCommitParents = new ArrayList<>();

        //需要对这三个map进行合并，并用一个新的map存储。
        for (String filePath : curCommitBlobs.keySet()) {
            newCommitBlobs.put(filePath, curCommitBlobs.get(filePath));
        }
        for (String filePath : addStageBlobs.keySet()) {
            newCommitBlobs.put(filePath, addStageBlobs.get(filePath));
        }
        for (String filePath : removeStageBlobs.keySet()) {
            newCommitBlobs.remove(filePath);
        }

        //再获取他的parents
        newCommitParents.add(curCommit.getId());

        //然后就是创建新的commit
        Commit newCommit = new Commit(message, newCommitBlobs, newCommitParents);
        newCommit.save();
        writeBranchHead("master", newCommit.getId());

        //最后记得清楚缓存区
        clearAddStage();
        clearRemoveStage();
    }

    public static void writeBranchHead(String branch, String id) {
        Utils.writeContents(Utils.join(HEADS, branch), id);
    }

    public static void getListLog(Commit commit) {
        printCommit(commit);

        if (commit.getParents().isEmpty()) {
            return;
        }

        File file = Utils.join(OBJECTS, commit.getParents().get(0));
        getListLog(readObject(file, Commit.class));
    }

    public static void printCommit(Commit commit) {
        System.out.println("===");
        System.out.println("commit " + commit.getId());

        if (commit.getParents().size() == 2) {
            System.out.println("Merge: ");
            for (String i : commit.getParents()) {
                System.out.print(i.substring(0, 5));
            }
            System.out.println();
        }

        System.out.println("Date:" + commit.getTimeStamp());
        System.out.println(commit.getMessage());
        System.out.println();
    }
}

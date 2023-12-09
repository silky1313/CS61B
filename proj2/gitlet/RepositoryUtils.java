package gitlet;


import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gitlet.FileUtils.*;
import static gitlet.Repository.*;
import static gitlet.Stage.*;
import static gitlet.Utils.*;

public class RepositoryUtils {
    /**
     * init commit
     */
    public static void initCommit() {
        Commit commit = new Commit();
        Repository.curCommit = commit;
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
        String nowBranch = getCurBranch();
        writeBranchHead(nowBranch, newCommit.getId());

        //最后记得清除缓存区
        clearStage();
    }

    static String getCurBranch() {
        return Utils.readContentsAsString(HEAD);
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

    public static void searchAllBranch() {
        String HEADBranch = readContentsAsString(HEAD);
        System.out.println("=== Branches ===");
        System.out.println("*" + HEADBranch);

        List<String> branches = getAllBranches();
        for (String i : branches) {
            if(i.equals(HEADBranch)) continue;
            System.out.println(i);
        }

        System.out.println();
    }

    public static List<Commit> getAllCommit() {
        List<String> list = plainFilenamesIn(OBJECTS);
        List<Commit> result = new ArrayList<>();
        assert list != null;
        for (String i : list) {
            File file = join(OBJECTS, i);
            try {
                Commit commit = readObject(file, Commit.class);
                result.add(commit);
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    public static List<String> getAllBranches() {
        return plainFilenamesIn(HEADS);
    }

    public static void searchStage() {
        addStage = getAddStage();
        removeStage = getRemoveStage();

        System.out.println("=== Staged Files ===");
        for (String i : addStage.getBlobs().keySet()) {
            File file = new File(i);
            System.out.println(file.getName());
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for (String i : removeStage.getBlobs().keySet()) {
            File file = new File(i);
            System.out.println(file.getName());
        }
        System.out.println();
    }

    /**
     * TODO:extra point
     */
    public static void searchModificationsNotStaged() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
    }

    /**
     * TODO:extra point
     */
    public static void searchUntracked() {
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public static Blob getBlobByID(String id) {
        File BLOB_FILE = join(OBJECTS, id);
        return readObject(BLOB_FILE, Blob.class);
    }

    public static void checkOut(Commit commit, String fileName) {
        File file = join(CWD, fileName);
        String filePath = file.getPath();

        if (commit.keyExist(filePath)) {
            File blobFile = join(OBJECTS, commit.getBlobs().get(filePath));
            Blob blob = readObject(blobFile, Blob.class);
            blob.reSave();
        } else {
            System.out.println("File does not exist in that commit.");
        }
    }

    public static void checkBranchNameExist(String branchName) {
        List<String> allBranch = getAllBranches();
        if (!allBranch.contains(branchName) ) {
            exit("No such branch exists.");
        }
    }

    public static void checkBranchIsCurCommit(String branchName) {
        String curBranch = getCurBranch();
        if (curBranch.equals(branchName)) {
            exit("No need to checkout the current branch.");
        }
    }

    public static Commit getCommitByBranchName(String branchName) {
        File branchFile = join(HEADS, branchName);
        String commitId = readContentsAsString(branchFile);
        File commitFile = join(OBJECTS, commitId);

        return readObject(commitFile, Commit.class);
    }

    /*从curCommit切换到newCommit*/
    public static void changeCommit(Commit newCommit) {
        List<String> onlyCurCommitTracked = findOnlyCurCommitTracked(newCommit);
        List<String> bothCommitTracked = findBothCommitTracked(newCommit);
        List<String> onlyNewCommitTracked = findOnlyNewCommitTracked(newCommit);
        deleteFiles(onlyCurCommitTracked, CWD);
        overwriteFiles(bothCommitTracked, newCommit);
        writeFiles(onlyNewCommitTracked, newCommit);
        clearStage();
    }

    public static void changeBranch(String branchName) {
        writeContents(HEAD, branchName);
    }

    /*找到仅在curCommit中跟踪的文件*/
    public static List<String> findOnlyCurCommitTracked(Commit newCommit) {
        List<String> curFileName = curCommit.getFileNames();
        List<String> newFileName = newCommit.getFileNames();

        for (String i : newFileName) {
            curFileName.remove(i);
        }

        return curFileName;
    }

    /*找到在两个commit的共同追踪的文件*/
    public static List<String> findBothCommitTracked(Commit newCommit) {
        List<String> curFileName = curCommit.getFileNames();
        List<String> newFileName = newCommit.getFileNames();
        List<String> result = new ArrayList<>();

        for (String i : curFileName) {
            if(newFileName.contains(i)) {
                result.add(i);
            }
        }

        return result;
    }

    public static List<String> findOnlyNewCommitTracked(Commit newCommit) {
        List<String> curFileName = curCommit.getFileNames();
        List<String> newFileName = newCommit.getFileNames();

        for (String i : curFileName) {
            newFileName.remove(i);
        }

        return newFileName;
    }

    /*需要将newCommit中文件覆盖oldFile中所有文件*/
    public static void overwriteFiles(List<String>oldFileNames, Commit newCommit) {
        for (String i : oldFileNames) {
            Blob blob =  newCommit.getBlobByFileName(i);
            blob.reSave();
        }
    }

    /**/
    private static void writeFiles(List<String> onlyNewCommitTracked, Commit newCommit) {
        for (String fileName : onlyNewCommitTracked) {
            File file = join(CWD, fileName);
            if (file.exists()) {
                exit("There is an untracked file in the way; delete it, or add and commit it first.");
            }
        }

        overwriteFiles(onlyNewCommitTracked, newCommit);
    }

    public static void checkCommitExits(String commitId) {
        List<Commit> commits = getAllCommit();
        for (Commit i : commits) {
            if (i.getId().equals(commitId)) {
               return;
            }
        }
        exit("No commit with that id exists.");

    }

    public static Commit getCommitByCommitId(String id) {
        File file = join(OBJECTS, id);
        return Utils.readObject(file, Commit.class);
    }

    public static void changeBranchHead(String id) {
        File file = Utils.join(HEADS, curBranch);

        Utils.writeContents(file, id);
    }
}

package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static gitlet.FileUtils.*;
import static gitlet.RepositoryUtils.*;
import static gitlet.Stage.*;
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
    public static String curBranch;

    /* TODO: fill in the rest of this class. */
    public static void init() throws IOException {
        if (GITLET_DIR.exists()) {
            exit("A Gitlet version-control system already exists in the current directory.");
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

        restoreState();

        if (removeStage.valueExist(blob.getId())) {
            removeStage.delete(blob);
            saveRemoveStage();
        }

        if (!curCommit.valueExist(blob.getId())) {
            if (addStage.valueExist(blob.getId())) {
                return;
            } else {
                blob.save();
                if (addStage.keyExist(blob.getPath())) {
                    addStage.delete(blob);
                }
                addStage.add(blob);
                saveAddStage();
            }
        } else {
            if (addStage.valueExist(blob.getId())) {
                addStage.delete(blob);
                saveAddStage();
            }
        }
    }

    /**
     * commit命令就是将父commit中tracker的file继续跟踪，然后再整合stage的tracked file。
     * 1.我需要先获取add-stage， remove-stage
     * 2.然后判断是否再父分支的基础上做了修改
     * 3.然后就是合并问题了。
     * 4.最后记得给commit加parents
     *
     * @param message
     */
    public static void commit(String message) {
        if (message.isEmpty()) {
            exit("Please enter a commit message.");
        }

        restoreState();

        if (checkNullStage()) {
            exit("No changes added to the commit.");
        }

        createNewCommit(message);
    }

    /*
    1. 如果文件保存再add区域，直接删除这个blob，同时从add区域删除
    2. 如果这个文件再curCommit中，需要删除对这个文件的追踪，但是不能删除blob，因为可能这个blob是共享的
     */
    public static void rm(String fileName) {
        restoreState();

        File file = join(CWD, fileName);
        Blob blob = new Blob(file);

        if (addStage.valueExist(blob.getId())) {
            addStage.delete(blob);
            saveAddStage();
        } else if (curCommit.keyExist(blob.getPath())) {
            removeStage.add(blob);
            deleteFile(blob.getFileName());
            saveRemoveStage();
        } else {
            exit("No reason to remove the file.");
        }
    }

    /*
     * 1.首先是从下往上
     * 2.只展示第一个parents，
     * 3.对于merge，记得展示merge的两个id
     * */
    public static void log() {
        curCommit = getCurCommit();
        getListLog(curCommit);
    }

    public static void globalLog() {
        List<Commit> commits = getAllCommit();
        for(Commit i : commits) {
            printCommit(i);
        }
    }

    public static void find(String message) {
        List<Commit> commits = getAllCommit();
        for(Commit i : commits) {
            if (i.getMessage().equals(message)) {
                System.out.println(i.getId());
            }
        }
    }

    public static void status() {
        searchAllBranch();
        searchStage();
        searchModificationsNotStaged();
        searchUntracked();
    }

    /*
     * 该方法需要将curCommit的file放到工作区，
     * 但是不添加到addStage
     */
    public static void checkOutOnCurCommit(String fileName) {
        curCommit = getCurCommit();
        checkOut(curCommit, fileName);
    }


    /*将当给定分支名id,将这个commit内的file添加到工作区，同时需要不需要添加到addStage*/
    public static void CheckOutFromOtherCommit(String commitId, String fileName) {
        List<Commit> all = getAllCommit();
        for (Commit i : all) {
            if (i.getId().equals(commitId)) {
                checkOut(i, fileName);
                return;
            }
        }
        System.out.println("No commit with that id exists.");
    }

    /*切换到branchName，同时将head文件内的当前分支设置这个
    * 同时还需要将当前分支内追踪的文件但是在checkout分支没有追踪的文件删除
    * 最后清除stage，如果当前分支和checkout分支是用一个就不用清除*/
    public static void checkOutFromBranch(String branchName) {
        checkBranchNameExist(branchName);
        checkBranchIsCurCommit(branchName);

        curCommit = getCurCommit();
        Commit newCommit = getCommitByBranchName(branchName);

        changeCommit(newCommit);
        changeBranch(branchName);
    }

    /*创建一个分支，然后指向当前的commit,但是不会切换到这个分支上*/
    public static void addNewBranch(String newBranch) {
        List<String> branches = getAllBranches();
        if (branches.contains(newBranch)) {
            exit("A branch with that name already exists.");
        }

        File file = Utils.join(HEADS, newBranch);
        curCommit = getCurCommit();
        writeContents(file, curCommit.getId());
    }

    public static void restoreState() {
        removeStage = getRemoveStage();
        addStage = getAddStage();
        curCommit = getCurCommit();
    }

    public static void removeBranch(String branchName) {
        List<String> branches = getAllBranches();
        curBranch = getCurBranch();

        if(curBranch.equals(branchName)) {
            exit("Cannot remove the current branch.");
        }

        if (branches.contains(branchName)) {
            File file = Utils.join(HEADS, branchName);
            Utils.restrictedDelete(file);
        } else{
            exit("A branch with that name does not exist.");
        }
    }
    /*和checkout切换到某个分支是差不多的
    * 1.先判断commitId是否存在
    * 2.然后切换到该commit，在checkout中已经实现过了
    * 3.然后将当前branch的commitId设置为这个id*/
    public static void reset(String commitId) {
        checkCommitExits(commitId);
        curCommit = getCurCommit();
        curBranch = getCurBranch();

        Commit commit = getCommitByCommitId(commitId);

        changeCommit(commit);
        changeBranchHead(commitId);
    }
}

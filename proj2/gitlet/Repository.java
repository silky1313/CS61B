package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gitlet.FileUtils.*;
import static gitlet.RepositoryUtils.*;
import static gitlet.Stage.*;
import static gitlet.Utils.*;

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
    public static void init() {
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
     * The commit command is to continue tracking the file of the tracker in the parent commit, and then integrate the tracked file of the stage.
     * 1.I need to get add-stage, remove-stage first
     * 2. Then determine whether the changes have been made on the basis of the parent branch
     * 3. Then there's the issue of merging.
     * 4. Finally, remember to add parents to commit
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
        1. If the file is saved in the add area, delete the blob directly and delete it from the add area
        2. If the file is in curCommit, you need to delete the tracking of the file, but you can't delete the blob, because the blob may be shared
         */
    public static void rm(String fileName) {
        restoreState();

        File file = join(CWD, fileName);
        String filePath = file.getPath();

        if (addStage.keyExist(filePath)) {
            addStage.delete(filePath);
            saveAddStage();
        } else if (curCommit.keyExist(filePath)) {
            Blob blob = getBlobFromCurrCommitByPath(filePath, curCommit);
            removeStage.add(blob);
            deleteFile(blob.getFileName());
            saveRemoveStage();
        } else {
            exit("No reason to remove the file.");
        }
    }

    /*
     * 1. First of all, from the bottom up
     * 2.Show only the first parent,
     *3. For Merge, remember to show the two IDs of Merge
     * */
    public static void log() {
        curCommit = getCurCommit();
        getListLog(curCommit);
    }

    public static void globalLog() {
        List<Commit> commits = getAllCommit();
        for (Commit i : commits) {
            printCommit(i);
        }
    }

    public static void find(String message) {
        List<Commit> commits = getAllCommit();
        boolean flag = false;
        for (Commit i : commits) {
            if (i.getMessage().equals(message)) {
                System.out.println(i.getId());
                flag = true;
            }
        }
        if (!flag) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void status() {
        searchAllBranch();
        searchStage();
        searchModificationsNotStaged();
        searchUntracked();
    }

    /*
     * This method needs to put the file of curCommit into the workspace,
     * However, it is not added to addStage
     */
    public static void checkOutOnCurCommit(String fileName) {
        curCommit = getCurCommit();
        checkOut(curCommit, fileName);
    }

    /* will add the file in this commit to the workspace when the branch name id is given, and it needs to be added to addStage*/
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

    /* Switch to branchName, and set the current branch in the head file to this
     * It is also necessary to delete the files that are tracked in the current branch but are not tracked in the checkout branch
     * Clear stage last, don't clear if the current branch and checkout branch are one*/
    public static void checkOutFromBranch(String branchName) {
        checkBranchNameExist(branchName);
        checkBranchIsCurCommit(branchName);

        curCommit = getCurCommit();
        Commit newCommit = getCommitByBranchName(branchName);

        changeCommit(newCommit);
        changeBranch(branchName);
    }

    /* creates a branch and points to the current commit, but does not switch to that branch */
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
        curBranch = getCurBranch();
    }

    public static void removeBranch(String branchName) {
        List<String> branches = getAllBranches();
        curBranch = getCurBranch();

        if (curBranch.equals(branchName)) {
            exit("Cannot remove the current branch.");
        }

        if (branches.contains(branchName)) {
            File file = Utils.join(HEADS, branchName);
            FileUtils.deleteFile(file);
        } else {
            exit("A branch with that name does not exist.");
        }
    }

    /* is pretty much the same as switching to a branch with checkout
     * 1.Check whether the commitId exists
     *2. Then switch to that commit, which has already been implemented in checkout
     *3. Then set the commitId of the current branch to this id */
    public static void reset(String commitId) {
        checkCommitExits(commitId);
        curCommit = getCurCommit();
        curBranch = getCurBranch();

        Commit commit = getCommitByCommitId(commitId);

        changeCommit(commit);
        changeBranchHead(commitId);
    }

    public static void merge(String mergeBranch) {
        restoreState();
        checkIfStageEmpty();
        checkIfBranchExists(mergeBranch);
        checkIfBranchIsCurBranch(mergeBranch);

        Commit mergeCommit = getCommitByBranchName(mergeBranch);
        Commit splitPoint = findSplitPoint(curCommit, mergeCommit);
        checkIfSplitPintIsGivenBranch(splitPoint, mergeCommit);
        checkIfSplitPintIsCurrBranch(splitPoint, mergeBranch);

        Map<String, String> currCommitBlobs = curCommit.getBlobs();
        String message = "Merged " + mergeBranch + " into " + curBranch + ".";
        String currBranchCommitID = getCommitByBranchName(curBranch).getId();
        String mergeBranchCommitID = getCommitByBranchName(mergeBranch).getId();
        List<String> parents = new ArrayList<>(List.of(currBranchCommitID, mergeBranchCommitID));
        Commit newCommit = new Commit(message, currCommitBlobs, parents);

        Commit mergedCommit = mergeFilesToNewCommit(splitPoint, newCommit, mergeCommit);

        mergedCommit.save();
        clearStage();
        writeBranchHead(curBranch, mergedCommit.getId());
    }
}
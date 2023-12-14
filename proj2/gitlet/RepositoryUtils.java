package gitlet;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

        for (String filePath : curCommitBlobs.keySet()) {
            newCommitBlobs.put(filePath, curCommitBlobs.get(filePath));
        }
        for (String filePath : addStageBlobs.keySet()) {
            newCommitBlobs.put(filePath, addStageBlobs.get(filePath));
        }
        for (String filePath : removeStageBlobs.keySet()) {
            newCommitBlobs.remove(filePath);
        }

        newCommitParents.add(curCommit.getId());

        Commit newCommit = new Commit(message, newCommitBlobs, newCommitParents);
        newCommit.save();
        String nowBranch = getCurBranch();
        writeBranchHead(nowBranch, newCommit.getId());

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

        System.out.println("Date: " + commit.getTimeStamp());
        System.out.println(commit.getMessage());
        System.out.println();
    }

    public static Blob getBlobFromCurrCommitByPath(String filePath, Commit currCommmit) {
        String blobID = currCommmit.getBlobs().get(filePath);
        return getBlobByID(blobID);
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

    /*Switch from cur commit to new commit*/
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

    /*Find files that are only tracked in cur commit*/
    public static List<String> findOnlyCurCommitTracked(Commit newCommit) {
        List<String> curFileName = curCommit.getFileNames();
        List<String> newFileName = newCommit.getFileNames();

        for (String i : newFileName) {
            curFileName.remove(i);
        }

        return curFileName;
    }

    /*Find the files that are tracked in both commits*/
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

    /*Need to overwrite all files in old File with new Commit Chinese */
    public static void overwriteFiles(List<String>oldFileNames, Commit newCommit) {
        for (String i : oldFileNames) {
            Blob blob =  newCommit.getBlobByFileName(i);
            blob.reSave();
        }
    }

    /**/
    private static void writeFiles(List<String> onlyNewCommitTracked, Commit newCommit) {
        if(onlyNewCommitTracked.isEmpty()) {
            return;
        }

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

    public static void checkIfStageEmpty() {
        if(addStage.getBlobs() == null && removeStage.getBlobs() == null) {
            exit("You have uncommitted changes.");
        }
    }

    public static void checkIfBranchExists(String mergeBranch) {
        List<String> branches = getAllBranches();
        if (!branches.contains(mergeBranch)) {
            exit("A branch with that name does not exist.");
        }
    }

    public static void checkIfBranchIsCurBranch(String mergeBranch) {
        if (mergeBranch.equals(curBranch)) {
            exit("Cannot merge a branch with itself.");
        }
    }

    public static Commit findSplitPoint(Commit commit1, Commit commit2) {
        Map<String, Integer> commit1IDToLength = caculateCommitMap(commit1, 0);
        Map<String, Integer> commit2IDToLength = caculateCommitMap(commit2, 0);
        return caculateSplitPoint(commit1IDToLength, commit2IDToLength);
    }

    private static Map<String, Integer> caculateCommitMap(Commit commit, int deep) {
        Map<String, Integer> map = new HashMap<>();
        map.put(commit.getId(), deep);

        if (commit.getParents().isEmpty()) {
            return map;
        }

        for (String id : commit.getParents()) {
            Commit parent = getCommitByCommitId(id);
            map.putAll(caculateCommitMap(parent, deep));
        }
        return map;
    }

    private static Commit caculateSplitPoint(Map<String, Integer> map1, Map<String, Integer> map2) {
        int minLength = Integer.MAX_VALUE;
        String minID = "";
        for (String id : map1.keySet()) {
            if (map2.containsKey(id) && map2.get(id) < minLength) {
                minID = id;
                minLength = map2.get(id);
            }
        }
        return getCommitByCommitId(minID);
    }

    public  static void checkIfSplitPintIsGivenBranch(Commit splitPoint, Commit mergeCommit) {
        if (splitPoint.getId().equals(mergeCommit.getId())) {
            exit("Given branch is an ancestor of the current branch.");
        }
    }

    public static void checkIfSplitPintIsCurrBranch(Commit splitPoint, String mergeBranch) {
        if (splitPoint.getId().equals(curCommit.getId())) {
            System.out.println("Current branch fast-forwarded.");
            checkOutFromBranch(mergeBranch);
        }
    }

    public static Commit mergeFilesToNewCommit(Commit splitPoint, Commit newCommit, Commit mergeCommit) {

        List<String> allFiles = caculateAllFiles(splitPoint, newCommit, mergeCommit);

        /*
         * case 1 5 6: write mergeCommit files into newCommit
         * case 1: overwrite files
         * case 5: write files
         * case 6: delete files
         */
        List<String> overwriteFiles = caculateOverwriteFiles(allFiles, splitPoint, newCommit, mergeCommit);
        List<String> writeFiles = caculateWriteFiles(allFiles, splitPoint, newCommit, mergeCommit);
        List<String> deleteFiles = caculateDeleteFiles(allFiles, splitPoint, newCommit, mergeCommit);

        overwriteFiles(changeBlobIDListToFileNameList(overwriteFiles), mergeCommit);
        writeFiles(changeBlobIDListToFileNameList(writeFiles), mergeCommit);
        deleteFiles(changeBlobIDListToFileNameList(deleteFiles), CWD);

        /* * case 3-2: deal conflict */
        checkIfConflict(allFiles, splitPoint, newCommit, mergeCommit);

        return caculateMergedCommit(newCommit, overwriteFiles, writeFiles, deleteFiles);
    }

    private static List<String> caculateAllFiles(Commit splitPoint, Commit newCommit, Commit mergeCommit) {
        List<String> allFiles = new ArrayList<>(splitPoint.getBlobIdList());

        allFiles.addAll(newCommit.getBlobIdList());
        allFiles.addAll(mergeCommit.getBlobIdList());
        Set<String> set = new HashSet<>(allFiles);
        allFiles.clear();
        allFiles.addAll(set);
        return allFiles;
    }

    private static List<String> caculateOverwriteFiles(List<String> allFiles, Commit splitPoint, Commit
            newCommit, Commit mergeCommit) {

        Map<String, String> splitPointMap = splitPoint.getBlobs();
        Map<String, String> newCommitMap = newCommit.getBlobs();
        Map<String, String> mergeCommitMap = mergeCommit.getBlobs();
        List<String> overwriteFiles = new ArrayList<>();

        for (String path : splitPointMap.keySet()) {
            if (newCommitMap.containsKey(path) && mergeCommitMap.containsKey(path)) {
                if ((splitPointMap.get(path).equals(newCommitMap.get(path))) && (!splitPointMap.get(path).equals(mergeCommitMap.get(path)))) {
                    overwriteFiles.add(mergeCommitMap.get(path));
                }
            }
        }
        return overwriteFiles;
    }


    private static List<String> caculateWriteFiles(List<String> allFiles, Commit splitPoint, Commit
            newCommit, Commit mergeCommit) {
        Map<String, String> splitPointMap = splitPoint.getBlobs();
        Map<String, String> newCommitMap = newCommit.getBlobs();
        Map<String, String> mergeCommitMap = mergeCommit.getBlobs();
        List<String> writeFiles = new ArrayList<>();
        for (String path : mergeCommitMap.keySet()) {
            if ((!splitPointMap.containsKey(path)) && (!newCommitMap.containsKey(path))) {
                writeFiles.add(mergeCommitMap.get(path));
            }
        }
        return writeFiles;
    }

    private static List<String> caculateDeleteFiles(List<String> allFiles, Commit splitPoint, Commit
            newCommit, Commit mergeCommit) {
        Map<String, String> splitPointMap = splitPoint.getBlobs();
        Map<String, String> newCommitMap = newCommit.getBlobs();
        Map<String, String> mergeCommitMap = mergeCommit.getBlobs();
        List<String> deleteFiles = new ArrayList<>();
        for (String path : splitPointMap.keySet()) {
            if (newCommitMap.containsKey(path) && (!mergeCommitMap.containsKey(path))) {
                deleteFiles.add(newCommitMap.get(path));
            }
        }
        return deleteFiles;
    }

    private static List<String> changeBlobIDListToFileNameList(List<String> blobIDList) {
        List<String> fileNameList = new ArrayList<>();
        Blob b;
        for (String id : blobIDList) {
            b = getBlobByID(id);
            fileNameList.add(b.getFileName().getName());
        }
        return fileNameList;
    }

    private static void checkIfConflict(List<String> allFiles, Commit splitPoint, Commit newCommit, Commit mergeCommit) {
        Map<String, String> splitPointMap = splitPoint.getBlobs();
        Map<String, String> newCommitMap = newCommit.getBlobs();
        Map<String, String> mergeCommitMap = mergeCommit.getBlobs();

        boolean conflict = false;
        for (String blobID : allFiles) {
            String path = getBlobByID(blobID).getPath();
            int commonPath = 0;
            if (splitPointMap.containsKey(path)) {
                commonPath += 1;
            }
            if (newCommitMap.containsKey(path)) {
                commonPath += 2;
            }
            if (mergeCommitMap.containsKey(path)) {
                commonPath += 4;
            }
            if ((commonPath == 3 && (!splitPointMap.get(path).equals(newCommitMap.get(path)))) ||
                    (commonPath == 5 && (!splitPointMap.get(path).equals(mergeCommitMap.get(path)))) ||
                    (commonPath == 6 && (!newCommitMap.get(path).equals(mergeCommitMap.get(path)))) ||
                    (commonPath == 7 &&
                            (!splitPointMap.get(path).equals(newCommitMap.get(path))) &&
                            (!splitPointMap.get(path).equals(mergeCommitMap.get(path))) &&
                            (!newCommitMap.get(path).equals(mergeCommitMap.get(path))))) {

                conflict = true;
                String currBranchContents = "";
                if (newCommitMap.containsKey(path)) {
                    Blob newCommitBlob = getBlobByID(newCommitMap.get(path));
                    currBranchContents = new String(newCommitBlob.getBytes(), StandardCharsets.UTF_8);
                }

                String givenBranchContents = "";
                if (mergeCommitMap.containsKey(path)) {
                    Blob mergeCommitBlob = getBlobByID(mergeCommitMap.get(path));
                    givenBranchContents = new String(mergeCommitBlob.getBytes(), StandardCharsets.UTF_8);
                }

                String conflictContents = "<<<<<<< HEAD\n" + currBranchContents + "=======\n" + givenBranchContents + ">>>>>>>\n";
                String fileName = getBlobByID(blobID).getFileName().getName();
                File conflictFile = join(CWD, fileName);
                writeContents(conflictFile, conflictContents);
            }

        }

        if (conflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    private static Commit caculateMergedCommit(Commit newCommit, List<String> overwriteFiles, List<String> writeFiles, List<String> deleteFiles) {
        Map<String, String> mergedCommitBlobs = newCommit.getBlobs();
        if (!overwriteFiles.isEmpty()) {
            for (String blobID : overwriteFiles) {
                Blob b = getBlobByID(blobID);
                mergedCommitBlobs.put(b.getPath(), blobID);
            }
        }
        if (!writeFiles.isEmpty()) {
            for (String blobID : writeFiles) {
                Blob b = getBlobByID(blobID);
                mergedCommitBlobs.put(b.getPath(), blobID);
            }
        }
        if (!deleteFiles.isEmpty()) {
            for (String blobID : overwriteFiles) {
                Blob b = getBlobByID(blobID);
                mergedCommitBlobs.remove(b.getPath());
            }
        }
        return new Commit(newCommit.getMessage(), mergedCommitBlobs, newCommit.getParents());
    }
}

package gitlet;

import static gitlet.Repository.*;

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
        if (ADDSTAGE.length() == 0) {
           return new Stage();
        }
        return Utils.readObject(ADDSTAGE, Stage.class);
    }

    public static Stage getAddStage() {
        if (REMOVESTAGE.length() == 0) {
            return new Stage();
        }
        return Utils.readObject(REMOVESTAGE, Stage.class);
    }
}

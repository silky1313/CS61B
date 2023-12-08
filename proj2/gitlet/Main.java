package gitlet;

import java.io.IOException;

import static gitlet.RepositoryUtils.exit;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            exit("Please enter a command.");
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                check(args, 1);
                Repository.init();
                break;

            case "add":
                check(args, 2);
                gitletExits();
                Repository.add(args[1]);
                break;

            case "commit":
                check(args, 2);
                gitletExits();
                Repository.commit(args[1]);
                break;

            case "rm":
                check(args, 2);
                gitletExits();
                Repository.rm(args[1]);
                break;

            case "log":
                check(args, 1);
                gitletExits();
                Repository.log();
                break;

            case "global-log":
                check(args, 1);
                gitletExits();
                Repository.globalLog();
                break;

            case "find":
                check(args, 2);
                gitletExits();
                Repository.find(args[1]);
                break;

            case "status":
                check(args, 1);
                gitletExits();
                Repository.status();
                break;

            case "checkout":
                gitletExits();
                /*checkout -- [file name]*/
                /*checkout [commit id] -- [file name]*/
                /*checkout [branch name]*/
                switch (args.length) {
                    case 3:
                        if (!args[1].equals("--")){
                            exit("Incorrect operands.");
                        }
                        Repository.checkOutFileOnCurCommit(args[2]);
                        break;
                    case 4:
                        if (!args[2].equals("--")) {
                            exit("Incorrect operands.");
                        }
                    case 2:

                }

                break;
            default:
                exit("No command with that name exists.");
        }
    }

    public static void check(String[] args, int length) {
        if (args.length != length) {
            exit("Incorrect operands.");
        }
    }

    public static void gitletExits() {
        if (!Repository.GITLET_DIR.exists()) {
            exit("Not in an initialized Gitlet directory.");
        }
    }
}

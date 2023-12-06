package gitlet;

import java.io.IOException;

import static gitlet.RepositoryUtils.exit;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        // TODO: what if args is empty?
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

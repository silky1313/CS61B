package capers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static capers.Utils.*;

/**
 * A repository for Capers
 *
 * @author TODO
 * The structure of a Capers Repository is as follows:
 * <p>
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 * - dogs/ -- folder containing all of the persistent data for dogs
 * - story -- file containing the current story
 * <p>
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /**
     * Current Working Directory.
     */
    static final File CWD = new File(System.getProperty("user.dir"));

    /**
     * Main metadata folder.
     */
    static final File CAPERS_FOLDER = new File(".capers");
    // TODO Hint: look at the `join`
    //      function in Utils

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     * <p>
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     * - dogs/ -- folder containing all of the persistent data for dogs
     * - story -- file containing the current story
     */
    public static void setupPersistence() throws IOException {
        // TODO 1.先创建story文件
        File storyFile = join(CWD, CAPERS_FOLDER.getName(), "story");
        if (!storyFile.exists()) {
            createDir(storyFile);
            createFile(storyFile);
        }

        //TODO 2.创建dogs文件夹
        File dogFile =
                join(CWD, CAPERS_FOLDER.getName(), Dog.DOG_FOLDER.getName());
        if (!dogFile.exists()) {
            createDir(dogFile);
        }
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     *
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) throws IOException {
        // TODO: 1.先将text写入
        String input = text + System.getProperty("line.separator");
        File inputFile = join(CWD, CAPERS_FOLDER.getName(), "story");
        Files.write(Path.of(inputFile.getPath()), input.getBytes(),
                StandardOpenOption.APPEND);
        String output = readContentsAsString(inputFile);
        System.out.println(output);
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age)
            throws IOException {
        Dog dog = new Dog(name, breed, age);
        dog.saveDog();
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     *
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) throws IOException {
        // TODO
        //1. 先反序列化得到对象，庆祝生日，再序列化回去
        Dog dog = Dog.fromFile(name);
        dog.haveBirthday();
        writeObject(dog.getDogFile(), dog);
    }
}

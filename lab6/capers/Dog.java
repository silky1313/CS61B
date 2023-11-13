package capers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static capers.CapersRepository.CAPERS_FOLDER;
import static capers.CapersRepository.CWD;
import static capers.Utils.*;

/**
 * Represents a dog that can be serialized.
 *
 * @author TODO
 */
public class Dog implements Serializable { // TODO

    /**
     * Folder that dogs live in.
     */
    static final File DOG_FOLDER = new File("dogs");
    // TODO (hint: look at the `join`
    //      function in Utils)

    /**
     * Age of dog.
     */
    private int age;
    /**
     * Breed of dog.
     */
    private String breed;
    /**
     * Name of dog.
     */
    private String name;

    /**
     * Creates a dog object with the specified parameters.
     *
     * @param name  Name of dog
     * @param breed Breed of dog
     * @param age   Age of dog
     */
    public Dog(String name, String breed, int age) {
        this.age = age;
        this.breed = breed;
        this.name = name;
    }

    /**
     * Reads in and deserializes a dog from a file with name NAME in DOG_FOLDER.
     *
     * @param name Name of dog to load
     * @return Dog read from file
     */
    public static Dog fromFile(String name) {
        // TODO (hint: look at the Utils file)
        File dogFile =
                join(CWD, CAPERS_FOLDER.getName(), Dog.DOG_FOLDER.getName(),
                        name);
        return readObject(dogFile, Dog.class);
    }

    /**
     * Increases a dog's age and celebrates!
     */
    public void haveBirthday() {
        age += 1;
        System.out.println(toString());
        System.out.println("Happy birthday! Woof! Woof!");
    }

    /**
     * Saves a dog to a file for future use.
     */
    public void saveDog() throws IOException {
        // TODO (hint: don't forget dog names are unique)
        File dogFile = getDogFile();

        if (dogFile.exists()) {
            System.out.println("dog is exits");
            return;
        }
        dogFile = createFile(dogFile);

        String text =
                "Woof! My name is %s and I am a %s! I am %d years old! Woof!";
        String replacedText =
                String.format(text, this.name, this.breed, this.age);
        writeObject(dogFile, this);
        System.out.println(replacedText);
    }

    @Override
    public String toString() {
        return String.format(
                "Woof! My name is %s and I am a %s! I am %d years old! Woof!",
                name, breed, age);
    }

    public File getDogFile() {
        return join(CWD, CAPERS_FOLDER.getName(), Dog.DOG_FOLDER.getName(),
                this.name);
    }
}

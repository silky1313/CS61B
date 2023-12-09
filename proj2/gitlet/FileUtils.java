package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static gitlet.Utils.join;

public class FileUtils {

    public static File createFile(File file) throws IOException {
        Path path = Path.of(file.getPath());
        Files.createDirectories(path.getParent());
        return Files.createFile(path).toFile();
    }

    public static File createDir(File file) throws IOException {
        Path path = Path.of(file.getPath());
        return Files.createDirectories(path).toFile();
    }

    public static void deleteFile(File deleteFile) {
        deleteFile.deleteOnExit();
    }

    /*All files prefixed with file*/
    public static void deleteFiles(List<String>fileNames, File prefix) {
        for (String i : fileNames) {
            File file = join(prefix, i);
            deleteFile(file);
        }
    }
}

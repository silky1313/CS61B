package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
}

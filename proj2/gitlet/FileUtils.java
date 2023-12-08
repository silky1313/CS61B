package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static gitlet.Repository.*;

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

    /*
    * 将file里面的内容写入CWD下的name内*/
    public static void addFileCWD(File file, String name) {
//        File newFile = Utils.join(CWD, name);
//        Utils.writeContents(newFile, );
    }
}

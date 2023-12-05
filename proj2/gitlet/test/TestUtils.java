package gitlet.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

public class TestUtils {

    public static void writeFile(String content, String fileName) {
        File file = join(CWD, fileName);
        writeContents(file, content);
    }

    public static void deleteDirectoryStream(Path path) {
        try {
            Files.delete(path);
            System.out.printf("删除文件成功：%s%n",path.toString());
        } catch (IOException e) {
            System.err.printf("无法删除的路径 %s%n%s", path, e);
        }
    }
}

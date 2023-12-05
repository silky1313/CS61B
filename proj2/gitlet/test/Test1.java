package gitlet.test;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import static gitlet.FileUtils.*;
import static gitlet.test.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static gitlet.Main.*;
import static gitlet.Repository.*;
import static gitlet.RepositoryUtils.*;
import static gitlet.Utils.*;

public class Test1 {

    public static String[] log = {"log"};

    @Before
    public void init() throws IOException {
        File file = join(CWD, ".gitlet");
        if (file.exists()) {
            Path path = Path.of(file.getPath());

            try (Stream<Path> walk = Files.walk(path)) {
                walk.sorted(Comparator.reverseOrder())
                        .forEach(TestUtils::deleteDirectoryStream);
            }
        }

        String[] args = {"init"};
        main(args);
    }

    @Test
    public void testAddAndRemove() throws IOException {
        String[] args = {"add", "add.txt"};
        writeFile("a", args[1]);
        main(args);
        System.out.println(addStage.getBlobs());

        String[] args2 = {"rm", "add.txt"};
        main(args2);
        System.out.println(removeStage.getBlobs());

        String[] commitArgs = {"commit", "test"};
        main(commitArgs);
    }

    @Test
    public void testCommit() throws IOException {
        String[] args2 = {"add", "add.txt"};
        writeFile("a", args2[1]);
        main(args2);

        String[] commitArgs = {"commit", "test"};
        main(commitArgs);
        curCommit = getCurCommit();
        assertEquals("test", curCommit.getMessage());
    }

    @Test
    public void testLog() throws IOException {
        StringBuilder content = new StringBuilder();
        StringBuilder message = new StringBuilder();


        for (int i = 0; i < 10; i++) {
            content.append("a");
            message.append("test");
            String[] args2 = {"add", "add.txt"};
            writeFile(content.toString(), args2[1]);
            main(args2);

            String[] commitArgs = {"commit", message.toString()};
            main(commitArgs);
            curCommit = getCurCommit();
            assertEquals(message.toString(), curCommit.getMessage());
        }

        main(log);
    }

}

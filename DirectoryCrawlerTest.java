import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by MMA on 3/4/2016.
 */
public class DirectoryCrawlerTest {

    @org.junit.Test
    public void testRun() throws Exception {

        System.out.println("Testing...");
        // Test folder

        File f = new File("testFolder");
        String indent = "";
        DirectoryCrawler.Crawling(f, indent);

    }
}
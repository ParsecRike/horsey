import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by rike on 08/03/15.
 */
public class FileUrlTest {

    @Test
    public void testUrl()  {
        Resource res = null;

        try {
            res = new UrlResource("file", "/WEB-INF/views/doit.html");
        } catch (MalformedURLException e) {
            fail();
        }

        System.out.println("res " + res.getFilename());


        assertEquals(true, true);
    }
}

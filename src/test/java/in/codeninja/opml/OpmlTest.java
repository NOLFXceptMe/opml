package in.codeninja.opml;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;
import java.net.URL;
import java.util.List;

/**
 * Created by naveen on 8/14/14.
 */
public class OpmlTest {
    private OpmlReader opmlReader = new OpmlReader();

    @Test
    public void readOpmlTest() {
        URL resourceURL = getClass().getResource("/subscriptions.xml");
        String resourceFile = resourceURL.getPath();
        try {
            Reader resourceReader = new FileReader(resourceFile);

            List<OpmlElement> opmlElementList = opmlReader.readDocument(resourceReader);
            System.out.println("Read " + opmlElementList.size() + " entries from resource");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

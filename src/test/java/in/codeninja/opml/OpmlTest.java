package in.codeninja.opml;

import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;

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

            boolean parseStatus = opmlReader.readDocument(resourceReader);
            if(parseStatus) {
                Map<String, List<String>> elementTree = opmlReader.getElementTree();
                for(String element: elementTree.keySet()) {
                    System.out.println("Folder " + element + " has elements " + elementTree.get(element).toString());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParsingIncompleteException e) {
            e.printStackTrace();
        }
    }
}

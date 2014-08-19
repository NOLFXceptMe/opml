package in.codeninja.opml;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Reads OPML documents.
 */
public class OpmlReader {
    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    private static final String TAG = "OpmlReader";
    private static final String ROOT_OUTLINE = "d6e3cff8-2788-11e4-b2a9-100ba9a6f528";

    // ATTRIBUTES
    private boolean initComplete = false;
    private boolean isInOpml = false;
    private boolean parseComplete = true;
    private ArrayList<OpmlElement> elementList;
    private Stack<String> elementStack;
    private Map<String, List<String>> elementTree;
    private String currentFolder = ROOT_OUTLINE;

    /**
     * OpmlReader initialization
     *
     * @return state of initialization
     */
    public boolean init() {
        isInOpml = false;
        parseComplete = false;
        elementList = null;
        elementStack = null;
        elementTree = null;
        currentFolder = ROOT_OUTLINE;

        initComplete = true;

        return initComplete;
    }

    /**
     * Reads an Opml document and returns a list of all OPML elements it can
     * find
     *
     * @throws IOException
     * @throws XmlPullParserException
     */
    public boolean readDocument(Reader reader)
            throws XmlPullParserException, IOException {
        if (!initComplete) init();

        elementTree = new HashMap<String, List<String>>();
        elementStack = new Stack<String>();
        elementList = new ArrayList<OpmlElement>();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(reader);
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    elementStack.push(ROOT_OUTLINE);
                    currentFolder = ROOT_OUTLINE;
                    log.info("Reached beginning of document");
                    log.debug("Moved to folder " + currentFolder);
                    break;
                case XmlPullParser.START_TAG:
                    if (!elementTree.containsKey(currentFolder)) {
                        elementTree.put(currentFolder, new ArrayList<String>());
                    }

                    if (xpp.getName().equals(OpmlSymbols.OPML)) {
                        isInOpml = true;
                        log.debug("Reached beginning of OPML tree.");
                    } else if (isInOpml && xpp.getName().equals(OpmlSymbols.OUTLINE)) {
                        log.debug("Found new Opml element");
                        List<String> currentMembers = elementTree.get(currentFolder);
                        String currentTag = elementStack.push(xpp.getAttributeValue(null, OpmlSymbols.TEXT));
                        currentMembers.add(currentTag);
                        elementTree.put(currentFolder, currentMembers);
                        log.debug("Added " + currentTag + " to " + currentFolder + " folder");

                        OpmlElement element = new OpmlElementImpl();

                        final String title = xpp.getAttributeValue(null, OpmlSymbols.TITLE);
                        final String text = xpp.getAttributeValue(null, OpmlSymbols.TEXT);
                        if (title != null) {
                            log.debug("Using title: " + title);
                            element.setTitle(title);
                        } else {
                            log.warn("Title not found, using text");
                            element.setTitle(text);
                        }
                        element.setText(xpp.getAttributeValue(null, OpmlSymbols.TEXT));
                        element.setXmlUrl(xpp.getAttributeValue(null, OpmlSymbols.XMLURL));
                        element.setHtmlUrl(xpp.getAttributeValue(null, OpmlSymbols.HTMLURL));
                        element.setType(xpp.getAttributeValue(null, OpmlSymbols.TYPE));
                        if (element.getXmlUrl() != null) {
                            if (element.getText() == null) {
                                log.warn("Opml element has no text attribute.");
                                element.setText(element.getXmlUrl());
                            }
                        } else {
                            currentFolder = currentTag;
                            log.debug("Moving to new folder " + currentFolder);
                        }
                        elementList.add(element);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (isInOpml && xpp.getName().equals(OpmlSymbols.OUTLINE)) {
                        String popped = elementStack.pop();

                        if (popped != null && popped.equals(currentFolder) && currentFolder != ROOT_OUTLINE) {
                            currentFolder = elementStack.peek();
                        }
                        log.debug("Moved to folder " + currentFolder);
                        log.debug("Reached end of a tag");
                    }
            }
            eventType = xpp.next();
        }


        if (eventType == XmlPullParser.END_DOCUMENT) {
            log.info("Reached end of document");

            isInOpml = false;
            parseComplete = true;
        }

        log.info("Parsing finished.");

        return parseComplete;
    }

    /* Get the parsed OPML XML as a list of OpmlElement(s) */
    public List<OpmlElement> getElementList() throws ParsingIncompleteException {
        if (!parseComplete) {
            throw new ParsingIncompleteException();
        }

        return elementList;
    }

    /* Get the parsed OPML XML as a hash map of node-name vs children */
    public Map<String, List<String>> getElementTree() throws ParsingIncompleteException {
        if (!parseComplete) {
            throw new ParsingIncompleteException();
        }

        return elementTree;
    }

    /* Get root node key so that client can start reading as a tree */
    public String getRootOutlineKey() {
        return ROOT_OUTLINE;
    }
}

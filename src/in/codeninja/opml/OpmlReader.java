package in.codeninja.opml;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/** Reads OPML documents. */
public class OpmlReader {
	private static final String TAG = "OpmlReader";

	// ATTRIBUTES
	private boolean isInOpml = false;
	private ArrayList<OpmlElement> elementList;

	/**
	 * Reads an Opml document and returns a list of all OPML elements it can
	 * find
	 * 
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public ArrayList<OpmlElement> readDocument(Reader reader)
			throws XmlPullParserException, IOException {
		elementList = new ArrayList<OpmlElement>();
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(reader);
		int eventType = xpp.getEventType();

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				System.out.println(TAG + "Reached beginning of document");
				break;
			case XmlPullParser.START_TAG:
				if (xpp.getName().equals(OpmlSymbols.OPML)) {
					isInOpml = true;
					System.out.println(TAG + "Reached beginning of OPML tree.");
				} else if (isInOpml && xpp.getName().equals(OpmlSymbols.OUTLINE)) {
					System.out.println(TAG + "Found new Opml element");
					OpmlElement element = new OpmlElementImpl();

					final String title = xpp.getAttributeValue(null, OpmlSymbols.TITLE);
					if (title != null) {
						System.out.println(TAG + "Using title: " + title);
						element.setText(title);
					} else {
						System.out.println(TAG + "Title not found, using text");
						element.setText(xpp.getAttributeValue(null, OpmlSymbols.TEXT));			
					}
					element.setXmlUrl(xpp.getAttributeValue(null, OpmlSymbols.XMLURL));
					element.setHtmlUrl(xpp.getAttributeValue(null, OpmlSymbols.HTMLURL));
					element.setType(xpp.getAttributeValue(null, OpmlSymbols.TYPE));
					if (element.getXmlUrl() != null) {
						if (element.getText() == null) {
							System.out.println(TAG + "Opml element has no text attribute.");
							element.setText(element.getXmlUrl());
						}
						elementList.add(element);
					} else {
						System.out.println(TAG + "Skipping element because of missing xml url");
					}
				}
				break;
			}
			eventType = xpp.next();
		}

		System.out.println(TAG + "Parsing finished.");

		return elementList;
	}

}

package in.codeninja.opml;

import io.filr.rss.model.Feed;

import java.io.IOException;
import java.io.Writer;

import org.kxml2.io.KXmlSerializer;
import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.XmlPullParserFactory;

/** Writes OPML documents. */
public class OpmlWriter {
	private static final String TAG = "OpmlWriter";
	private static final String ENCODING = "UTF-8";
	private static final String OPML_VERSION = "2.0";
	private static final String OPML_TITLE = "AntennaPod Subscriptions";

	/**
	 * Takes a list of feeds and a writer and writes those into an OPML
	 * document.
	 * 
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 */
	public void writeDocument(List<Feed> feeds, Writer writer)
			throws IllegalArgumentException, IllegalStateException, IOException {
			System.out.println(TAG + ": Starting to write document");
		
		XmlSerializer xs = new KXmlSerializer();
		xs.setOutput(writer);

		xs.startDocument(ENCODING, false);
		xs.startTag(null, OpmlSymbols.OPML);
		xs.attribute(null, OpmlSymbols.VERSION, OPML_VERSION);

		xs.startTag(null, OpmlSymbols.HEAD);
		xs.startTag(null, OpmlSymbols.TITLE);
		xs.text(OPML_TITLE);
		xs.endTag(null, OpmlSymbols.TITLE);
		xs.endTag(null, OpmlSymbols.HEAD);

		xs.startTag(null, OpmlSymbols.BODY);
		for (Feed feed : feeds) {
			xs.startTag(null, OpmlSymbols.OUTLINE);
			xs.attribute(null, OpmlSymbols.TEXT, feed.getTitle());
			xs.attribute(null, OpmlSymbols.TITLE, feed.getTitle());
			if (feed.getType() != null) {
				xs.attribute(null, OpmlSymbols.TYPE, feed.getType());
			}
			xs.attribute(null, OpmlSymbols.XMLURL, feed.getDownload_url());
			if (feed.getLink() != null) {
				xs.attribute(null, OpmlSymbols.HTMLURL, feed.getLink());
			}
			xs.endTag(null, OpmlSymbols.OUTLINE);
		}
		xs.endTag(null, OpmlSymbols.BODY);
		xs.endTag(null, OpmlSymbols.OPML);
		xs.endDocument();
			System.out.println(TAG + ": Finished writing document");
	}
}

package in.codeninja.opml;

/** Represents a single feed in an OPML file. */
public class OpmlElementImpl implements OpmlElement {
	private String title;
	private String text;
	private String xmlUrl;
	private String htmlUrl;
	private String type;

	public OpmlElementImpl() {
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getXmlUrl() {
		return xmlUrl;
	}

	public void setXmlUrl(String xmlUrl) {
		this.xmlUrl = xmlUrl;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}

    @Override
    public String toString() {
        return "OpmlElementImpl{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", xmlUrl='" + xmlUrl + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}

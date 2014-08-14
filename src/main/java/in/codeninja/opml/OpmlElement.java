package in.codeninja.opml;

public interface OpmlElement {
    public String getTitle();

    public void setTitle(String title);

    public String getText();

    public void setText(String text);

    public String getHtmlUrl();

    public void setHtmlUrl(String htmlUrl);

    public String getXmlUrl();

    public void setXmlUrl(String xmlUrl);

    public String getType();

    public void setType(String type);
}

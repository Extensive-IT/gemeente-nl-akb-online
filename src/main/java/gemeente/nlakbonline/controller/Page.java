package gemeente.nlakbonline.controller;

public class Page {
    private String id;
    private String title;
    private String footer;
    private String content;
    private String introduction;
    private boolean htmlEncoded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public boolean isHtmlEncoded() {
        return htmlEncoded;
    }

    public void setHtmlEncoded(final boolean htmlEncoded) {
        this.htmlEncoded = htmlEncoded;
    }
}

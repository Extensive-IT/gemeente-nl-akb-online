package gemeente.nlakbonline.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
@ConfigurationProperties(prefix = "content.pages")
public class ContentConfiguration {
    private List<Page> pages;

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public Optional<Page> getById(final String id) {
        return this.pages.stream().filter(page -> id.equals(page.getId())).findAny();
    }
}

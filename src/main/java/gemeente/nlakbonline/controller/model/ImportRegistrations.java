package gemeente.nlakbonline.controller.model;

import org.springframework.web.multipart.MultipartFile;

public class ImportRegistrations {
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}

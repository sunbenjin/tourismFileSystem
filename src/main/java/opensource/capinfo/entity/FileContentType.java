package opensource.capinfo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//@Data
@Table(name = "file_content_type")
@Entity
public class FileContentType extends BaseEntity {

    private String fileSuffix;//文件后缀名


    private String mimeType;//http请求的content_type
    @Column(name = "file_suffix", nullable = false, length = 255)
    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }
    @Column(name = "mime_type", nullable = false, length = 255)
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}

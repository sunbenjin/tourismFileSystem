package opensource.capinfo.entity;

/**
 * 支持三种设置
 */
public enum PreviewType {
    IMG("file-preview-image"), OTHER("file-preview-other"), TEXT("file-preview-text");

    private String clazz;

    private PreviewType(String clazz) {
        this.clazz = clazz;
    }

    public String getClazz() {
        return clazz;
    }
}

package opensource.capinfo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Entity
@NoArgsConstructor
@Table(name = "sys_resources_files")
public class SysResourcesFilesEntity extends BaseEntity{

    /**
     *文件名
     */
    private String fileName;
    /**
     * 文件安全等级
     */
    private String fileLevel;
    /**
     * 文件后缀名
     */
    private String fileSuffix;
    /**
     * 文件路径
     */
    private String fileUrl;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 业务表ID
     */
    private String busiId;
    /**
     *  文件动态ID编号
     */
    private String filesDynCode;
    /**
     * 文件唯一性标识
     */
    private String fileUniqueCode;

    /**
     * 系统标识  //对应系统表
     */
    private String sysId;

    /**
     * mime类型
     */
    private String mimeType;
    @Builder
    public SysResourcesFilesEntity(String id, String createBy, Timestamp createDate, String updateBy, Timestamp updateDate, String remarks, String delFlag, String fileName, String fileLevel, String fileSuffix, String fileUrl, String fileSize, String fileType, String busiId, String filesDynCode, String fileUniqueCode, String sysId, String mimeType, File files) {
        super(id, createBy, createDate, updateBy, updateDate, remarks, delFlag);
        this.fileName = fileName;
        this.fileLevel = fileLevel;
        this.fileSuffix = fileSuffix;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.busiId = busiId;
        this.filesDynCode = filesDynCode;
        this.fileUniqueCode = fileUniqueCode;
        this.sysId = sysId;
        this.mimeType = mimeType;
        this.files = files;
    }

    @Transient
    public String getFileAllName() {
        if(StringUtils.isNotBlank(fileName)&&StringUtils.isNotBlank(fileSuffix)) {
            return fileName+"."+fileSuffix;
        }
        return "";
    }


    /**
     * 文件
     */
    @Transient
    private File files;

    @Transient
    public File getFiles() {
        return files;
    }

    public void setFiles(File files) {
        this.files = files;
    }



    @Column(name = "mime_type", nullable = true, length = 1000)
    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }



    @Basic
    @Column(name = "file_name", nullable = true, length = 1000)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Basic
    @Column(name = "file_level", nullable = true, length = 10)
    public String getFileLevel() {
        return fileLevel;
    }

    public void setFileLevel(String fileLevel) {
        this.fileLevel = fileLevel;
    }

    @Basic
    @Column(name = "file_suffix", nullable = true, length = 50)
    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    @Basic
    @Column(name = "file_url", nullable = true, length = 1000)
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Basic
    @Column(name = "file_size", nullable = true, length = 100)
    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    @Basic
    @Column(name = "file_type", nullable = true, length = 100)
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Basic
    @Column(name = "busi_id", nullable = true, length = 64)
    public String getBusiId() {
        return busiId;
    }

    public void setBusiId(String busiId) {
        this.busiId = busiId;
    }

    @Basic
    @Column(name = "files_dyn_code", nullable = true, length = 64)
    public String getFilesDynCode() {
        return filesDynCode;
    }

    public void setFilesDynCode(String filesDynCode) {
        this.filesDynCode = filesDynCode;
    }

    @Basic
    @Column(name = "file_unique_code", nullable = true, length = 255)
    public String getFileUniqueCode() {
        return fileUniqueCode;
    }

    public void setFileUniqueCode(String fileUniqueCode) {
        this.fileUniqueCode = fileUniqueCode;
    }

    @Basic
    @Column(name = "sys_Id", nullable = true, length = 64)
    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SysResourcesFilesEntity that = (SysResourcesFilesEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (fileLevel != null ? !fileLevel.equals(that.fileLevel) : that.fileLevel != null) return false;
        if (fileSuffix != null ? !fileSuffix.equals(that.fileSuffix) : that.fileSuffix != null) return false;
        if (fileUrl != null ? !fileUrl.equals(that.fileUrl) : that.fileUrl != null) return false;
        if (fileSize != null ? !fileSize.equals(that.fileSize) : that.fileSize != null) return false;
        if (fileType != null ? !fileType.equals(that.fileType) : that.fileType != null) return false;
        if (busiId != null ? !busiId.equals(that.busiId) : that.busiId != null) return false;
        if (filesDynCode != null ? !filesDynCode.equals(that.filesDynCode) : that.filesDynCode != null) return false;
        if (fileUniqueCode != null ? !fileUniqueCode.equals(that.fileUniqueCode) : that.fileUniqueCode != null)
            return false;
        if (sysId != null ? !sysId.equals(that.sysId) : that.sysId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (fileLevel != null ? fileLevel.hashCode() : 0);
        result = 31 * result + (fileSuffix != null ? fileSuffix.hashCode() : 0);
        result = 31 * result + (fileUrl != null ? fileUrl.hashCode() : 0);
        result = 31 * result + (fileSize != null ? fileSize.hashCode() : 0);
        result = 31 * result + (fileType != null ? fileType.hashCode() : 0);
        result = 31 * result + (busiId != null ? busiId.hashCode() : 0);
        result = 31 * result + (filesDynCode != null ? filesDynCode.hashCode() : 0);
        result = 31 * result + (fileUniqueCode != null ? fileUniqueCode.hashCode() : 0);
        result = 31 * result + (sysId != null ? sysId.hashCode() : 0);
        return result;
    }
















}

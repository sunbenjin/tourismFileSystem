package opensource.capinfo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import opensource.capinfo.utils.IdGen;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Persister;
import org.springframework.data.annotation.Persistent;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @ClassName BaseEntity
 * @Description TODO
 * @Author 消魂钉
 * @Date 7/2 0002 9:08
 */

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * 删除标记（0：正常；1：删除；2：隐藏；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_HIDE = "2";


    /**
     * id 主键必须有
     */
    protected String id;

    /**
     * 创建者
     */
    protected String createBy;
    /**
     * 创建时间
     */
    protected Timestamp createDate;
    /**
     * 修改者
     */
    protected String updateBy;
    /**
     * 修改时间
     */
    protected Timestamp updateDate;
    /**
     * 备注
     */
    protected String remarks;
    /**
     * 是否上传
     */
    protected String delFlag;

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @PrePersist
    public void setPersist(){
        if(StringUtils.isBlank(this.id)){
            this.id = IdGen.uuid();
        }
        if(StringUtils.isBlank(this.createBy)){
            createBy = "系统默认";
        }
        this.createBy = createBy;
        this.updateBy = createBy;
        if(createDate==null){
            createDate = new Timestamp(new Date().getTime());
        }
        if (StringUtils.isBlank(delFlag)){
            //初始化隐藏
            delFlag = DEL_FLAG_HIDE;
        }
        this.createDate = createDate;
        this.updateDate = createDate;
    }

    @PreUpdate
    public void setUpdate(){
        if(StringUtils.isNotBlank(this.updateBy)){
            createBy = "系统默认";
        }
        this.updateBy = updateBy;
        if(updateDate==null){
            updateDate = new Timestamp(new Date().getTime());
        }
        this.updateDate = updateDate;
    }


    @Basic
    @Column(name = "create_by", nullable = false, length = 64)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        if(StringUtils.isNotBlank(this.createBy)){
            createBy = "系统默认";
        }
        this.createBy = createBy;
        this.updateBy = createBy;
    }

    @Basic
    @Column(name = "create_date", nullable = false)
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        if(createDate==null){
            createDate = new Timestamp(new Date().getTime());
        }
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "update_by", nullable = false, length = 64)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Basic
    @Column(name = "update_date", nullable = false)
    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    @Basic
    @Column(name = "remarks", nullable = true, length = 255)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Basic
    @Column(name = "del_flag", nullable = false, length = 1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }








}

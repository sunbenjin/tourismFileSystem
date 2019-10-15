package opensource.capinfo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="文件上传对象",description="文件上传对象")
public class FileInputParams {
    /**
     * 业务表ID
  	 * --每个页面生成一个随机的编码先放入到busi_id中进行保存，如果页面保存成功后，将其替换为工单ID即可。
  	 *
  	 * 每个页面的每个组件在一个新的填报页面创建的时候会生成一个随机的编码，如果busi_id 没有传参则表示为新工单，
  	 * 如果业务ID有则需要进行回显操作。
  	 * 当busi_id 为空的情况下。
     *
     */
    @ApiModelProperty(value="业务表id",name="busiId",example="对应数据的主键id")
    private String busiId;

    /**
     * 一个组件一个ID
     */
    @ApiModelProperty(value="页面组件标识",name="fileUniqueCode",example="页面组件标识，一个页面中有多个上传的地方时对应同一个业务表id，不同的页面上传组件标识")
    private String fileUniqueCode;		// 页面组件标识

    /**
     * 保存当前页面上传文件的随机码（一个附件，一次页面请求，生成一个）
     */
    @ApiModelProperty(value="页面随机码",name="filesDynCode",example="页面随机码，进入一次页面生成一个对应的页面随机码，进入后上传的附件都带有这个页面随机码")
    private String filesDynCode;//页面随机码
    /**
     * 删除的时候可能会用到
     */
    @ApiModelProperty(value="附件主键",name="key",example="附件主键，一个附件对应一个主键")
    private String key;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 后缀名
     */
    private String fileSuffix;

    /**
     * 文件url
     */
    private String fileUrl;
    /**
     * 文件大小
     */
    private String fileSize;	// 文件大小

    /**
     * 对应的表名
     */
    @ApiModelProperty(value="对应业务表表名",name="tableName",example="对应业务表表名")
    private String tableName; //busiId 对应的表名
    /**
     * 其他任何输入对象
     */
    private Object data;

    //private String accessToken;
	
}

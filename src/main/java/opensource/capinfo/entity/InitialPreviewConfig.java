package opensource.capinfo.entity;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 例子
 *
 * initialPreview: [
 *     // IMAGE DATA
 *    'http://lorempixel.com/800/460/business/1',
 *     // IMAGE RAW MARKUP
 *     '<img src="http://lorempixel.com/800/460/business/2" class="kv-preview-data file-preview-image" style="height:160px">',
 *     // TEXT DATA
 *     "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec ut mauris ut libero fermentum feugiat eu et dui. Mauris condimentum rhoncus enim, sed semper neque vestibulum id. Nulla semper, turpis ut consequat imperdiet, enim turpis aliquet orci, eget venenatis elit sapien non ante. Aliquam neque ipsum, rhoncus id ipsum et, volutpat tincidunt augue. Maecenas dolor libero, gravida nec est at, commodo tempor massa. Sed id feugiat massa. Pellentesque at est eu ante aliquam viverra ac sed est.",
 *     // PDF DATA
 *     'http://kartik-v.github.io/bootstrap-fileinput-samples/samples/pdf-sample.pdf',
 *     // VIDEO DATA
 *     "http://kartik-v.github.io/bootstrap-fileinput-samples/samples/small.mp4",
 * ],
 * initialPreviewAsData: true, // defaults markup
 * initialPreviewConfig: [
 *     {caption: "Business 1.jpg", size: 762980, url: "$urlD", key: 11},
 *     {previewAsData: false, size: 823782, caption: "Business 2.jpg", url: "$urlD", key: 13},
 *     {caption: "Lorem Ipsum.txt", type: "text", size: 1430, url: "$urlD", key: 12},
 *     {type: "pdf", size: 8000, caption: "PDF Sample.pdf", url: "$urlD", key: 14},
 *     {type: "video", size: 375000, filetype: "video/mp4", caption: "Krajee Sample.mp4", url: "$urlD", key: 15},
 * ],
 *
 *
 * @ClassName: InitialPreviewConfig
 * @Description:TODO(这里用一句话描述这个类的作用)
 * @author: litaolin
 * @date: 2019年5月23日 下午9:42:21
 * @Copyright: 2019 www.capinfo.com.cn Inc. All rights reserved.
 */
@Data
@AllArgsConstructor
@Builder
public class InitialPreviewConfig {

    /**
     *  image: {width:"auto", height: "auto", 'max-width': "100%", 'max-height': "100%"},
     *  html:  {width: "213px", height: "160px"},
     *  text: {width: "213px", height: "160px"},
     *  office: {width: "213px", height: "160px"},
     *  gdocs: {width: "213px", height: "160px"},
     *  video: {width: "213px", height: "160px"},
     *  audio: {width: "100%", height: "30px"},
     *  flash: {width: "213px", height:"160px"},
     *  object: {width: "213px", height: "160px"},
     *  pdf: {width:"213px", height: "160px"},
     *  other: {width: "213px", height: "160px"}
     *
     *    'doc': function(ext) {
     *             return ext.match(/(doc|docx)$/i);
     *         },
     *         'xls': function(ext) {
     *             return ext.match(/(xls|xlsx)$/i);
     *         },
     *         'ppt': function(ext) {
     *             return ext.match(/(ppt|pptx)$/i);
     *         },
     *         'zip': function(ext) {
     *             return ext.match(/(zip|rar|tar|gzip|gz|7z)$/i);
     *         },
     *         'htm': function(ext) {
     *             return ext.match(/(htm|html)$/i);
     *         },
     *         'txt': function(ext) {
     *             return ext.match(/(txt|ini|csv|java|php|js|css)$/i);
     *         },
     *         'mov': function(ext) {
     *             return ext.match(/(avi|mpg|mkv|mov|mp4|3gp|webm|wmv)$/i);
     *         },
     *         'mp3': function(ext) {
     *             return ext.match(/(mp3|wav)$/i);
     *         }
     *
     *
     */
    public enum InitialPreviewFileType {
        IMAGE("image","jpg,gif,png"),
        HTML("html","htm,html"),
        TEXT("text","txt,ini,md,csv"),
        OFFICE("office","doc,docx,xls,xlsx,ppt,pptx"),
        GDOCS("gdocs",""),
        VIDEO("video","mp4,avi,mpg,mkv,mov,3gp,webm,wmv"),
        AUDIO("audio","mp3,wav"),
        FLASH("flash","flv"),
        OBJECT("object","exe"),
        PDF("pdf","pdf"),
        OTHER("other",""),
        ZIP("object","zip,rar,tar,7z,gz,gzip");
        private String type;
        private String suffix;
        InitialPreviewFileType(String type,String suffix) {
            this.type = type;
            this.suffix = suffix;
        }

        public String getType() {
            return type;
        }

        public String getSuffix() {
            return suffix;
        }
    }

    @Expose
    private String key;// 将作为数据传递给url via AJAX POST 的密钥。
    @Expose
    private String fileId;// 用于存储data-fileid每个文件所旅途的属性标识 数据库中File表的UUID即可
    @Expose
    private String caption;// 图标 文件的真实名称
    @Expose
    private String fileHttpUrl;//文件回显路径     放在另一个数据里不好取。放在这个里面一个冗余的属性

    /**
     * 在键中配置的文件模板类型之一previewSettings。
     * 这将覆盖initialPreviewFileType设置。这对于标识用于显示特定文件内容的模板很有用。
     */
    private String type; // 对应 previewSettings

    private String filetype;

    private float size; // 文件的大小(以字节为单位)，将使用size布局模板显示。如果未设置或不能将其解析为浮点数，则将显示一个空白字符串。

    private String exif;// 对象的exif属性(仅适用于JPEG图像文件)，该属性将用于在autoOrientImageInitial中配置的图像的自动定向.用于自动翻转图像

    private boolean previewAsData;// 布尔类型
    // 是否将此预览内容解析为数据输入而不是原始标记源。这将覆盖initialPreviewAsData设置。

    private String width; // 字符串，显示的图像/内容的CSS宽度。

    private String url; // 用于通过AJAX
    // post响应删除初始预览中的图像/内容的url。如果没有设置，这将默认为deleteUrl。

    private String frameClass;// 为文件的缩略图框架设置的附加框架css类。

    private String frameAttr;// 对象，缩略图框架的HTML属性设置(设置为键:值对)。

    private String extra;// 额外的数据将通过POST作为数据传递给初始预览删除url/AJAX服务器调用。如果没有设置，这将默认为deleteata。

    //private boolean downloadUrl;//是否允许下载
}


/**
 *
 *
 *
 *         {caption: "Desert.jpg", size: 827000, width: "120px", url: "/file-upload-batch/2", key: 1},
 *         {caption: "Lighthouse.jpg", size: 549000, width: "120px", url: "/file-upload-batch/2", key: 2},
 *         {
 *             type: "video",
 *             size: 375000,
 *             filetype: "video/mp4",
 *             caption: "KrajeeSample.mp4",
 *             url: "/file-upload-batch/2",
 *             key: 3,
 *             downloadUrl: 'http://kartik-v.github.io/bootstrap-fileinput-samples/samples/small.mp4', // override url
 *             filename: 'KrajeeSample.mp4' // override download filename
 *         },
     *         {type: "office", size: 102400, caption: "SampleDOCFile_100kb.doc", url: "/file-upload-batch/2", key: 4},
     *         {type: "office", size: 45056, caption: "SampleXLSFile_38kb.xls", url: "/file-upload-batch/2", key: 5},
     *         {type: "office", size: 512000, caption: "SamplePPTFile_500kb.ppt", url: "/file-upload-batch/2", key: 6},
     *         {type: "gdocs", size: 811008, caption: "multipage_tiff_example.tif", url: "/file-upload-batch/2", key: 7},
     *         {type: "gdocs", size: 375808, caption: "sample_ai.ai", url: "/file-upload-batch/2", key: 8},
     *         {type: "gdocs", size: 40960, caption: "sample_eps.eps", url: "/file-upload-batch/2", key: 9},
     *         {type: "pdf", size: 8000, caption: "About.pdf", url: "/file-upload-batch/2", key: 10, downloadUrl: false}, // disable download
     *         {type: "text", size: 1430, caption: "LoremIpsum.txt", url: "/file-upload-batch/2", key: 11, downloadUrl: false},  // disable download
     *         {type: "html", size: 3550, caption: "LoremIpsum.html", url: "/file-upload-batch/2", key: 12, downloadUrl: false}  // disable download
 *
 *
 */

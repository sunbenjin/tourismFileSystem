package opensource.capinfo.web;

import com.sun.deploy.net.HttpUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import opensource.capinfo.utils.FileinputUtils;
import opensource.capinfo.utils.GsonUtils;
import opensource.capinfo.entity.FileInputParams;
import opensource.capinfo.entity.FileOutUploader;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.service.SysResourcesFilesService;
import opensource.capinfo.uploader.fileNameRule.SimpleDateCodingRules;
import opensource.capinfo.uploader.jsonTransform.FileInputConverter;
import opensource.capinfo.uploader.strategy.FileInputUploaderStrategy;
import opensource.capinfo.uploader.strategy.UploaderStrategy;
import opensource.capinfo.uploader.transformToMp4.ConverterToMp4;
import opensource.capinfo.uploader.uploadHandle.FtpUploaderHandle;
import opensource.capinfo.uploader.uploaderStorage.MySqlUploaderStorage;
import opensource.capinfo.utils.FTPUtils;
import opensource.capinfo.utils.ResultData;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 *
 * /enclosure/fileInput/beetl
 */
@RestController
@Slf4j
@RequestMapping("/enclosure/fileInput")
@Api(description = "FileInput插件接口")
public class FileInputUploaderController {

    @Autowired
    private SysResourcesFilesService sysResourcesFilesService;
    @Value("${localaddress}")
    private String localaddress;
    @Value("${server.port}")
    private String serverPort;

    @Value("${uploader.basePath}")
    private String basePath;

    @Value("${remote-file")
    private String remoteFileUrl;
    /**
     * 测试是否连通可用
     *
     * @return
     */
    //@ApiOperation(value = "测试", notes = "测试是否连通可用")
    @GetMapping(value = "/test")
    public String test() {
        SysResourcesFilesEntity entity = SysResourcesFilesEntity.builder().id("李陶琳").busiId("测试一下").build();

        sysResourcesFilesService.save(entity);

        return GsonUtils.toJson(entity);
    }

    //@ApiOperation(value = "测试", notes = "测试beetl是否OK")
    @GetMapping(value = "/beetl")
    public String api(){
        return "/demo";
    }

    @GetMapping(value = "/zoom")
    public ModelAndView zoom(ModelAndView model, HttpServletRequest request){
        model.setViewName("/scaleImg");
        String url = request.getParameter("data");
        model.addObject("url",url);
        return model;
    }


    //@ApiOperation(value = "上传文件", notes = "上传下载组件")
    @PostMapping(value = "/uploader")
    public FileOutUploader uploader(FileInputParams fileInputParams,HttpServletRequest request) {
        //文件上传
        FileInputUploaderStrategy strategy =
                new FileInputUploaderStrategy(
                        fileInputParams,new FtpUploaderHandle(),
                        new SimpleDateCodingRules(),
                        new MySqlUploaderStorage(),
                        new FileInputConverter(),new ConverterToMp4());
        FileOutUploader fileOutUploader = strategy.uploadFile(request);
        System.out.println(GsonUtils.toJson(fileOutUploader));
        return fileOutUploader;
    }

    /**
     * 上传文件接口
     * @param
     * @param request
     * @return
     *
     * (@RequestBody @ApiParam(name="上传文件对象",value="传入json格式，未保存数据前的上传，需传入fileUniqueCode组件标识、tableName对应业务表表名、filesDynCode页面随机码以及附件",required=true) FileInputParams fileInputParams
     */
    @ApiOperation(value = "上传文件", notes = "上传下载组件")
    @PostMapping(value = "/api/uploader",headers="content-type=multipart/form-data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileUniqueCode", value = "fileUniqueCode", required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "tableName", value = "tableName", required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "filesDynCode", value = "filesDynCode", required = true,dataType = "String",paramType = "query")
    })
    public ResultData uploaderApi(@ApiParam(name="fileUniqueCode") String fileUniqueCode,@ApiParam(name="tableName") String tableName,@ApiParam(name="filesDynCode") String filesDynCode, @ApiParam(value="附件",required=true) MultipartFile[] file, HttpServletRequest request) {
        ResultData result = new ResultData();
        FileInputParams fileInputParams = new FileInputParams();
        fileInputParams.setFileUniqueCode(fileUniqueCode);
        fileInputParams.setTableName(tableName);
        fileInputParams.setFilesDynCode(filesDynCode);
        try {
            if (file == null || file.length == 0) {
                return ResultData.error("未上传文件");
            }
            //文件上传
            UploaderStrategy strategy = new FileInputUploaderStrategy(
                    fileInputParams,new FtpUploaderHandle(),
                    new SimpleDateCodingRules(),
                    new MySqlUploaderStorage(),
                    new FileInputConverter(),new ConverterToMp4());
            //FileOutUploader fileOutUploader = strategy.uploadFileByMultipartFile(file);

            FileOutUploader fileOutUploader = strategy.uploadFileMultipleFiles(file);
            result.setMsg("文件上传成功");
            result.setFlag(true);
            result.setData(fileOutUploader);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("上传文件失败");
        }
    }







    //@ApiOperation(value = "点击保存修改对应附件", notes = "点击保存修改对应附件")
    @PostMapping(value = "/updateFile")
    public ResultData updateFile(FileInputParams fileInputParams,HttpServletRequest request) {

        List<SysResourcesFilesEntity> list = sysResourcesFilesService.findByFileUniqueCodeAndFilesDynCode(fileInputParams.getFileUniqueCode(), fileInputParams.getFilesDynCode());
        FileInputUploaderStrategy strategy =
                new FileInputUploaderStrategy(
                        fileInputParams,new FtpUploaderHandle(),
                        new SimpleDateCodingRules(),
                        new MySqlUploaderStorage(),
                        new FileInputConverter(),new ConverterToMp4());
        ResultData resultData = strategy.updateFileEntity(list, fileInputParams.getBusiId());

        return resultData;
    }

    @ApiOperation(value = "点击保存修改对应附件", notes = "点击保存修改对应附件")
    @PostMapping(value = "/api/updateFile")
    public ResultData updateFileApi(@RequestBody @ApiParam(name="上传文件对象",value="传入json格式，保存数据后调用保存对应附件，应传入busiId业务表数据id、fileUniqueCode组件标识、filesDynCode页面随机码",required=true) FileInputParams fileInputParams,HttpServletRequest request) {

        ResultData resultData = null;
        try {
            List<SysResourcesFilesEntity> list = sysResourcesFilesService.findByFileUniqueCodeAndFilesDynCode(fileInputParams.getFileUniqueCode(), fileInputParams.getFilesDynCode());
            FileInputUploaderStrategy strategy =
                    new FileInputUploaderStrategy(
                            fileInputParams,new FtpUploaderHandle(),
                            new SimpleDateCodingRules(),
                            new MySqlUploaderStorage(),
                            new FileInputConverter(),new ConverterToMp4());
            resultData = strategy.updateFileEntity(list, fileInputParams.getBusiId());
            return resultData;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("保存附件数据失败");
        }
    }

    @ApiOperation(value = "busiId", notes = "通过busiId查找附件list")
    @GetMapping(value = "/api/findByBusiId")
    public ResultData findByBusiId(HttpServletRequest request,String busiId) {
        try {
            if(StringUtils.isNotBlank(busiId)){
                List<SysResourcesFilesEntity> list = sysResourcesFilesService.findByBusiId(busiId);
                List<SysResourcesFilesEntity> entities = new ArrayList<>();
                if(!CollectionUtils.isEmpty(list)){
                    for(SysResourcesFilesEntity entity:list){
                        String fileUrl = entity.getFileUrl();

                        entity.setFileUrl(localaddress+":"+serverPort+"/image/"+fileUrl.replaceAll("\\\\", "/"));
                        entities.add(entity);
                    }
                }
                return ResultData.sucess("ok",entities);
            }else{
                return ResultData.error("fail");
            }
        }catch (Exception e){

            return ResultData.error("fail");

        }
    }




    @ApiOperation(value = "回显界面", notes = "回显界面")
    @PostMapping(value = "/init")
    public FileOutUploader init(FileInputParams fileInputParams,HttpServletRequest request) {

        //文件上传
        FileInputUploaderStrategy strategy =
                new FileInputUploaderStrategy(
                        fileInputParams,new FtpUploaderHandle(),
                        new SimpleDateCodingRules(),
                        new MySqlUploaderStorage(),
                        new FileInputConverter(),new ConverterToMp4());
        List<SysResourcesFilesEntity> newList =
                sysResourcesFilesService.findByBusiIdAndFileUniqueCode(fileInputParams.getBusiId(),fileInputParams.getFileUniqueCode());
       // GsonUtils.toJson(fileOutUploader);
        List<SysResourcesFilesEntity> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(newList)){
            for(SysResourcesFilesEntity filesEntity:newList){
                String fileUrl = filesEntity.getFileUrl();
                filesEntity.setFileUrl(fileUrl.replaceAll("\\\\", "/"));
                list.add(filesEntity);

            }
        }
     /*   FileOutUploader init = strategy.init(list);
        System.out.println(GsonUtils.toJson(init));*/
        return strategy.init(list);
    }


    @ApiOperation(value = "回显对应附件", notes = "回显对应附件")
    @PostMapping(value = "/api/init")
    public ResultData initApi(@RequestBody @ApiParam(name="上传文件对象",value="传入json格式，回显对应数据对应上传组件的附件，需传入busiId业务表数据id、fileUniqueCode组件标识",required = true) FileInputParams fileInputParams,HttpServletRequest request) {
        ResultData result = new ResultData();
        try {
            //文件上传
            FileInputUploaderStrategy strategy =
                    new FileInputUploaderStrategy(
                            fileInputParams,new FtpUploaderHandle(),
                            new SimpleDateCodingRules(),
                            new MySqlUploaderStorage(),
                            new FileInputConverter(),new ConverterToMp4());
            List<SysResourcesFilesEntity> newList =
                    sysResourcesFilesService.findByBusiIdAndFileUniqueCode(fileInputParams.getBusiId(),fileInputParams.getFileUniqueCode());
            List<SysResourcesFilesEntity> list = new ArrayList<>();
            if(!CollectionUtils.isEmpty(newList)){
                for(SysResourcesFilesEntity filesEntity:newList){
                    String fileUrl = filesEntity.getFileUrl();
                    filesEntity.setFileUrl(fileUrl.replaceAll("\\\\", "/"));
                    list.add(filesEntity);
                }
            }
            FileOutUploader init = strategy.init(list);
            result.setFlag(true);
            result.setCode(200);
            result.setMsg("获取附件数据成功");
            result.setData(init);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("获取附件数据失败");
        }
    }




    //@ApiOperation(value = "删除文件",notes = "删除文件")
    @PostMapping(value = "/delete")
    public String delete(String key){
        System.out.println(key);
        /**
         * 通过主键删除文件
         */
        if(sysResourcesFilesService.delete(key)){
            return "{\"message\":\"文件删除成功\"}";
        }
        return "{\"message\":\"文件删除失败\"}";
    }

    @ApiOperation(value = "删除文件",notes = "删除文件")
    @PostMapping(value = "/api/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "附件主键", required = true,dataType = "String",paramType = "query")
    })
    public ResultData deleteApi(@ApiParam(name="key",value="附件主键",required = true) String key) {

        try {
            if(sysResourcesFilesService.delete(key)){
                return ResultData.sucess("文件删除成功");
            } else {
                return ResultData.error("文件删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("文件删除失败");
        }
    }



    @ApiOperation(value = "下载文件",notes = "下载文件")
    @GetMapping(value = "/download")
    public void download(String key, HttpServletResponse response){

        SysResourcesFilesEntity entity = sysResourcesFilesService.findById(key);
        OutputStream outputStream = null;
        try {
            response.reset();
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + entity.getFileAllName() + "\"");
            outputStream = new BufferedOutputStream(
                    response.getOutputStream());
            InputStream inputStream = FTPUtils.getInputStream(entity.getFileUrl(), 1000000L);
            byte data[] = new byte[1024];
            while (inputStream.read(data, 0, 1024) >= 0) {
                outputStream.write(data);
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally
        {
            if(outputStream!=null){
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @ApiOperation(value = "生成页面随机码", notes = "生成页面随机码")
    @GetMapping(value = "createFileDynCode")
    public ResultData createFileDynCode() {
        try {
            ResultData result = ResultData.sucess("生成页面随机码成功");
            String dynCode = UUID.randomUUID().toString().replaceAll("-", "");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("dynCode", dynCode);
            result.setData(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("生成页面随机码失败");
        }


    }
    /**
     * 上传文件接口
     * @param
       * @return
     *
     * (@RequestBody @ApiParam(name="上传文件对象",value="传入json格式，未保存数据前的上传，需传入fileUniqueCode组件标识、tableName对应业务表表名、filesDynCode页面随机码以及附件",required=true) FileInputParams fileInputParams
     */
    @ApiOperation(value = "pdf签名", notes = "上传下载组件")
    @PostMapping(value = "/api/insertFileRecord")
     public ResultData insertFileRecord(@RequestBody FileInputParams fileInputParams){
        ResultData result = new ResultData();
        SysResourcesFilesEntity sysResourcesFilesEntity = new SysResourcesFilesEntity();
       File file = new File(basePath+fileInputParams.getFileUrl());
       if(file.isFile()&&file.exists()){
           sysResourcesFilesEntity.setFileName(fileInputParams.getFileName());
           sysResourcesFilesEntity.setFileUrl(fileInputParams.getFileUrl());
           sysResourcesFilesEntity.setDelFlag("0");
           sysResourcesFilesEntity.setSysId(fileInputParams.getFileUniqueCode());
           sysResourcesFilesEntity.setFileUniqueCode(fileInputParams.getFileUniqueCode());
           sysResourcesFilesEntity.setMimeType("application/pdf");
           sysResourcesFilesEntity.setFileType("pdf");
          sysResourcesFilesEntity.setFileSuffix("pdf");
          sysResourcesFilesEntity.setFilesDynCode(fileInputParams.getFilesDynCode());
          sysResourcesFilesEntity.setFileSize(file.length()+"");
          sysResourcesFilesEntity.setBusiId(fileInputParams.getBusiId());

       }
      //  sysResourcesFilesEntity.setFileName();
       try{
           sysResourcesFilesService.save(sysResourcesFilesEntity);

            result.setMsg("文件上传成功");
            result.setFlag(true);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("上传文件失败");
        }
    }
    @ApiOperation(value = "删除pdf签名", notes = "上传下载组件")
    @PostMapping(value = "/api/deleteSignPdf")
    public ResultData deleteSignPdf(@RequestBody FileInputParams fileInputParams) {
        SysResourcesFilesEntity sysResourcesFilesEntity = new SysResourcesFilesEntity();
        try {
            List<SysResourcesFilesEntity> newList = sysResourcesFilesService.findByBusiIdAndFileUniqueCodeAndFilesDynCode(fileInputParams.getBusiId(),fileInputParams.getFileUniqueCode(),fileInputParams.getFilesDynCode());
            /*if(sysResourcesFilesService.delete(key)){
                return ResultData.sucess("文件删除成功");
            } else {
                return ResultData.error("文件删除失败");
            }*/
            if(!CollectionUtils.isEmpty(newList)){
                for (SysResourcesFilesEntity entity:newList){
                  //  entity.setDelFlag("1");
                    sysResourcesFilesService.delete(entity.getId());
                }
            }
            return ResultData.sucess("文件删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.error("文件删除失败");
        }
    }

    @GetMapping("updateTourismFiles")
    public void updateTourismFiles(){
        sysResourcesFilesService.autoUpdateSysResourcesFilesOnce();
    }
}

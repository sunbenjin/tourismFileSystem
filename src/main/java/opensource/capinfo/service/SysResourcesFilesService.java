package opensource.capinfo.service;

import opensource.capinfo.config.ApplicationProperties;
import opensource.capinfo.dao.FileContentTypeRepository;
import opensource.capinfo.dao.SysResourcesFilesRepository;
import opensource.capinfo.entity.FileContentType;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.io.File;
import java.util.List;

/**
 * @ClassName SysResourcesFilesService
 * @Description TODO
 * @Author 消魂钉
 * @Date 7/2 0002 0:47
 */
@Service
@Lazy(false)
@EnableScheduling
public class SysResourcesFilesService {
    @Autowired
    private SysResourcesFilesRepository sysResourcesFilesRepository;
    @Autowired
    private ApplicationProperties props;
    @Autowired
    private FileContentTypeRepository fileContentTypeRepository;
    @Transactional(readOnly = false)
    public void save(SysResourcesFilesEntity sysResourcesFilesEntity){
        sysResourcesFilesRepository.save(sysResourcesFilesEntity);

    };

    /**
     * 删除文件
     * @param key
     */
    @Transactional(readOnly = false)
    public boolean delete(String key) {
        //上传就上传上去吧，删除关联就可以了。
        if(StringUtils.isNotBlank(key)){
            SysResourcesFilesEntity entity = SysResourcesFilesEntity.builder().id(key).build();
            sysResourcesFilesRepository.delete(entity);
            return true;
        }
        return false;
    }


    public SysResourcesFilesEntity findById(String fileId) {
        return sysResourcesFilesRepository.getOne(fileId);
    }

    @Transactional(readOnly = true)
    public List<SysResourcesFilesEntity> findByBusiIdAndFileUniqueCode(String busiId, String fileUniqueCode) {
        return  sysResourcesFilesRepository.findByBusiIdAndFileUniqueCode(busiId, fileUniqueCode);
    }
    public List<SysResourcesFilesEntity> findByBusiId(String busiId) {
        return sysResourcesFilesRepository.findByBusiId(busiId);
    }

    public List<SysResourcesFilesEntity> findByFileUniqueCodeAndFilesDynCode(String fileUniqueCode, String filesDynCode) {
        return sysResourcesFilesRepository.findByFileUniqueCodeAndFilesDynCode(fileUniqueCode, filesDynCode);
    }
    public List<SysResourcesFilesEntity> findByBusiIdAndFileUniqueCodeAndFilesDynCode(String busiId,String fileUniqueCode,String filesDynCode){
        return sysResourcesFilesRepository.findByBusiIdAndFileUniqueCodeAndFilesDynCode(busiId,fileUniqueCode,filesDynCode);
    }
    @Transactional(readOnly = false)
    public void autoUpdateSysResourcesFilesOnce() {
        List<SysResourcesFilesEntity> list = sysResourcesFilesRepository.findAll();
        //System.out.println(list.size()+" list size");
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                SysResourcesFilesEntity entity = list.get(i);
                String fileUrl = entity.getFileUrl();
                File file = new File(props.getFtpServer().getHomeDirectory() + fileUrl);
                System.out.println("is file: "+file.isFile());
                if (file.exists() && file.isFile()) {
                    System.out.println("file size: "+ file.length());
                    sysResourcesFilesRepository.updateOne(file.length()+"",entity.getId());
                }
            }
        }
    }
}

package opensource.capinfo.dao;

import opensource.capinfo.entity.SysResourcesFilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysResourcesFilesRepository extends JpaRepository<SysResourcesFilesEntity,String> {


    List<SysResourcesFilesEntity> findByBusiIdAndFileUniqueCode(String busiId, String fileUniqueCode);

    List<SysResourcesFilesEntity> findByFileUniqueCodeAndFilesDynCode(String fileUniqueCode, String filesDynCode);

    List<SysResourcesFilesEntity> findByBusiIdAndFileUniqueCodeAndFilesDynCode(String busiId,String fileUniqueCode,String filesDynCode);

    List<SysResourcesFilesEntity> findByBusiId(String busiId);

    @Query(value="update sys_resources_files set file_size=?1 where id=?2",nativeQuery = true)
    @Modifying
    public void updateOne(String fileSize,String id);
}
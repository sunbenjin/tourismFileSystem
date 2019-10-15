package opensource.capinfo.dao;

import opensource.capinfo.entity.FileContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileContentTypeRepository extends JpaRepository<FileContentType,String> {

    List<FileContentType> findMineTypeByFileSuffix(String fileSuffix);
}

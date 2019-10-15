package opensource.capinfo.service;

import opensource.capinfo.dao.FileContentTypeRepository;
import opensource.capinfo.entity.FileContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
@Transactional(readOnly = true)
@EnableScheduling
public class FileContentTypeService {

    @Autowired
    private FileContentTypeRepository fileContentTypeRepository;

    public List<FileContentType> fileMimeTypeByFileSuffix(String fileSuffix){
      List<FileContentType> list = fileContentTypeRepository.findMineTypeByFileSuffix(fileSuffix);
        return list;
    }
   // @Scheduled(cron="0 40 19 * * *")
    public void select(){
        String fileSffix = ".pdf";
        //Example
        List<FileContentType> list = fileMimeTypeByFileSuffix(fileSffix);
        if(list.size()>0){
            System.out.println(list.get(0).getMimeType());
        }
    }

    public static void main(String[] args) {
        File file = new File("C:/tourism/tourismfileService/2019/04/09/2019040949158.jpg");
        System.out.println(file.isFile());
    }

}

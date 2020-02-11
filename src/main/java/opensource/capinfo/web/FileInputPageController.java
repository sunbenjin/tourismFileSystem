package opensource.capinfo.web;

import opensource.capinfo.dao.SysResourcesFilesRepository;
import opensource.capinfo.entity.SysResourcesFilesEntity;
import opensource.capinfo.utils.FileData;
import org.hibernate.annotations.CollectionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FileInputPageController
 * @Description TODO
 * @Author 消魂钉
 * @Date 7/4 0004 17:07
 */
@Controller
@RequestMapping(value = "/fileInput")
public class FileInputPageController {

    @Value("${localaddress}")
    private String localaddress;
    @Value("${server.port}")
    private String port;
    @Value("${priview-url}")
    private String priviewUrl;
    /**
     *   var projectName = parameter.attr("projectName")
     *             var filesId = parameter.attr("projectName")
     *             var buisId = parameter.attr("buisId");
     *             var fileUniqueCode = parameter.attr("fileUniqueCode");
     *             var tableName = parameter.attr("tableName");
     *             var readOnly = parameter.attr("readOnly");
     * @param model
     * @param request
     * @return
     */
    @GetMapping("index")
    public ModelAndView test(ModelAndView model, HttpServletRequest request){

        try {
            model.setViewName("/index");
            String projectName = request.getParameter("projectName");
            String filesId =  request.getParameter("filesId");
            String buisId = request.getParameter("buisId");
            String fileUniqueCode = request.getParameter("fileUniqueCode");
            String tableName = request.getParameter("tableName");
            String fileReadOnly = request.getParameter("fileReadOnly");
            String filesDynCode = request.getParameter("filesDynCode");
            String accessToken = request.getParameter("accessToken");
            model.addObject("projectName",projectName);
            model.addObject("filesId",filesId);
            model.addObject("buisId",buisId);
            model.addObject("fileUniqueCode",fileUniqueCode);
            model.addObject("tableName",tableName);
            model.addObject("fileReadOnly",fileReadOnly);
            model.addObject("filesDynCode", filesDynCode);
            model.addObject("accessToken", accessToken);
            model.addObject("localAddress", localaddress+":"+port);
            model.addObject("priviewUrl",priviewUrl);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
}

    public static void main(String[] args) {
        for(int i=0; i<10; i++){
            if(i==1) continue;
            System.out.println(i);
        }
    }

}

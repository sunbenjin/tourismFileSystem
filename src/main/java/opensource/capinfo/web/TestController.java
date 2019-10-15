package opensource.capinfo.web;

import opensource.capinfo.utils.ResultData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {


    @RequestMapping(value = "testApi", method = RequestMethod.GET)
    public ResultData testApi(HttpServletRequest request, HttpServletResponse response) {
        ResultData result = new ResultData();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("test1","test一下");
        map.put("test2","来两个");
        result.setFlag(true);
        result.setCode(200);
        result.setMsg("test");
        result.setData(map);
        return result;
    }

}

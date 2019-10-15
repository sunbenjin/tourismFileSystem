package opensource.capinfo.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import opensource.capinfo.utils.JwtUtil;
import opensource.capinfo.utils.ResultData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/token")
@Api(description = "获取访问权限token")
public class TokenController {

    @Value("${codelist}")
    private String codelist;

    @ApiOperation(value = "获取token", notes = "获取token")
    @RequestMapping(value = "getToken", method = RequestMethod.POST)
    public ResultData getToken(String userId) {
        String codeArr[] = codelist.split(",");
        List<String> codeList = new ArrayList<>(Arrays.asList(codeArr));
        if (!codeList.contains(userId)) {
            return ResultData.error("code不存在");
        }
        String token = JwtUtil.sign(userId);
        if (StringUtils.isNotBlank(token)) {
            ResultData result = ResultData.sucess("请求成功");
            result.setData(token);
            return result;
        }
        return ResultData.error("请求失败");
    }

}

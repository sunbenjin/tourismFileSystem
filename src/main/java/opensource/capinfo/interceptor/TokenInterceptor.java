package opensource.capinfo.interceptor;

import com.alibaba.fastjson.JSONObject;
import opensource.capinfo.utils.JwtUtil;
import opensource.capinfo.utils.ResultData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("utf-8");
        String referer = request.getHeader("referer");
        String token = request.getHeader("accessToken");
        /*if (StringUtils.isNotBlank(referer) && (referer.startsWith("http://1.0.7.124") || referer.startsWith("http://localhost"))) {
            return true;
        }*/
        if (StringUtils.isNotBlank(token)) {
            boolean verify = JwtUtil.verify(token);
            if (verify) {
                return true;
            } else {
                ResultData result = ResultData.error("token验证失败");
                response.getWriter().write(JSONObject.toJSONString(result));
                return false;
            }
        }
        ResultData result = ResultData.error("token为空");
        response.getWriter().write(JSONObject.toJSONString(result));
        return false;
    }
}

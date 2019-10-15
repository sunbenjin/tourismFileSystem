package opensource.capinfo.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final long EXPIRE_TIME = 15 * 60 * 1000;

    private static final String TOKEN_SECRET = "00eac38e72ca4b18a041cc2666c7ccad";

    public static String sign(String userId) {

        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            Map<String, Object> header = new HashMap<String, Object>();
            header.put("typ", "JWT");
            header.put("alg", "HS256");

            if ("123456".equals(userId)) {
                return JWT.create()
                        .withHeader(header)
                        .withClaim("userId", userId)
                        .withExpiresAt(date)
                        .sign(algorithm);
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static boolean verify(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String token1 = jwt.getToken();
            String header = jwt.getHeader();
            Claim userId = jwt.getClaim("userId");
            String str = userId.asString();
            if ("123456".equals(str)) {
                return true;
            } else {
                return false;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }


}

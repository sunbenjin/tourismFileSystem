package opensource.capinfo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @ClassName FileUtils
 * @Description TODO
 * @Author 消魂钉
 * @Date 6/26 0026 10:51
 */
public class RandomFileUtils {


    /**
     * 文件名随机数策略
     * 获取随机文件名称   yyyyMMdd+5尾随机数
     * @return
     */
    public static String getRandomFileName() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return str + rannum;
    }

}

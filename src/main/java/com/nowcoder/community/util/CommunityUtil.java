package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    // 生成随机的字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll(" ", "");
    }

    // MD5加密，只能加密不能解密
    //但是每次加密后只能得到固定的值，所以要加盐
    //盐就是：hello+一个随机的字符串--> fadsfasdfas
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            //判定key是否为null，这就是我们导的apache哪个包
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}

package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.*;

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

    // 获得json字符串
    public static String getJSONString(int code, String msg, Map<String, Object> map){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if(map!=null){
            Iterator itr=map.entrySet().iterator();
            while(itr.hasNext()){
                Map.Entry entry = (Map.Entry)itr.next();
                json.put((String)entry.getKey(), entry.getValue());
            }
        }
        return json.toJSONString();
    }

    // 重载一下
    public static String getJSONString(int code, String msg){
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code){
        return getJSONString(code, null, null);
    }

//    public static void main(String[] args) {
//        Map<String, Object> map=new HashMap<>();
//        map.put("name", "zhangsan");
//        map.put("age", 25);
//        System.out.println(getJSONString(0, "ok", map));
//    }

}

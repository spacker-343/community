package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text="这里可以赌博,嫖娼，吸毒，开票，哈哈";
        text="☆f☆a☆b☆c☆";
//        text="赌赌博博";
//        text="我要学习,我要进大厂";
//        text="fabc";
//        text="赌博吸毒";
        text=sensitiveFilter.filter(text);
        System.out.println(text);
    }
}

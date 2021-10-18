package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware
{

    private ApplicationContext applicationContext;

    /**
     *  当实现了ApplicationContextAware的下面这个方法时时
        spring就会自动把applicationContext对象传入函数中
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Test
    public void testApplicationContext(){
        System.out.println(applicationContext);
        AlphaDao bean = applicationContext.getBean(AlphaDao.class);
        System.out.println(bean.get());
    }

    @Test
    public void testPostConstruct(){
        AlphaService alphaService=applicationContext.getBean(AlphaService.class);
        System.out.println(alphaService);
    }
    @Qualifier
    @Test
    public void testBeanConfig(){
        SimpleDateFormat simpleDateFormat=
                applicationContext.getBean(SimpleDateFormat.class);
        System.out.println(simpleDateFormat.format(new Date()));
    }
}

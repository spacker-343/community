package com.nowcoder.community.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class AlphaService {

    //在构造方法之前调用
    @PostConstruct
    public void init() {
        System.out.println("Alpha init");
    }

    public AlphaService() {
        System.out.println("构造调用");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("销毁AlphaService");
    }
}

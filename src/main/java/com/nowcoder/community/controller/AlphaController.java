package com.nowcoder.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @Author ${USER}
 * @create ${DATE} ${TIME}
 */
@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @RequestMapping("/test")
    @ResponseBody
    public String aa() {
        return "hello world";
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + "=" + value);
        }
        System.out.println(request.getParameter("code"));
        response.setContentType("text/html;charset=UTF-8");
        try (
                PrintWriter writer = response.getWriter()
        ) {
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(@RequestParam(name = "current", required = false, defaultValue = "1") int current,
                              @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    @RequestMapping(path = "/students/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }

    @ResponseBody
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    public String saveStudent(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView model = new ModelAndView();
        model.addObject("name", "张三");
        model.addObject("age", 30);
        model.setViewName("/demo/view");
        return model;
    }

    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model) {
        model.addAttribute("name", "李四");
        model.addAttribute("age", 20);
        return "/demo/view";
    }

    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> map = new HashMap<>(1);
        map.put("name", "张三");
        map.put("age", 20);
        map.put("salary", 8000.00);
        return map;
    }

    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>(1);
        map.put("name", "张三");
        map.put("age", 20);
        map.put("salary", 8000.00);

        Map<String, Object> map2 = new HashMap<>(1);
        map2.put("name", "李四");
        map2.put("age", 22);
        map2.put("salary", 9000.00);
        list.add(map);
        list.add(map2);
        return list;
    }
}

package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    // 登录才可以访问该路径
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage() {
        return "/site/setting";
    }

    /**
     * @param headerImage 如果是多个图片，这里要写数组,这里要和form表单input的name属性一致
     * @param model
     * @return
     */
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有选择图片");
            return "/site/setting";
        }

        // 需要修改用户上传文件的名字，因为可能很多人传相同名字的图片
        String fileName = headerImage.getOriginalFilename();

        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 文件格式不正确应该也加上判断文件后缀是否图片文件的后缀 &&
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式不正确");
            return "/site/setting";
        }

        // 生成文件随机名加后缀
        fileName = CommunityUtil.generateUUID() + suffix;

        // 传输进指定的文件夹名+文件名
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败" + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }

        // 更新当前用户的头像的路径（web访问路径）
        // http://localhost:8080/community/user/header/xxx.png
        // 每访问一个controller方法，拦截器就会在threadLocal里保存一个user对象
        User user = hostHolder.getUser();
        // 其实就是下面的getHeader方法的访问路径
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        // 新头像的路径
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    /**
     * th:src就会请求这个方法
     * 从服务器上获取自己上传的头像
     * 可以查看别人的头像，所以不需要loginrequired
     */
    @RequestMapping(path = "/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放文件的路径
        String filePath = uploadPath + "/" + fileName;
        // 文件后缀
        // 响应格式“image/jpg”或者'image/png'等等
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);

        // 响应图片
        // 图片路径
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(filePath);
                OutputStream os = response.getOutputStream();
        ) {
            // 使用缓存
            byte[] buffer = new byte[1024];

            // 返回读入缓冲区的总字节数，如果由于已到达文件末尾而没有更多数据，则为-1
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败" + e.getMessage());
        }
    }

    /**
     * 这是我自己写的方法
     * @param oldPassword
     * @param newPassword
     * @param model
     * @return
     */
    @RequestMapping(path = "/updatepassword", method = RequestMethod.POST)
    public String updatePassword(String oldPassword, String newPassword, Model model) {
        // 验证新密码或旧密码是否为空，即是前端验证了一道，我们也要防止专业人士来搞破坏
        if (StringUtils.isBlank(oldPassword)) {
            model.addAttribute("oldPasswordError", "原密码不能为空");
            return "/site/setting";
        }
        if (StringUtils.isBlank(newPassword)) {
            model.addAttribute("newPasswordError", "新密码不能为空");
            return "/site/setting";
        }

        // 验证原密码是否正确
        // 获取user
        User user = hostHolder.getUser();
        // 对传进来的旧密码加上盐后进行md5加密,然后进行匹配
        String md5 = CommunityUtil.md5(oldPassword + user.getSalt());
        if (!user.getPassword().equals(md5)) {
            model.addAttribute("oldPasswordError", "旧密码不正确");
            return "/site/setting";
        }

        // 新密码加上盐后进行md5加密
        newPassword = CommunityUtil.md5(newPassword + user.getSalt());

        // 修改密码
        userService.updatePassword(user.getId(), newPassword);

        // 注销当前用户
        return "redirect:/logout";
    }

    // 个人主页
    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }

        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        // 关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 是否已关注
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }

        model.addAttribute("hasFollowed", hasFollowed);
        return "/site/profile";
    }
}

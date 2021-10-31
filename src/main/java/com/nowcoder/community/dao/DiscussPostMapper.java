package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    /**
     * 返回所有帖子
     *
     * @param userId 用户id，用来查询用户的帖子
     * @param offset limit 开始行号
     * @param limit  每页显示数
     * @return 返回分页后的帖子
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * \@Param 注解用于给参数取别名
     * 如果只有一个参数，并且在<if></if>里使用，则必须加别名
     *
     * @param userId 用户id
     * @return 返回帖子总数
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    /**
     * 插入帖子
     *
     * @param discussPost
     * @return
     */
    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    /**
     * 更新帖子评论数量
     *
     * @return
     */
    int updateCommentCount(int id, int commentCount);

}

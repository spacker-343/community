package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    /**
     * 返回所有帖子
     * 在第七章中增加了帖子的分数，因此新增参数orderMode,用来按分数进行排序
     * 实际上就是考得order by score desc
     * @param userId 用户id，用来查询用户的帖子
     * @param offset limit 开始行号
     * @param limit  每页显示数
     * @param orderMode 1：按照分数排序，用在热度 0：不按分数排序
     * @return 返回分页后的帖子
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);

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

    /**
     * 0-普通; 1-置顶;
     * @param id
     * @param type
     * @return
     */
    int updateType(int id, int type);

    /**
     * 0-正常; 1-精华; 2-拉黑;
     * @param id
     * @param status
     * @return
     */
    int updateStatus(int id, int status);

    int updateScore(int id, double score);

}

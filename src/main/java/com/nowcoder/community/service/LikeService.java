package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 登录用户给评论或评论下的回复点赞
     *
     * @param userId     当前登录用户
     * @param entityType 评论还是评论下的回复
     * @param entityId   评论的用户ID
     */
    public void like(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);

        // 查询是否包含该value
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if (isMember) {
            // 如果包含说明用户已点赞，那么取消该点赞
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        } else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
    }

    /**
     * 查询点赞的数量
     * 因为set中是不可重复的，那么该条评论userId的数量就是赞的数量
     *
     * @param entityType 评论还是评论下的回复
     * @param entityId   评论的用户ID
     * @return 点赞的数量
     */
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    /**
     * 查询当前用户对该条评论是否点赞
     * 初次查看帖子时有用
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return 1：点赞 0：未点赞
     */
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }
}

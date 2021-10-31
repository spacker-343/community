package com.nowcoder.community.util;

// 这个赞纯属是存在redis里了，和数据库没关系，持久化也是redis来做
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";

    /**
     * 评论评论entityId是该条评论的entityId，回复的entityId是评论数据库的自增字段id
     * 我曾认为点赞一个，全部的评论赞数都会增加，因为只有一个entityId
     * 所以根本就没重复，信息都用到了
     *
     * @param entityType 帖子还是评论的赞
     * @param entityId
     * @return
     */
    public static String getEntityLikeKey(int entityType, int entityId) {
        // like:entity:entityType:entityId
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 某个用户的赞
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

}

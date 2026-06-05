package com.bookmark.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bookmark.entity.Bookmark;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookmarkMapper extends BaseMapper<Bookmark> {
    /**
     * 分页查询用户的书签列表
     */
    default IPage<Bookmark> selectByUserId(Page<Bookmark> page, @Param("userId") Long userId) {
        return this.selectPage(page, new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Bookmark>()
                .eq("user_id", userId)
                .orderByDesc("create_time"));
    }

    /**
     * 根据标签搜索书签
     */
    default IPage<Bookmark> selectByTag(Page<Bookmark> page, @Param("userId") Long userId, @Param("tag") String tag) {
        return this.selectPage(page, new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Bookmark>()
                .eq("user_id", userId)
                .like("tags", tag)
                .orderByDesc("create_time"));
    }

    /**
     * 根据关键词搜索书签
     */
    default IPage<Bookmark> searchBookmarks(Page<Bookmark> page, @Param("userId") Long userId, @Param("keyword") String keyword) {
        return this.selectPage(page, new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Bookmark>()
                .eq("user_id", userId)
                .and(wrapper -> wrapper
                        .like("title", keyword)
                        .or()
                        .like("description", keyword)
                        .or()
                        .like("tags", keyword))
                .orderByDesc("create_time"));
    }

    /**
     * 获取用户的所有标签
     */
    @Select("SELECT DISTINCT tags FROM bookmark WHERE user_id = #{userId} AND tags IS NOT NULL")
    List<String> selectTagsByUserId(@Param("userId") Long userId);
}
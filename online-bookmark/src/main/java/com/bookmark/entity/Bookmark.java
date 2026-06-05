package com.bookmark.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 书签实体类
 * @author 开发者
 * @date 2024-01-01
 */
@Data
@TableName("bookmark")
public class Bookmark {
    /**
     * 书签ID（主键，自增）
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID（外键）
     */
    private Long userId;

    /**
     * 书签标题
     */
    private String title;

    /**
     * 网址
     */
    private String url;

    /**
     * 描述
     */
    private String description;

    /**
     * 标签（逗号分隔）
     */
    private String tags;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
package com.bookmark.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bookmark.entity.Bookmark;
import com.bookmark.entity.User;
import com.bookmark.mapper.BookmarkMapper;
import com.bookmark.service.IBookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 书签服务实现类
 * @author 开发者
 */
@Service
public class BookmarkServiceImpl implements IBookmarkService {

    @Autowired
    private BookmarkMapper bookmarkMapper;

    /**
     * 添加书签
     */
    @Override
    public String addBookmark(Bookmark bookmark, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "请先登录！";
        }

        bookmark.setUserId(currentUser.getId());
        bookmark.setCreateTime(LocalDateTime.now());
        bookmark.setUpdateTime(LocalDateTime.now());

        // 格式化标签（去除空格，统一小写）
        if (!StringUtils.isEmpty(bookmark.getTags())) {
            String formattedTags = Arrays.stream(bookmark.getTags().split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.joining(","));
            bookmark.setTags(formattedTags);
        }

        int result = bookmarkMapper.insert(bookmark);
        return result > 0 ? "添加成功！" : "添加失败，请重试！";
    }

    /**
     * 编辑书签
     */
    @Override
    public String editBookmark(Bookmark bookmark, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "请先登录！";
        }

        // 验证书签归属
        Bookmark existing = bookmarkMapper.selectById(bookmark.getId());
        if (existing == null || !existing.getUserId().equals(currentUser.getId())) {
            return "无权操作此书签！";
        }

        // 更新字段
        existing.setTitle(bookmark.getTitle());
        existing.setUrl(bookmark.getUrl());
        existing.setDescription(bookmark.getDescription());

        // 格式化标签
        if (!StringUtils.isEmpty(bookmark.getTags())) {
            String formattedTags = Arrays.stream(bookmark.getTags().split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.joining(","));
            existing.setTags(formattedTags);
        }

        existing.setUpdateTime(LocalDateTime.now());
        int result = bookmarkMapper.updateById(existing);
        return result > 0 ? "修改成功！" : "修改失败，请重试！";
    }

    /**
     * 删除书签
     */
    @Override
    public String deleteBookmark(Long bookmarkId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "请先登录！";
        }

        // 验证书签归属
        Bookmark existing = bookmarkMapper.selectById(bookmarkId);
        if (existing == null || !existing.getUserId().equals(currentUser.getId())) {
            return "无权删除此书签！";
        }

        int result = bookmarkMapper.deleteById(bookmarkId);
        return result > 0 ? "删除成功！" : "删除失败，请重试！";
    }

    /**
     * 查看书签详情
     */
    @Override
    public Bookmark getBookmarkDetail(Long bookmarkId) {
        return bookmarkMapper.selectById(bookmarkId);
    }

    /**
     * 分页查询用户书签
     */
    // 分页查询用户书签
    @Override
    public IPage<Bookmark> getBookmarksByPage(Integer pageNum, Integer pageSize, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getId() == null) {
            return new Page<>(pageNum, pageSize); // 返回空但非 null 的 Page
        }
        Page<Bookmark> page = new Page<>(pageNum, pageSize);
        IPage<Bookmark> result = bookmarkMapper.selectByUserId(page, currentUser.getId());
        return result != null ? result : page; // 确保永不返回 null
    }

    // 按标签分类查看
    @Override
    public IPage<Bookmark> getBookmarksByTag(String tag, Integer pageNum, Integer pageSize, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getId() == null) {
            return new Page<>(pageNum, pageSize);
        }
        Page<Bookmark> page = new Page<>(pageNum, pageSize);
        IPage<Bookmark> result = bookmarkMapper.selectByTag(page, currentUser.getId(), tag);
        return result != null ? result : page;
    }

    // 搜索书签
    @Override
    public IPage<Bookmark> searchBookmarks(String keyword, Integer pageNum, Integer pageSize, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getId() == null) {
            return new Page<>(pageNum, pageSize);
        }
        Page<Bookmark> page = new Page<>(pageNum, pageSize);
        IPage<Bookmark> result = bookmarkMapper.searchBookmarks(page, currentUser.getId(), keyword);
        return result != null ? result : page;
    }

    /**
     * 获取用户的所有标签
     */
    @Override
    public List<String> getUserTags(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return java.util.Collections.emptyList();
        }

        // 添加空值保护
        List<String> tagStrings = bookmarkMapper.selectTagsByUserId(currentUser.getId());
        if (tagStrings == null || tagStrings.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        // 解析并去重所有标签
        return tagStrings.stream()
                .filter(tags -> !StringUtils.isEmpty(tags))
                .flatMap(tags -> Arrays.stream(tags.split(",")))
                .map(String::trim)
                .filter(tag -> !StringUtils.isEmpty(tag)) // 过滤空标签
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
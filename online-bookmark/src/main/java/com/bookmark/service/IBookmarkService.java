package com.bookmark.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bookmark.entity.Bookmark;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 书签服务接口
 * @author 开发者
 */
public interface IBookmarkService {

    /**
     * 添加书签
     * @param bookmark 书签对象
     * @param session HttpSession
     * @return 操作结果
     */
    String addBookmark(Bookmark bookmark, HttpSession session);

    /**
     * 编辑书签
     * @param bookmark 书签对象
     * @param session HttpSession
     * @return 操作结果
     */
    String editBookmark(Bookmark bookmark, HttpSession session);

    /**
     * 删除书签
     * @param bookmarkId 书签ID
     * @param session HttpSession
     * @return 操作结果
     */
    String deleteBookmark(Long bookmarkId, HttpSession session);

    /**
     * 查看书签详情
     * @param bookmarkId 书签ID
     * @return 书签对象
     */
    Bookmark getBookmarkDetail(Long bookmarkId);

    /**
     * 分页查询用户书签
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param session HttpSession
     * @return 分页结果
     */
    IPage<Bookmark> getBookmarksByPage(Integer pageNum, Integer pageSize, HttpSession session);

    /**
     * 按标签分类查看
     * @param tag 标签
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param session HttpSession
     * @return 分页结果
     */
    IPage<Bookmark> getBookmarksByTag(String tag, Integer pageNum, Integer pageSize, HttpSession session);

    /**
     * 搜索书签
     * @param keyword 关键词
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param session HttpSession
     * @return 分页结果
     */
    IPage<Bookmark> searchBookmarks(String keyword, Integer pageNum, Integer pageSize, HttpSession session);

    /**
     * 获取用户的所有标签
     * @param session HttpSession
     * @return 标签列表
     */
    List<String> getUserTags(HttpSession session);
}
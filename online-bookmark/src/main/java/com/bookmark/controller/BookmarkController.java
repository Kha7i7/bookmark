package com.bookmark.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bookmark.entity.Bookmark;
import com.bookmark.entity.User;
import com.bookmark.service.IBookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 书签控制器
 * @author 开发者
 */
@Controller
@RequestMapping("/bookmark")
public class BookmarkController {

    @Autowired
    private IBookmarkService bookmarkService;

    /**
     * 首页 - 书签列表
     */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       Model model, HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                return "redirect:/user/login";
            }

            IPage<Bookmark> page = bookmarkService.getBookmarksByPage(pageNum, pageSize, session);
            model.addAttribute("page", page != null ? page : new Page<Bookmark>());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("tags", bookmarkService.getUserTags(session));

            return "index";
        } catch (Exception e) {
            e.printStackTrace(); // 打印完整堆栈
            model.addAttribute("error", "加载失败: " + e.getMessage());
            return "error"; // 创建 error.html 显示错误信息
        }
    }

    /**
     * 按标签查看
     */
    @GetMapping("/tag/{tag}")
    public String listByTag(@PathVariable String tag,
                            @RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/user/login";
        }

        // 修复：必须添加 currentUser 到 Model
        model.addAttribute("currentUser", currentUser);

        IPage<Bookmark> page = bookmarkService.getBookmarksByTag(tag, pageNum, pageSize, session);
        model.addAttribute("page", page);
        model.addAttribute("currentTag", tag);
        model.addAttribute("tags", bookmarkService.getUserTags(session));

        return "index";
    }

    /**
     * 搜索书签
     */
    @GetMapping("/search")
    public String search(@RequestParam String keyword,
                         @RequestParam(defaultValue = "1") Integer pageNum,
                         @RequestParam(defaultValue = "10") Integer pageSize,
                         Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/user/login";
        }

        // 修复：必须添加 currentUser 到 Model
        model.addAttribute("currentUser", currentUser);

        IPage<Bookmark> page = bookmarkService.searchBookmarks(keyword, pageNum, pageSize, session);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("tags", bookmarkService.getUserTags(session));

        return "index";
    }

    /**
     * 添加书签
     */
    @PostMapping("/add")
    @ResponseBody
    public String addBookmark(Bookmark bookmark, HttpSession session) {
        return bookmarkService.addBookmark(bookmark, session);
    }

    /**
     * 编辑书签
     */
    @PostMapping("/edit")
    @ResponseBody
    public String editBookmark(Bookmark bookmark, HttpSession session) {
        return bookmarkService.editBookmark(bookmark, session);
    }

    /**
     * 删除书签
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public String deleteBookmark(@PathVariable Long id, HttpSession session) {
        return bookmarkService.deleteBookmark(id, session);
    }

    /**
     * 书签详情
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/user/login";
        }

        Bookmark bookmark = bookmarkService.getBookmarkDetail(id);
        if (bookmark == null || !bookmark.getUserId().equals(currentUser.getId())) {
            return "redirect:/bookmark/list";
        }

        model.addAttribute("bookmark", bookmark);
        return "bookmark-detail";
    }

    /**
     * 获取书签JSON数据（用于编辑）
     */
    @GetMapping("/api/detail/{id}")
    @ResponseBody
    public Map<String, Object> getBookmarkForEdit(@PathVariable Long id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            result.put("success", false);
            result.put("message", "未登录");
            return result;
        }

        Bookmark bookmark = bookmarkService.getBookmarkDetail(id);
        if (bookmark == null || !bookmark.getUserId().equals(currentUser.getId())) {
            result.put("success", false);
            result.put("message", "无权访问此书签");
            return result;
        }

        result.put("success", true);
        result.put("id", bookmark.getId());
        result.put("title", bookmark.getTitle());
        result.put("url", bookmark.getUrl());
        result.put("description", bookmark.getDescription());
        result.put("tags", bookmark.getTags());
        return result;
    }

}
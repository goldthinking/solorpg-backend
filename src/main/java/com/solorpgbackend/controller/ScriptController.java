package com.solorpgbackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.solorpgbackend.dto.ScriptDTO;
import com.solorpgbackend.service.IScriptService;
import com.solorpgbackend.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScriptController {

    @Autowired
    private IScriptService scriptService;

    /**
     * 获取分页的剧本列表
     * @return Result<IPage<ScriptDTO>>
     */
    @GetMapping("/api/script")
    public Result getScripts(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) Integer tagId,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String searchQuery) {
        if (searchQuery != null && searchQuery.trim().isEmpty()) {
            searchQuery = null;
        }

        IPage<ScriptDTO> scriptPage = scriptService.getScriptsByPage(page, size, tagId, difficulty, searchQuery);  // 传递 searchQuery
        return Result.success(scriptPage);
    }
}

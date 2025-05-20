package com.solorpgbackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.solorpgbackend.dto.PaginatedResult;
import com.solorpgbackend.dto.ScriptDTO;
import com.solorpgbackend.service.IScriptService;
import com.solorpgbackend.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "剧本管理", description = "剧本相关接口")
public class ScriptController {
    @Autowired
    private IScriptService scriptService;

    /**
     * 获取分页的剧本列表
     * @return Result<IPage<ScriptDTO>>
     */
    @GetMapping("/api/script")
    @Operation(
            summary = "获取剧本分页列表",
            description = "根据标签、难度和搜索关键词获取剧本分页数据"
    )
    public Result<PaginatedResult<ScriptDTO>> getScripts(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) Integer tagId,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String searchQuery) {
        if (searchQuery != null && searchQuery.trim().isEmpty()) {
            searchQuery = null;
        }

        PaginatedResult<ScriptDTO> scriptPage = scriptService.getScriptsByPage(page, size, tagId, difficulty, searchQuery);  // 传递 searchQuery
        return Result.success(scriptPage);
    }
}

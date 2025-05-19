package com.solorpgbackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.solorpgbackend.common.Result;
import com.solorpgbackend.entity.Script;
import com.solorpgbackend.mapper.ScriptMapper;
import com.solorpgbackend.service.IScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Nemotte
 * @since 2025-04-25
 */
@Controller
public class ScriptController {
    @Autowired
    private IScriptService iScriptService;

    @RequestMapping(value = "/scripts", method = RequestMethod.GET)
    public Result getScripts(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer tagId,
            @RequestParam(required = false) String difficulty) {

        try {
            IPage<Script> scriptPage = iScriptService.getScriptsByPage(page, size, tagId, difficulty);

            if (scriptPage.getRecords().isEmpty()) {
                return Result.error("No scripts found");
            }

            return Result.success(scriptPage);
        } catch (Exception e) {
            return Result.error("Server error: " + e.getMessage());
        }
    }
}

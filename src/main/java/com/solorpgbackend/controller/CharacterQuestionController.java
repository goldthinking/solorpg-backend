package com.solorpgbackend.controller;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.solorpgbackend.service.ICharacterQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Nemotte
 * @since 2025-04-25
 */
@RestController
@Tag(name = "玩家提问管理", description = "玩家提问相关接口")
public class CharacterQuestionController {

    @Autowired
    private ICharacterQuestionService iCharacterQuestionService;

    @Operation(summary = "AI生成内容（流式输出）")
    @PostMapping("/api/characterQuestion/ai/fetch")
    public void fetchAI(@RequestBody Map<String, String> body, HttpServletResponse response) throws IOException, NoApiKeyException, InputRequiredException {
        String prompt = body.get("prompt");
        iCharacterQuestionService.streamAIResponse(prompt, response);
    }

}

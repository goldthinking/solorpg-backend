package com.solorpgbackend.service;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.solorpgbackend.entity.CharacterQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Nemotte
 * @since 2025-04-25
 */
public interface ICharacterQuestionService extends IService<CharacterQuestion> {
    void streamAIResponse(String prompt, HttpServletResponse response) throws IOException, NoApiKeyException, InputRequiredException;
}

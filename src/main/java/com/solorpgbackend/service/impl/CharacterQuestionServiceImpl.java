package com.solorpgbackend.service.impl;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.solorpgbackend.entity.CharacterQuestion;
import com.solorpgbackend.mapper.CharacterQuestionMapper;
import com.solorpgbackend.service.ICharacterQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.reactivex.Flowable;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Nemotte
 * @since 2025-04-25
 */
@Service
public class CharacterQuestionServiceImpl extends ServiceImpl<CharacterQuestionMapper, CharacterQuestion> implements ICharacterQuestionService {
    @Override
    public void streamAIResponse(String prompt, HttpServletResponse response) throws IOException, NoApiKeyException, InputRequiredException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");

        ApplicationParam param = ApplicationParam.builder()
                .apiKey("sk-9a02671dccc94b1dbcfff17af010605a")
                .appId("0bf2fcf6a3304c2793b6429b573552d9")
                .prompt(prompt)
                .incrementalOutput(true)
                .build();

        Application application = new Application();
        Flowable<ApplicationResult> result = application.streamCall(param);

        ServletOutputStream out = response.getOutputStream();

        result.blockingForEach(data -> {
            String text = data.getOutput().getText();
            if (text != null && !text.trim().isEmpty()) {
                String safeText = text
                        .replace("\\", "\\\\")   // 先转义反斜杠
                        .replace("\"", "\\\"")   // 再转义引号
                        .replace("\r", "")       // 去掉 CR，只保留 LF
                        .replace("\n", "\\n");   // 必须保留 \n，JSON 语义换行

                String jsonLine = "{\"response\": \"" + safeText + "\"}\n";
                out.write(jsonLine.getBytes(StandardCharsets.UTF_8));
                out.flush();
            }
        });

        out.close();
    }
}

package com.solorpgbackend.dto;

import com.solorpgbackend.entity.Script;
import com.solorpgbackend.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * <p>
 * 剧本数据传输对象（包含标签）
 * </p>
 *
 * @author Nemotte
 * @since 2025-04-25
 */

@Schema(description = "剧本信息")
public class ScriptDTO extends Script {

    @Schema(description = "剧本标签", example = "[\"恐怖\", \"悬疑\"]")
    private List<Tag> tags;  // 标签信息

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}

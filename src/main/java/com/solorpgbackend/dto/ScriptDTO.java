package com.solorpgbackend.dto;

import com.solorpgbackend.entity.Script;
import com.solorpgbackend.entity.Tag;
import java.util.List;

/**
 * <p>
 * 剧本数据传输对象（包含标签）
 * </p>
 *
 * @author Nemotte
 * @since 2025-04-25
 */
public class ScriptDTO extends Script {
    private List<Tag> tags;  // 标签信息

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}

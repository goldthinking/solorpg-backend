package com.solorpgbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Nemotte
 * @since 2025-04-25
 */
@Getter
@Setter
public class Tags implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签唯一标识
     */
    @TableId(value = "tag_id", type = IdType.AUTO)
    private Integer tagId;

    /**
     * 标签名称（例如悬疑、推理）
     */
    private String tagName;

    /**
     * 标签描述
     */
    private String description;
}

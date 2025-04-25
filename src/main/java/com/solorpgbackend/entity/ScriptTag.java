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
public class ScriptTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "script_tag_id", type = IdType.AUTO)
    private Integer scriptTagId;

    private Integer scriptId;

    private Integer tagId;
}

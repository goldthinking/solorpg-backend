package com.solorpgbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
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
@Data
public class Script implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 剧本唯一标识
     */
    @TableId(value = "script_id", type = IdType.AUTO)
    private Integer scriptId;

    /**
     * 剧本名称
     */
    private String scriptName;

    /**
     * 剧本描述
     */
    private String scriptDescription;

    /**
     * 剧本内容（JSON格式，存储剧本详细信息）
     */
    private String scriptData;

    /**
     * 剧本作者
     */
    private String author;

    /**
     * 剧本难度
     */
    private String difficulty;

    /**
     * 游玩次数
     */
    private Integer playTimes;
}

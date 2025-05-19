package com.solorpgbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

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
public class CharacterQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 提问唯一标识
     */
    @TableId(value = "question_id", type = IdType.AUTO)
    private Integer questionId;

    /**
     * 虚拟角色名称（非实体表）
     */
    private String characterName;

    /**
     * 关联对局
     */
    private Integer gameSessionId;

    /**
     * 提问内容
     */
    private String questionText;

    /**
     * 角色回答内容（含胡编但有正确信息）
     */
    private String answerText;

    /**
     * 提问时间
     */
    private LocalDateTime createdAt;
}

package com.solorpgbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class GameSession implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏局唯一标识
     */
    @TableId(value = "game_session_id", type = IdType.AUTO)
    private Integer gameSessionId;

    /**
     * 游戏开始时间
     */
    private LocalDateTime gameDate;

    /**
     * 游戏状态（进行中、已结束）
     */
    private String status;

    /**
     * 关联用户（游戏的参与者）
     */
    private Integer userId;

    /**
     * 关联剧本（游戏的模板）
     */
    private Integer scriptId;

    /**
     * AI给出的表现评分
     */
    private BigDecimal aiScore;

    /**
     * AI文字反馈内容
     */
    private String aiFeedback;

    /**
     * 评价时间
     */
    private LocalDateTime ratedAt;
}

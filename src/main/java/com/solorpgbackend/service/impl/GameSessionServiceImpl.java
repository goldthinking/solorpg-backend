package com.solorpgbackend.service.impl;

import com.solorpgbackend.entity.GameSession;
import com.solorpgbackend.mapper.GameSessionMapper;
import com.solorpgbackend.service.IGameSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Nemotte
 * @since 2025-04-25
 */
@Service
public class GameSessionServiceImpl extends ServiceImpl<GameSessionMapper, GameSession> implements IGameSessionService {

}

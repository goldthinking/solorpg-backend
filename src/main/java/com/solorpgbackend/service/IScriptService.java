package com.solorpgbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.solorpgbackend.entity.Script;
import com.baomidou.mybatisplus.extension.service.IService;
import com.solorpgbackend.mapper.ScriptMapper;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Nemotte
 * @since 2025-04-25
 */
public interface IScriptService extends IService<Script> {
    public List<Script> getAllScripts();

    IPage<Script> getScriptsByPage(int page, int size, Integer tagId, String filterDifficulty);
}

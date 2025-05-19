package com.solorpgbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.solorpgbackend.dto.PaginatedResult;
import com.solorpgbackend.dto.ScriptDTO;
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

    public PaginatedResult<ScriptDTO> getScriptsByPage(int page, int size, Integer tagId, String difficulty, String querySearch);
}

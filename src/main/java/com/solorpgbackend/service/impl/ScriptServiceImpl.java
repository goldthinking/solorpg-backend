package com.solorpgbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.solorpgbackend.dto.PaginatedResult;
import com.solorpgbackend.dto.ScriptDTO;
import com.solorpgbackend.entity.Script;
import com.solorpgbackend.entity.ScriptTag;
import com.solorpgbackend.entity.Tag;
import com.solorpgbackend.mapper.ScriptMapper;
import com.solorpgbackend.mapper.ScriptTagMapper;
import com.solorpgbackend.mapper.TagMapper;
import com.solorpgbackend.service.IScriptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Nemotte
 * @since 2025-04-25
 */
@Service
public class ScriptServiceImpl extends ServiceImpl<ScriptMapper, Script> implements IScriptService {
    @Autowired
    private ScriptMapper scriptMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ScriptTagMapper scriptTagMapper;

    @Override
    public List<Script> getAllScripts() {
        return scriptMapper.selectList(null);
    }

    @Override
    public PaginatedResult<ScriptDTO> getScriptsByPage(int page, int size, Integer tagId, String difficulty, String querySearch) {
        // 构建查询条件
        QueryWrapper<Script> queryWrapper = new QueryWrapper<>();

        // 添加难度过滤条件
        if (difficulty != null && !difficulty.isEmpty()) {
            queryWrapper.eq("difficulty", difficulty);
        }

        // 添加标签过滤条件
        if (tagId != null) {
            QueryWrapper<ScriptTag> scriptTagQuery = new QueryWrapper<>();
            scriptTagQuery.eq("tag_id", tagId);
            List<Integer> scriptIds = scriptTagMapper.selectList(scriptTagQuery)
                    .stream()
                    .map(ScriptTag::getScriptId)
                    .collect(Collectors.toList());

            if (!scriptIds.isEmpty()) {
                queryWrapper.in("script_id", scriptIds);
            } else {
                // 如果没有剧本有这个标签，返回空结果
                return new PaginatedResult<>(List.of(), 0);
            }
        }

        // 添加搜索条件
        if (querySearch != null && !querySearch.trim().isEmpty()) {
            queryWrapper.like("script_name", querySearch)
                    .or()
                    .like("script_description", querySearch);
        }

        // 先查询总数
        long total = scriptMapper.selectCount(queryWrapper);

        // 设置分页查询
        int offset = (page - 1) * size;
        queryWrapper.last("LIMIT " + offset + "," + size);

        // 执行查询，获取当前页的数据
        List<Script> scriptList = scriptMapper.selectList(queryWrapper);

        // 转换为 ScriptDTO，并加载标签信息
        List<ScriptDTO> scriptDTOs = scriptList.stream().map(script -> {
            ScriptDTO scriptDTO = new ScriptDTO();
            scriptDTO.setScriptId(script.getScriptId());
            scriptDTO.setScriptName(script.getScriptName());
            scriptDTO.setScriptDescription(script.getScriptDescription());
            scriptDTO.setScriptData(script.getScriptData());
            scriptDTO.setAuthor(script.getAuthor());
            scriptDTO.setDifficulty(script.getDifficulty());
            scriptDTO.setPlayTimes(script.getPlayTimes());

            // 加载标签信息
            List<Tag> tags = loadScriptTags(script);
            scriptDTO.setTags(tags);

            return scriptDTO;
        }).collect(Collectors.toList());

        return new PaginatedResult<>(scriptDTOs, total);
    }

    private List<Tag> loadScriptTags(Script script) {
        // 查询剧本的所有标签ID
        QueryWrapper<ScriptTag> scriptTagQuery = new QueryWrapper<>();
        scriptTagQuery.eq("script_id", script.getScriptId());
        List<Integer> tagIds = scriptTagMapper.selectList(scriptTagQuery)
                .stream()
                .map(ScriptTag::getTagId)
                .collect(Collectors.toList());

        if (!tagIds.isEmpty()) {
            // 查询这些标签的详细信息
            QueryWrapper<Tag> tagQuery = new QueryWrapper<>();
            tagQuery.in("tag_id", tagIds);
            return tagMapper.selectList(tagQuery);
        }
        return List.of();
    }
}

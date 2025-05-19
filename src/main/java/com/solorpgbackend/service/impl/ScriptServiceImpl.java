package com.solorpgbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public IPage<ScriptDTO> getScriptsByPage(int page, int size, Integer tagId, String difficulty, String searchQuery) {
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
                // 如果没有剧本有这个标签，返回空分页
                return new Page<>(page, size, 0);
            }
        }

        // 添加搜索条件
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            queryWrapper.like("script_name", searchQuery)  // 可以根据需要修改为查找剧本名称或者描述
                    .or()
                    .like("script_description", searchQuery);  // 查找描述中的内容
        }

        // 创建分页对象
        Page<Script> pageParam = new Page<>(page, size);

        // 执行分页查询
        IPage<Script> scriptPage = scriptMapper.selectPage(pageParam, queryWrapper);

        // 将每个 Script 转换为 ScriptDTO 并加载标签信息
        IPage<ScriptDTO> scriptDTOPage = new Page<>(page, size);
        scriptDTOPage.setRecords(scriptPage.getRecords().stream().map(script -> {
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
        }).collect(Collectors.toList()));

        // 设置分页信息
        scriptDTOPage.setTotal(scriptPage.getTotal()); // 设置总记录数
        scriptDTOPage.setPages((int) Math.ceil((double) scriptPage.getTotal() / size)); // 计算总页数

        return scriptDTOPage;
    }


    private int getTotalCount(Integer tagId, String difficulty) {
        // 查询符合条件的剧本总数
        QueryWrapper<Script> queryWrapper = new QueryWrapper<>();
        if (difficulty != null && !difficulty.isEmpty()) {
            queryWrapper.eq("difficulty", difficulty);
        }

        // 如果有标签过滤条件
        if (tagId != null) {
            // 查询有这个标签的所有剧本ID
            QueryWrapper<ScriptTag> scriptTagQuery = new QueryWrapper<>();
            scriptTagQuery.eq("tag_id", tagId);
            List<Integer> scriptIds = scriptTagMapper.selectList(scriptTagQuery)
                    .stream()
                    .map(ScriptTag::getScriptId)
                    .collect(Collectors.toList());

            if (!scriptIds.isEmpty()) {
                queryWrapper.in("script_id", scriptIds);
            } else {
                return 0;
            }
        }

        return Math.toIntExact(scriptMapper.selectCount(queryWrapper));
    }

    private List<ScriptDTO> getScriptsByPageData(int page, int size, Integer tagId, String difficulty) {
        // 查询当前页的剧本数据
        QueryWrapper<Script> queryWrapper = new QueryWrapper<>();
        if (difficulty != null && !difficulty.isEmpty()) {
            queryWrapper.eq("difficulty", difficulty);
        }

        // 如果有标签过滤条件
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
                return List.of();
            }
        }

        // 设置分页查询
        int offset = (page - 1) * size;
        queryWrapper.last("LIMIT " + offset + "," + size);

        // 执行查询，获取当前页的数据
        List<Script> scriptList = scriptMapper.selectList(queryWrapper);

        return scriptList.stream().map(script -> {
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

package com.solorpgbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 *  服务实现类
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
    public List<Script> getAllScripts(){
        return scriptMapper.selectList(null);
    }

    @Override
    public IPage<Script> getScriptsByPage(int page, int size, Integer tagId, String difficulty) {
        QueryWrapper<Script> queryWrapper = new QueryWrapper<>();

        // 添加难度过滤条件
        if (difficulty != null && !difficulty.isEmpty()) {
            queryWrapper.eq("difficulty", difficulty);
        }

        // 添加标签过滤条件
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
                // 如果没有剧本有这个标签，返回空分页
                return new Page<>(page, size, 0);
            }
        }

        Page<Script> pageParam = new Page<>(page, size);
        IPage<Script> scriptPage = scriptMapper.selectPage(pageParam, queryWrapper);

        // 为每个剧本加载标签信息
        scriptPage.getRecords().forEach(this::loadScriptTags);

        return scriptPage;
    }

    private void loadScriptTags(Script script) {
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
            List<Tag> tags = tagMapper.selectList(tagQuery);
            script.setTags(tags);
        }
    }
}

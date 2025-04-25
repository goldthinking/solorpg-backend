package com.solorpgbackend.service.impl;

import com.solorpgbackend.entity.Tags;
import com.solorpgbackend.mapper.TagsMapper;
import com.solorpgbackend.service.ITagsService;
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
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags> implements ITagsService {

}

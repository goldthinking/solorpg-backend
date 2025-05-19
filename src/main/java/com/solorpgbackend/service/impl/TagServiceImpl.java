package com.solorpgbackend.service.impl;

import com.solorpgbackend.entity.Tag;
import com.solorpgbackend.mapper.TagMapper;
import com.solorpgbackend.service.ITagService;
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
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

}

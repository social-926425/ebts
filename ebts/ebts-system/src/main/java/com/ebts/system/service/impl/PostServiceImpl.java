package com.ebts.system.service.impl;

import java.util.List;

import com.ebts.common.constant.ReturnConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ebts.common.constant.UserConstants;
import com.ebts.common.exception.CustomException;
import com.ebts.common.utils.StringUtils;
import com.ebts.system.entity.Post;
import com.ebts.system.dao.PostDao;
import com.ebts.system.dao.UserPostDao;
import com.ebts.system.service.PostService;

/**
 * 岗位信息 服务层处理
 *
 * @author binlin
 */
@Service
public class PostServiceImpl implements PostService {
    private Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private PostDao postMapper;

    @Autowired
    private UserPostDao userPostMapper;

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public List<Post> selectPostList(Post post) {
        try {
            return postMapper.selectPostList(post);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 查询所有岗位
     *
     * @return 岗位列表
     */
    @Override
    public List<Post> selectPostAll() {
        try {
            return postMapper.selectPostAll();
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 通过岗位ID查询岗位信息
     *
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    @Override
    public Post selectPostById(Long postId) {
        try {
            return postMapper.selectPostById(postId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 根据用户ID获取岗位选择框列表
     *
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    @Override
    public List<Integer> selectPostListByUserId(Long userId) {
        try {
            return postMapper.selectPostListByUserId(userId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 校验岗位名称是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkPostNameUnique(Post post) {
        try {
            Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
            Post info = postMapper.checkPostNameUnique(post.getPostName());
            if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
                return UserConstants.NOT_UNIQUE;
            }
            return UserConstants.UNIQUE;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 校验岗位编码是否唯一
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkPostCodeUnique(Post post) {
        try {
            Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
            Post info = postMapper.checkPostCodeUnique(post.getPostCode());
            if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
                return UserConstants.NOT_UNIQUE;
            }
            return UserConstants.UNIQUE;
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int countUserPostById(Long postId) {
        try {
            return userPostMapper.countUserPostById(postId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 删除岗位信息
     *
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int deletePostById(Long postId) {
        try {
            return postMapper.deletePostById(postId);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 批量删除岗位信息
     *
     * @param postIds 需要删除的岗位ID
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public int deletePostByIds(Long[] postIds) {
        try {
            for (Long postId : postIds) {
                Post post = selectPostById(postId);
                if (countUserPostById(postId) > 0) {
                    throw new CustomException(String.format("%1$s已分配,不能删除", post.getPostName()));
                }
            }
            return postMapper.deletePostByIds(postIds);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 新增保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int insertPost(Post post) {
        try {
            return postMapper.insertPost(post);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }

    /**
     * 修改保存岗位信息
     *
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int updatePost(Post post) {
        try {
            return postMapper.updatePost(post);
        }catch (RuntimeException e){
            logger.error(e.getMessage());
            throw new CustomException(ReturnConstants.OP_ERROR);
        }
    }
}

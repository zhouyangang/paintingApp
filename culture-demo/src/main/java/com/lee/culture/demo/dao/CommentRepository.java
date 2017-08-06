package com.lee.culture.demo.dao;

import com.lee.culture.demo.po.CommentInfoEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @Author: joe
 * @Date: 17-8-5 下午9:59.
 * @Description:
 */
public interface CommentRepository extends CrudRepository<CommentInfoEntity, Integer> {
}

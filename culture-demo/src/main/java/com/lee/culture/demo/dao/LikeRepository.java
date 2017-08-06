package com.lee.culture.demo.dao;

import com.lee.culture.demo.po.LikeInfoEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @Author: joe
 * @Date: 17-8-5 下午9:58.
 * @Description:
 */
public interface LikeRepository extends CrudRepository<LikeInfoEntity, Integer> {
}

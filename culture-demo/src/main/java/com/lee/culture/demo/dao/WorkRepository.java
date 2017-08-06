package com.lee.culture.demo.dao;

import com.lee.culture.demo.po.WorkInfoEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @Author: joe
 * @Date: 17-8-5 下午9:57.
 * @Description:
 */
public interface WorkRepository extends CrudRepository<WorkInfoEntity, Integer> {
}

package com.lee.culture.demo.service.impl;

import com.lee.culture.demo.dao.WorkRepository;
import com.lee.culture.demo.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: joe
 * @Date: 17-8-5 下午10:06.
 * @Description:
 */
@Service
public class WorkServiceImpl implements WorkService {

    @Autowired
    private WorkRepository workDao;
}

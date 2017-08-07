package com.lee.culture.demo.service.impl;

import com.lee.culture.demo.ServiceException;
import com.lee.culture.demo.bean.request.UserLoginDto;
import com.lee.culture.demo.bean.request.UserProfileDto;
import com.lee.culture.demo.dao.UserRepository;
import com.lee.culture.demo.po.UserInfoEntity;
import com.lee.culture.demo.service.OssService;
import com.lee.culture.demo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: joe
 * @Date: 17-8-5 下午10:05.
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private OssService ossService;

    @Value("${oss.upload-path}")
    private String uploadPath;

    @Override
    public UserInfoEntity findByPhone(String phone) {
        return userDao.findByPhone(phone);
    }

    @Override
    @Transactional
    public UserInfoEntity signUp(UserLoginDto dto) {
        UserInfoEntity exist = userDao.findByPhone(dto.getPhone());
        if (exist != null) {
            throw new ServiceException("手机已存在");
        }

        UserInfoEntity entity = new UserInfoEntity();
        entity.setPhone(dto.getPhone());
        entity.setPwd(dto.getPassword());

        entity.setRegisterTime(new Date());

        return userDao.save(entity);
    }

    @Override
    public UserInfoEntity findById(Integer userId) {
        return userDao.findOne(userId);
    }

    @Override
    @Transactional
    public UserInfoEntity updateProfile(UserInfoEntity user, UserProfileDto dto) {

        if (StringUtils.isNotBlank(dto.getAlias())) {
            user.setAliasName(dto.getAlias());
        }

        if (StringUtils.isNotBlank(dto.getDescription())) {
            user.setDescription(dto.getDescription());
        }

        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }

        if (StringUtils.isNotBlank(dto.getProfileUrl())) {
            user.setProfilePath(dto.getProfileUrl());
        }

        return userDao.save(user);
    }

    @Override
    public String uploadAvastar(UserInfoEntity loginUser, MultipartFile picture) throws IOException {
        final String uniqueName = UUID.randomUUID().toString();
        final java.nio.file.Path filePath = Paths.get(uploadPath, uniqueName);
        Files.copy(picture.getInputStream(), filePath);
        final String newUniqueName = UUID.randomUUID().toString();
        final java.nio.file.Path newFilePath = Paths.get(uploadPath, newUniqueName);
        ImageIO.write(ImageIO.read(filePath.toFile()), "jpeg", newFilePath.toFile());
        filePath.toFile().delete();

        URL url = ossService.uploadWork(newUniqueName, newFilePath.getFileName().toString());

        UserProfileDto dto = new UserProfileDto();
        dto.setProfileUrl(url.toString());
        updateProfile(loginUser, dto);

        return url.toString();
    }

}

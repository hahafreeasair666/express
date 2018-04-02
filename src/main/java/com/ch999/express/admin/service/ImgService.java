package com.ch999.express.admin.service;

import com.ch999.express.admin.entity.Image;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hahalala
 */
public interface ImgService {

    Image uploadImg(MultipartFile file);
}

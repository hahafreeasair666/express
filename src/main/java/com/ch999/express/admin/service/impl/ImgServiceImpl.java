package com.ch999.express.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.ch999.express.admin.entity.Image;
import com.ch999.express.admin.service.ImageService;
import com.ch999.express.admin.service.ImgService;
import com.ch999.express.common.HttpClientUtil;
import com.ch999.express.common.ImageRes;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 处理图片的服务类
 *
 * @author hahalala
 */
@Service
@Slf4j
public class ImgServiceImpl implements ImgService {

    private static final String IMG_SERVER = "http://120.79.160.214:9333";

    private static final String IMG_UPLOAD_URL = "/submit";

    //private ReentrantLock lock = new ReentrantLock();

    @Resource
    private ImageService imgsService;

    @Override
    public Image uploadImg(MultipartFile file) {
        File file1 = null;
        try {
            if (file.getSize() >= 1024 * 100) {
                //lock.lock();
                log.info("图片压缩");
                String str = IdWorker.get32UUID();
                Thumbnails.of(file.getInputStream()).size(200, 200).toFile( str + ".jpg");
                file1 = new File(str + ".jpg");
                log.info("压缩完毕");
                //lock.unlock();
            }
        } catch (Exception e) {
            log.error("压缩图片异常");
        }
        Image imgs = new Image();
        String s = HttpClientUtil.uploadFile(IMG_SERVER, IMG_UPLOAD_URL, file, file1);
        if(file1 != null) {
            file1.delete();
            log.info("上传完毕，删除压缩图片");
        }
        ImageRes imageRes = JSON.parseObject(s, ImageRes.class);
        String fid = imageRes.getFid();
        String fileName = imageRes.getFileName();
        Integer size = imageRes.getSize();
        if (StringUtils.isBlank(fid) || size == null || size == 0) {
            return null;
        }
        imgs.setName(fileName);
        imgs.setUrl("http://" + imageRes.getFileUrl().replace("localhost", "120.79.160.214"));
        imgsService.insert(imgs);
        return imgs;
    }

    public static boolean checkIsImg(String name) {
        String[] split = name.split("\\.");
        if (split.length < 2) {
            return false;
        } else if ("jpg".equals(split[1])) {
            return true;
        } else if ("png".equals(split[1])) {
            return true;
        } else if ("gif".equals(split[1])) {
            return true;
        } else if ("jpeg".equals(split[1])) {
            return true;
        } else if ("bmp".equals(split[1])) {
            return true;
        }
        return false;
    }

    /*private MultipartFile createImg(File file){
        try {
            FileInputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
            //注意这里面填啥，MultipartFile里面对应的参数就有啥，比如我只填了name，则
            //MultipartFile.getName()只能拿到name参数，但是originalFilename是空。
            return multipartFile;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }*/

}

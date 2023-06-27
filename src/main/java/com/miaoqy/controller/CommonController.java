package com.miaoqy.controller;

import com.miaoqy.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传与下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 上传
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file是临时文件，需要转存指定位置，否则请求完后会被删除
        log.info("获取文件：{}", file.toString());
        //判断一下当前目录是否存在，不存在则创建
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //获取一下传入的原文件名
        String originalFilename = file.getOriginalFilename();
        //我们只需要获取一下格式后缀，取子串，起始点为最后一个.
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //为了防止出现重复的文件名，我们需要使用UUID
        String fileName = UUID.randomUUID() + suffix;
        try {
            //我们将其转存到我们的指定目录下
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //将文件名返回给前端，便于后期的开发
        return R.success(fileName);

    }

    /**
     * 下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        FileInputStream fis = null;
        ServletOutputStream os = null;
        try {
            fis = new FileInputStream(basePath + name);
            os = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1)
                os.write(buffer, 0, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fis != null) {

                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/11/3 10:51
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${savePath}")
    private String basePath;

    /**
     * 上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R upload(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        //生成随机id 防止id重复
        String name = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(filename);
        //保存文件
        file.transferTo(new File(basePath + name));
        return R.success(name);
    }


    /**
     * 下载
     *
     * @param name
     * @param response
     * @return
     */
    @GetMapping("/download")
    public R download(String name, HttpServletResponse response) throws IOException {
        FileInputStream fis = new FileInputStream(new File(basePath + name));
        ServletOutputStream os = response.getOutputStream();
        IOUtils.copy(fis, os);
        return R.success("bingo");
    }


}

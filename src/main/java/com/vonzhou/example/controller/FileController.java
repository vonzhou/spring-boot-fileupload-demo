package com.vonzhou.example.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by vonzhou on 2018/4/23.
 */
@RestController
public class FileController {


    @PostMapping("/file")
    public String upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {

        System.out.println(multipartFile.getOriginalFilename() + ", " + multipartFile.getSize());

        String out = "/tmp/a";

        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(out));


        return "ok";
    }
}

package com.pinyougou.manager.controller;


import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;


@RestController
public class UploadController {
	//由于在springmvc中已经引入了配置文件的约束：<context:property-placeholder location="classpath:config/application.properties" />
	//所以可以直接注入
    @Value("${FILE_SERVER_URL}")
    private String file_server_url;
   
    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
     //MultipartFile file是前端前端传递给后端的文件


        String originalFilename = file.getOriginalFilename();//获取文件的全名称（包括文件的扩展名）
        String extName = originalFilename.substring(originalFilename.lastIndexOf("."));//获取扩展名
        
        try {
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            String fileId = client.uploadFile(file.getBytes(), extName);
            String url = file_server_url+fileId;//图片的完整地址
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"上传失败");
        }
    }
}

package com.pinyougou.shop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlProcessor.ResolutionMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import entity.Result;
import util.FastDFSClient;

@RestController
public class UploadController {
	
	@Value("${FILE_SERVER_URL}")
	private String file_server_url;
	
	@RequestMapping("/upload")
	public Result upload(MultipartFile file){
		// 获取文件的名称
		String fileName = file.getOriginalFilename();
		// 获取文件的扩展名
		String extName = fileName.substring(fileName.lastIndexOf(".")+1);
		
		
		try {
			FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
			String fileId = client.uploadFile(file.getBytes(),extName);
			String url = file_server_url + fileId;
			
			return new Result(true, url);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			return new Result(false, "上传失败");
		}
	}
}

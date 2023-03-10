package com.report;


import com.report.entity.FileData;
import com.report.entity.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Files;

/**
 * @ClassName MinioService
 * @Description TODO
 * @Author QiBin
 * @Date 2023/1/31 17:54
 * @Version 1.0
 **/
@Component
@Slf4j
public class MinioService {


    @Resource
    MinioProperties minioProperties;
    /**
     * 首页下载excel
     * @param file
     * @return
     */
    public FileData uploadOneFile1(File file) throws Exception{
        String bucketName = minioProperties.getBucketName();

        uploadFi(bucketName, file, file.getName());
        String url = getObjectUrl(bucketName,file.getName());
        FileData fileData = new FileData();
        fileData.setUrl(url);
        fileData.setBucketName(bucketName);
        fileData.setObjectName(file.getName());
        return fileData;
    }


    public void uploadFi(String bucketName, File file, String fileName) throws Exception {

//        String s = Files.probeContentType(file.toPath());
        String s = MediaTypeFactory.getMediaType(fileName).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();

        if (StringUtils.isBlank(s)) {
            s="text/plain";
        }
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                //.contentType(new MimetypesFileTypeMap().getContentType(file))
                .contentType(s)
                .object(fileName)
                .stream(Files.newInputStream(file.toPath()), file.length(), -1)
                .build();
        minioClient.putObject(putObjectArgs);
    }

    /**
     * 文件访问路径
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @return
     */
    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName){
        boolean flag = bucketExists(bucketName);
        String url = "";
        if (flag) {
            GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .build();
            url = minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
        }
        return url;
    }
    @Resource
    private MinioClient minioClient;
    /**
     * 检查桶是否存在
     * @param bucketName
     * @return
     * @throws Exception
     */
    public boolean bucketExists(String bucketName) throws Exception {
        boolean flag = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (flag) {
            log.info("{} exists", bucketName);
            return true;
        } else {
            log.info("{} does not exist",bucketName);
            return false;
        }
    }
    /**
     * 上传文件
     *
     * @param multipartFile
     */
    @SneakyThrows
    public void uploadMu(String bucketName, MultipartFile multipartFile,String fileName) {
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .contentType(multipartFile.getContentType())
                .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                .build();
        minioClient.putObject(putObjectArgs);
    }

}

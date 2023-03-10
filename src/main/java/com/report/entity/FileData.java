package com.report.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName FileData
 * @Description TODO
 * @Author QiBin
 * @Date 2023/1/10 09:47
 * @Version 1.0
 **/
@Data
@ApiModel(value = "文件返回值",description = "文件返回值")
public class FileData implements Serializable {

    private static final long serialVersionUID = 2909024418136560072L;
    @ApiModelProperty(value = "文件url")
    /**
     * 文件url
     */
    private String url;
    @ApiModelProperty(value = "桶")
    private String bucketName;
    @ApiModelProperty(value = "文件名")
    private String objectName;
    @ApiModelProperty(value = "文件类型")
    private String fileTypeCode;
    @ApiModelProperty(value = "文件类型名称")
    private String fileTypeName;
    @ApiModelProperty(value = "文件长度")
    private Long fileLength;
}

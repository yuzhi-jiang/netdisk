package com.yefeng.netdisk.front.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-03-19 10:18
 */
@Data
public class FileParamBo {
    String search;
    @NotBlank(message = "diskId不能为空")
    String diskId;
    String fileId;

    String parentFileId;
    //file or folder
    String type;

    // name asc/desc
    String order;
    @Min(value = 1, message = "分页参数最小值为1")
    @NotBlank(message = "pageNum不能为空")

    Integer pageNum;
    @Max(value = 500, message = "每页最多500")
    @NotBlank(message = "pageSize不能为空")
    Integer pageSize;
    Integer limit;
    @JsonProperty("page")
    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

}

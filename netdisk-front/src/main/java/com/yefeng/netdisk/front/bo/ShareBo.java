package com.yefeng.netdisk.front.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-25 11:24
 */
@Data
public class ShareBo {
    /** file/list_by_share
     * [
     *   {
     *     "drive_id": "358565",
     *     "domain_id": "bj29",
     *     "file_id": "605419f2240cbc69520f4c03bf92385ffb809faa",
     *     "share_id": "FiJqoy1sbBr",
     *     "name": "案例三——学生信息管理系统需求说明书1.01.doc",
     *     "type": "file",
     *     "created_at": "2021-03-19T03:26:42.426Z",
     *     "updated_at": "2021-08-26T11:02:16.743Z",
     *     "file_extension": "doc",
     *     "mime_type": "application/msword",
     *     "mime_extension": "doc",
     *     "size": 1163264,
     *     "parent_file_id": "root",
     *     "thumbnail": "https://pdsapi.aliyundrive.com/v2/redirect?id=7d58d56705eb4b6183df1526327620a71677296796478094119",
     *     "category": "doc",
     *     "punish_flag": 0,
     *     "revision_id": "605419f24d121f086072473382a1c3a181fe6916"
     *   },
     *   {
     *     "drive_id": "358565",
     *     "domain_id": "bj29",
     *     "file_id": "5fbb487d7a5bb0e1f9be4d08a0f91bbf7f7e9b01",
     *     "share_id": "FiJqoy1sbBr",
     *     "name": "双十一.js",
     *     "type": "file",
     *     "created_at": "2020-11-23T05:28:29.843Z",
     *     "updated_at": "2021-08-26T11:11:23.357Z",
     *     "file_extension": "js",
     *     "mime_type": "text/plain; charset=utf-8",
     *     "mime_extension": "txt",
     *     "size": 15018,
     *     "parent_file_id": "root",
     *     "category": "others",
     *     "punish_flag": 0,
     *     "revision_id": "5fbb487d563a78ab2b6f42f9b4e381489bc9e2fe"
     *   }
     * ]
     */



    /**
     * {
     *   "expiration": "2023-03-27T03:30:06.400Z",
     *   "sync_to_homepage": false,
     *   "share_pwd": "",
     *   "drive_id": "358565",
     *   "file_id_list": [
     *     "5fbb487d7a5bb0e1f9be4d08a0f91bbf7f7e9b01",
     *     "605419f2240cbc69520f4c03bf92385ffb809faa"
     *   ]
     * }
     *
     *
     *
     *
     * {
     *   "category": "file",
     *   "popularity": 3,
     *   "share_id": "FiJqoy1sbBr",
     *   "share_msg": "「双十一.js」等文件，点击链接保存，或者复制本段内容，打开「阿里云盘」APP ，无需下载极速在线查看，视频原画倍速播放。",
     *   "share_name": "双十一.js",
     *   "description": "",
     *   "expiration": "2023-03-27T03:30:06.400Z",
     *   "expired": false,
     *   "share_pwd": "",
     *   "share_url": "https://www.aliyundrive.com/s/FiJqoy1sbBr",
     *   "creator": "17d0f46b8fe8448c89af8bd03ca04380",
     *   "drive_id": "358565",
     *   "file_id": "5fbb487d7a5bb0e1f9be4d08a0f91bbf7f7e9b01",
     *   "file_id_list": [
     *     "5fbb487d7a5bb0e1f9be4d08a0f91bbf7f7e9b01",
     *     "605419f2240cbc69520f4c03bf92385ffb809faa"
     *   ],
     *   "preview_count": 0,
     *   "save_count": 0,
     *   "download_count": 0,
     *   "status": "enabled",
     *   "created_at": "2023-02-25T03:30:20.694Z",
     *   "updated_at": "2023-02-25T03:30:20.694Z",

     *   "enable_file_changed_notify": true,
     *   "is_photo_collection": false,
     *   "sync_to_homepage": false,
     *   "popularity_str": "3",
     *   "share_title": "双十一.js 等文件",
     *   "full_share_msg": "双十一.js 等文件\nhttps://www.aliyundrive.com/s/FiJqoy1sbBr\n点击链接保存，或者复制本段内容，打开「阿里云盘」APP ，无需下载极速在线查看，视频原画倍速播放。",
     *   "share_subtitle": "打开阿里云盘App，无需下载极速在线查看，视频原画倍速播放。",
     *   "display_name": "双十一.js 等 2 个文件"
     * }
     */

    @ApiModelProperty(value = "文件id列表",dataType = "Stinrg[]")
    String[] fileIdList;
    @ApiModelProperty("分享密码")
    String sharePwd;
    @ApiModelProperty("云盘id")
    String diskId;


    @ApiModelProperty("过期时间,格式为: yyyy-MM-dd HH:mm:ss")

    String expiration;
    //分享文件的类型 1，文件，2，文件夹，3.txt,4.doc,5.other
    @ApiModelProperty(value = "分享文件的类型 1，文件，2，文件夹，3.txt,4.doc,5.other",dataType = "Byte")
    Byte type;
}

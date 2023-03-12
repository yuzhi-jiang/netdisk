/*
 Navicat Premium Data Transfer

 Source Server         : 本地mysql
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : netdisk

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 12/03/2023 23:14:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `param_key`(`param_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统配置信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'CLOUD_STORAGE_CONFIG_KEY', '{\"type\":1,\"qiniuDomain\":\"http://ppkn5nh6t.bkt.clouddn.com\",\"qiniuPrefix\":\"upload\",\"qiniuAccessKey\":\"YLKWjJFNCjYHiVw6-E7RnqscoVfgiHpzqdBSn-HW\",\"qiniuSecretKey\":\"FpQmCJcePJ-HfGJHSAcf55O6di6yQ0FtrbNkdwyF\",\"qiniuBucketName\":\"disk\",\"aliyunDomain\":\"\",\"aliyunPrefix\":\"\",\"aliyunEndPoint\":\"\",\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qcloudBucketName\":\"\"}', 1, '云存储配置信息', NULL, '2023-03-11 13:54:44', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
  `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典类型',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典码',
  `value` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典值',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除标记  -1：已删除  0：正常',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `type`(`type`, `code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, '性别', 'sex', '0', '女', 0, NULL, 0, NULL, '2023-03-11 13:54:52', NULL, NULL);
INSERT INTO `sys_dict` VALUES (2, '性别', 'sex', '1', '男', 1, NULL, 0, NULL, '2023-03-11 13:54:52', NULL, NULL);
INSERT INTO `sys_dict` VALUES (3, '性别', 'sex', '2', '未知', 3, NULL, 0, NULL, '2023-03-11 13:54:52', NULL, NULL);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (647, NULL, '', 'com.yefeng.account.controller.UserController.getinfo()', NULL, 3, '0:0:0:0:0:0:0:1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (648, NULL, '测试获取信息', 'com.yefeng.account.controller.UserController.getinfo()', '\"123\"', 5, '127.0.0.1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (649, NULL, '测试获取信息', 'com.yefeng.account.controller.UserController.getinfo()', '\"asdf\"', 6, '0:0:0:0:0:0:0:1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (650, NULL, '测试获取信息', 'com.yefeng.account.controller.UserController.getinfo()', '\"asdf\"', 0, '127.0.0.1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (651, NULL, '测试获取信息', 'com.yefeng.account.controller.UserController.getinfo()', '\"asdf\"', 0, '0:0:0:0:0:0:0:1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (652, NULL, '测试获取信息', 'com.yefeng.account.controller.UserController.getinfo()', '\"asdf123\"', 0, '0:0:0:0:0:0:0:1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (653, NULL, '测试获取信息', 'com.yefeng.account.controller.UserController.getinfo()', '\"sss\"', 0, '127.0.0.1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (654, NULL, '测试获取信息', 'com.yefeng.account.controller.UserController.getinfo()', '\"asdf\"', 5, '0:0:0:0:0:0:0:1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (655, NULL, '测试获取信息', 'com.yefeng.account.controller.UserController.getinfo()', '\"5555\"', 84, '0:0:0:0:0:0:0:1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (656, NULL, '测试获取信息', 'com.yefeng.account.controller.UserController.getinfo()', '\"5555\"', 3, '0:0:0:0:0:0:0:1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (657, NULL, '测试获取信息', 'com.yefeng.account.controller.UserController.getinfo()', '\"5555\"', 5, '0:0:0:0:0:0:0:1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (658, '1234324:yefeng', '测试获取信息', 'com.yefeng.account.controller.UserController.getinfo()', '\"5555\"', 4, '0:0:0:0:0:0:0:1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (659, 'null:null', '测试获取信息', 'com.yefeng.netdisk.front.controller.UserController.getinfo()', '\"12\"', 6, '0:0:0:0:0:0:0:1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (660, 'null:null', '测试获取信息', 'com.yefeng.netdisk.front.controller.UserController.getinfo()', '\"\"', 2, '0:0:0:0:0:0:0:1', NULL, '2023-03-11 13:55:00', NULL, NULL);
INSERT INTO `sys_log` VALUES (661, 'null:null', '测试获取信息', 'com.yefeng.netdisk.front.controller.UserController.getinfo()', '\"12\"', 5, '0:0:0:0:0:0:0:1', NULL, '2023-03-11 13:55:00', NULL, NULL);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 105 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', NULL, NULL, 0, 'fa fa-cog', 3, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (2, 49, '后台用户', 'modules/sys/user.html', NULL, 1, 'fa fa-user', 1, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (3, 54, '用户角色', 'modules/sys/role.html', NULL, 1, 'fa fa-user-secret', 2, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', 'modules/sys/menu.html', NULL, 1, 'fa fa-th-list', 3, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (5, 55, 'SQL监控', 'druid/sql.html', NULL, 1, 'fa fa-bug', 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (6, 1, '定时任务', 'modules/job/schedule.html', NULL, 1, 'fa fa-tasks', 5, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (7, 6, '查看', NULL, 'sys:schedule:list,sys:schedule:info', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (8, 6, '新增', NULL, 'sys:schedule:save', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (9, 6, '修改', NULL, 'sys:schedule:update', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (10, 6, '删除', NULL, 'sys:schedule:delete', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (11, 6, '暂停', NULL, 'sys:schedule:pause', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (12, 6, '恢复', NULL, 'sys:schedule:resume', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (13, 6, '立即执行', NULL, 'sys:schedule:run', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (14, 6, '日志列表', NULL, 'sys:schedule:log', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (16, 2, '新增', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:perms', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:perms', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (27, 1, '参数管理', 'modules/sys/config.html', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', 1, 'fa fa-sun-o', 6, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (29, 50, '系统日志', 'modules/sys/log.html', 'sys:log:list', 1, 'fa fa-file-text-o', 7, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (30, 1, '文件上传', 'modules/oss/oss.html', 'sys:oss:all', 1, 'fa fa-file-image-o', 6, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (31, 53, '部门管理', 'modules/sys/dept.html', NULL, 1, 'fa fa-file-code-o', 1, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (32, 31, '查看', NULL, 'sys:dept:list,sys:dept:info', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (33, 31, '新增', NULL, 'sys:dept:save,sys:dept:select', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (34, 31, '修改', NULL, 'sys:dept:update,sys:dept:select', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (35, 31, '删除', NULL, 'sys:dept:delete', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (36, 1, '字典管理', 'modules/sys/dict.html', NULL, 1, 'fa fa-bookmark-o', 6, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (37, 36, '查看', NULL, 'sys:dict:list,sys:dict:info', 2, NULL, 6, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (38, 36, '新增', NULL, 'sys:dict:save', 2, NULL, 6, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (39, 36, '修改', NULL, 'sys:dict:update', 2, NULL, 6, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (40, 36, '删除', NULL, 'sys:dict:delete', 2, NULL, 6, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (41, 0, '其他管理', NULL, NULL, 0, 'fa fa-cog', 3, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (43, 0, '网盘管理', NULL, NULL, 0, 'fa fa-cog', 2, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (49, 0, '用户管理', NULL, NULL, 0, 'fa fa-user', 1, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (50, 0, '日志管理', NULL, NULL, 0, 'fa fa-file-text-o', 2, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (51, 41, '接口文档', '/swagger/index.html', NULL, 1, 'fa fa-file', 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (52, 0, '数据大盘', 'main.html', NULL, 1, 'fa fa-tachometer', 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (53, 0, '部门管理', NULL, NULL, 0, 'fa fa-id-card-o', 1, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (54, 0, '角色管理', NULL, NULL, 0, 'fa fa-user-secret', 1, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (55, 0, '系统监控', NULL, NULL, 0, 'fa fa-bar-chart', 3, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (56, 55, 'Hadoop监控', 'hadoop.html', NULL, 1, 'fa fa-eye', 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (58, 49, '前台用户', 'modules/front/user.html', NULL, 1, 'fa fa-user', 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (59, 58, '查询', NULL, 'front:user:list,front:user:info', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (60, 58, '新增', NULL, 'front:user:save', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (61, 58, '修改', NULL, 'front:user:update', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (62, 58, '删除', NULL, '', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (63, 29, '清除', NULL, 'sys:log:delete', 2, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (89, 55, 'Eureka注册中心', 'http://localhost:8761/', NULL, 1, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (90, 55, '服务监控', 'http://localhost:9010', NULL, 1, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (91, 41, '文件预览', 'http://193.112.27.123:8012/index', NULL, 1, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (92, 43, '企业网盘', 'modules/front/disk.html', NULL, 1, NULL, 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (93, 55, '服务器连接', 'http://193.112.27.123:2222/ssh/host/193.112.27.123', NULL, 1, 'fa fa-link', 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (94, 55, '服务器监控', 'modules/sys/server.html', NULL, 1, 'fa fa-video-camera', 0, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (99, 95, '删除', '', 'front:sysnotice:delete', 2, NULL, 6, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (100, 1, '通知公告', 'modules/sys/sysnotice.html', NULL, 1, 'fa fa-file-code-o', 6, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (101, 100, '查看', NULL, 'sys:sysnotice:list,sys:sysnotice:info', 2, NULL, 6, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (102, 100, '新增', NULL, 'sys:sysnotice:save', 2, NULL, 6, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (103, 100, '修改', NULL, 'sys:sysnotice:update', 2, NULL, 6, NULL, '2023-03-11 13:55:05', NULL, NULL);
INSERT INTO `sys_menu` VALUES (104, 100, '删除', NULL, 'sys:sysnotice:delete', 2, NULL, 6, NULL, '2023-03-11 13:55:05', NULL, NULL);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` tinyint(4) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告内容',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '公告状态（0正常 1关闭）',
  `last_ver` smallint(6) NOT NULL DEFAULT 0 COMMENT '版本号',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (3, '有奖征文|程序员/媛 恋爱、结婚这件小事', 2, '春暖花开，芬芳四溢，CSDN博客特举行“程序员（媛）恋爱/结婚”为主题的征文活动，大声说出你们的爱', 0, 0, '', NULL, '2023-03-11 13:55:14', NULL, NULL);
INSERT INTO `sys_notice` VALUES (6, '你为什么还不努力？', 2, '你比他好一点，他不会承认你，反而会嫉妒你，只有你比他好很多，他才会承认你，然后还会很崇拜你，所以要做，就一定要比别人做得好很多!', 0, 0, '', NULL, '2023-03-11 13:55:14', NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1499 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1073, 1, 1, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1074, 1, 4, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1075, 1, 23, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1076, 1, 24, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1077, 1, 25, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1078, 1, 26, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1079, 1, 6, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1080, 1, 7, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1081, 1, 8, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1082, 1, 9, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1083, 1, 10, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1084, 1, 11, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1085, 1, 12, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1086, 1, 13, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1087, 1, 14, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1088, 1, 27, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1089, 1, 30, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1090, 1, 36, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1091, 1, 37, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1092, 1, 38, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1093, 1, 39, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1094, 1, 40, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1095, 1, 41, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1096, 1, 51, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1097, 1, 91, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1098, 1, 43, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1099, 1, 64, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1100, 1, 65, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1101, 1, 66, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1102, 1, 67, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1103, 1, 68, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1104, 1, 69, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1106, 1, 71, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1107, 1, 72, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1108, 1, 73, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1109, 1, 74, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1110, 1, 75, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1111, 1, 76, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1119, 1, 92, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1120, 1, 49, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1121, 1, 2, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1122, 1, 15, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1123, 1, 16, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1124, 1, 17, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1125, 1, 18, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1126, 1, 58, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1127, 1, 59, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1128, 1, 60, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1129, 1, 61, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1130, 1, 50, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1131, 1, 29, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1132, 1, 63, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1133, 1, 52, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1134, 1, 53, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1135, 1, 31, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1136, 1, 32, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1137, 1, 33, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1138, 1, 34, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1139, 1, 35, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1140, 1, 54, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1141, 1, 3, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1142, 1, 19, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1143, 1, 20, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1144, 1, 21, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1145, 1, 22, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1146, 1, 55, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1147, 1, 5, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1148, 1, 56, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1150, 1, 89, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1151, 1, 90, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1325, 3, 1, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1326, 3, 27, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1327, 3, 30, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1328, 3, 43, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1329, 3, 64, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1330, 3, 65, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1331, 3, 66, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1332, 3, 67, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1333, 3, 68, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1334, 3, 69, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1336, 3, 71, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1337, 3, 72, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1338, 3, 73, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1339, 3, 74, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1340, 3, 75, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1341, 3, 76, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1349, 3, 92, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1350, 3, 29, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1351, 3, 55, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1352, 3, 5, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1353, 3, 56, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1355, 3, 89, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1356, 3, 90, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1357, 3, 93, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1358, 3, 94, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1429, 2, 1, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1430, 2, 4, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1431, 2, 23, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1432, 2, 24, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1433, 2, 25, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1434, 2, 26, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1435, 2, 6, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1436, 2, 7, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1437, 2, 8, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1438, 2, 9, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1439, 2, 10, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1440, 2, 11, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1441, 2, 12, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1442, 2, 13, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1443, 2, 14, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1444, 2, 27, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1445, 2, 30, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1446, 2, 36, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1447, 2, 37, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1448, 2, 38, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1449, 2, 39, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1450, 2, 40, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1451, 2, 100, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1452, 2, 101, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1453, 2, 102, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1454, 2, 103, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1455, 2, 104, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1456, 2, 43, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1457, 2, 64, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1458, 2, 65, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1459, 2, 66, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1460, 2, 67, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1461, 2, 68, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1462, 2, 69, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1464, 2, 71, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1465, 2, 72, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1466, 2, 73, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1467, 2, 74, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1468, 2, 75, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1469, 2, 76, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1477, 2, 92, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1478, 2, 49, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1479, 2, 58, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1480, 2, 59, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1481, 2, 60, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1482, 2, 61, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1483, 2, 62, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1484, 2, 50, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1485, 2, 29, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1486, 2, 52, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1487, 2, 31, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1488, 2, 32, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1489, 2, 33, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1490, 2, 34, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1491, 2, 55, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1492, 2, 5, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1493, 2, 56, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1495, 2, 89, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1496, 2, 90, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1497, 2, 93, NULL, '2023-03-11 13:55:48', NULL, NULL);
INSERT INTO `sys_role_menu` VALUES (1498, 2, 94, NULL, '2023-03-11 13:55:48', NULL, NULL);

-- ----------------------------
-- Table structure for tb_disk
-- ----------------------------
DROP TABLE IF EXISTS `tb_disk`;
CREATE TABLE `tb_disk`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `total_capacity` bigint(20) NOT NULL,
  `use_capacity` bigint(20) NULL DEFAULT NULL,
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1617169131327459333 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_disk
-- ----------------------------
INSERT INTO `tb_disk` VALUES (1617169131327459332, 25, 524288000, 0, NULL, '2023-03-12 11:09:30', NULL, NULL);
INSERT INTO `tb_disk` VALUES (1617169131327459337, 32, 524288000, 0, NULL, '2023-03-12 17:54:44', NULL, NULL);

-- ----------------------------
-- Table structure for tb_disk_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_disk_file`;
CREATE TABLE `tb_disk_file`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `disk_id` bigint(20) NULL DEFAULT NULL,
  `disk_file_id` char(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '云盘文件/文件夹id',
  `file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户文件的名称（同样的文件每个人文件有不同的名称）',
  `parent_file_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'root' COMMENT '用户文件的父文件夹名称（同样的文件每个人文件有不同的路径）',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '文件类型1文件2文件夹',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '文件状态0待上传,1.已经成功上传2.激活可用3.不可用',
  `file_id` bigint(20) NULL DEFAULT NULL COMMENT '数据库文件id',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_disk_file
-- ----------------------------
INSERT INTO `tb_disk_file` VALUES (23, 1617169131327459332, '8nc1w0vx63njwjc6ant0m9ur0huko9qzgy5uaub3', '文件夹1', 'dd979svzrf4phoxtkjf8ybtbiu8glmcewft2vyqx', 2, 3, NULL, NULL, '2023-03-12 12:08:01', NULL, NULL);
INSERT INTO `tb_disk_file` VALUES (25, 1617169131327459332, 'dd979svzrf4phoxtkjf8ybtbiu8glmcewft2vyqx', '文件夹2', 'root', 2, 2, NULL, NULL, '2023-03-12 13:39:53', NULL, NULL);

-- ----------------------------
-- Table structure for tb_disk_item
-- ----------------------------
DROP TABLE IF EXISTS `tb_disk_item`;
CREATE TABLE `tb_disk_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `disk_id` bigint(20) NOT NULL COMMENT '隶属盘',
  `capaticy_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '容量名称',
  `capaticy_value` bigint(20) NULL DEFAULT NULL COMMENT '容量大小',
  `expire_time` int(11) NULL DEFAULT NULL COMMENT '过期时间 -1为不过期，最低单位为天',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_disk_item
-- ----------------------------
INSERT INTO `tb_disk_item` VALUES (7, 1617169131327459332, '基础容量', 524288000, -1, NULL, '2023-03-12 11:09:30', NULL, NULL);
INSERT INTO `tb_disk_item` VALUES (12, 1617169131327459337, '基础容量', 524288000, -1, NULL, '2023-03-12 17:54:44', NULL, NULL);

-- ----------------------------
-- Table structure for tb_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_file`;
CREATE TABLE `tb_file`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_id` char(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件id',
  `original_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '源文件名',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储文件名',
  `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储路径',
  `length` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件长度/大小',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '文件类型  0：目录  1：文件',
  `hash` char(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件hash',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '是否有效,可用于敏感文件排除',
  `upload_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上传id',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `filehash`(`hash`) USING BTREE COMMENT 'hash索引，只能判断是否存在，不能用于范围查找'
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_file
-- ----------------------------
INSERT INTO `tb_file` VALUES (1, '-1', '2', '3', '/yefng', '216', 1, 'sdafsadf', 1, '', NULL, '2023-03-11 13:56:08', NULL, NULL);
INSERT INTO `tb_file` VALUES (2, NULL, '《顶盒免费固件大全合集分享》 .txt', '《顶盒免费固件大全合集分享》 .txt', '/', '767', 1, 'e2ccff1f4b7e8cd3269f2852dbd4b8f3', NULL, '', NULL, '2023-03-11 13:56:08', NULL, NULL);
INSERT INTO `tb_file` VALUES (3, NULL, 'xsync', 'xsync', '/22', '529', 1, '25bdcdc1c48bbef30ce5ffaefa88784b', NULL, '', NULL, '2023-03-11 13:56:08', NULL, NULL);
INSERT INTO `tb_file` VALUES (5, NULL, 'Default.rdp', 'Default.rdp', '/1617169131327459329', '2288', 1, 'd88c857cd87a84f82c4a966ffdd1bf4f', NULL, '', NULL, '2023-03-11 13:56:08', NULL, NULL);
INSERT INTO `tb_file` VALUES (6, NULL, '22dddc962d4ea10b9aaf6c67832c05e7.pdf', '22dddc962d4ea10b9aaf6c67832c05e7.pdf', '/1617169131327459329', '153453', 1, '3180d187b2f28d05bee7cfe68d92e915', NULL, '', NULL, '2023-03-11 13:56:08', NULL, NULL);
INSERT INTO `tb_file` VALUES (7, NULL, '江茂明-后端开发笔试题.docx', '江茂明-后端开发笔试题.docx', '/1617169131327459329', '135918', 1, 'aaddc0670ae374560e036aad03e6c873', NULL, '', NULL, '2023-03-11 13:56:08', NULL, NULL);
INSERT INTO `tb_file` VALUES (8, NULL, '留言板系统.xmind', '留言板系统.xmind', '/1617169131327459329', '156820', 1, '7464c268815dea93dbf34f3286d04f92', NULL, '', NULL, '2023-03-11 13:56:08', NULL, NULL);
INSERT INTO `tb_file` VALUES (9, NULL, '[1975][10月]聪明 目录.xlsx', '[1975][10月]聪明 目录.xlsx', '/1617169131327459329', '2676607', 1, '2690ef813c14dd055d31a6389adbbbf8', NULL, '', NULL, '2023-03-11 13:56:08', NULL, NULL);
INSERT INTO `tb_file` VALUES (10, NULL, '天池比赛入门与实践 - AI学习 - 阿里云天池_3.ts', '天池比赛入门与实践 - AI学习 - 阿里云天池_3.ts', '/1617169131327459329', '94668716', 1, 'f3ddc61438b81aef951ecc6dc0beaaec', NULL, '', NULL, '2023-03-11 13:56:08', NULL, NULL);
INSERT INTO `tb_file` VALUES (11, 'c8cum35ekrgbzyyxsdwoi0tlxcw3mkya7q49rkay', 'xsync', 'xsync', '/yefeng', '529', 1, '520a91c059cd57f9d88a52f349e4f90d37921ab0', 1, 'qyvqsuhoytsi2qe4zjcbaxeafehxhf2b', NULL, '2023-03-11 13:56:08', NULL, NULL);
INSERT INTO `tb_file` VALUES (12, 'vcd9g16a255cfwv3ohsjuvu78p643j8namr8fj99', '1.jpg', '1.jpg', '/yefeng', '2288', 1, 'df0d35eb674915c363b700fe075b588778de3394', 1, 'txjfi1qsdn1dlavtd9hbn95defaq437d', NULL, '2023-03-11 13:56:08', NULL, NULL);

-- ----------------------------
-- Table structure for tb_follow
-- ----------------------------
DROP TABLE IF EXISTS `tb_follow`;
CREATE TABLE `tb_follow`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `from_user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `to_user_id` bigint(20) NULL DEFAULT NULL COMMENT '被关注用户ID',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '关注用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_follow
-- ----------------------------

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `role_id` int(10) NOT NULL,
  `role_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------

-- ----------------------------
-- Table structure for tb_share
-- ----------------------------
DROP TABLE IF EXISTS `tb_share`;
CREATE TABLE `tb_share`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `disk_id` bigint(20) NULL DEFAULT NULL COMMENT '分享的云盘id',
  `full_share_msg` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `share_title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `expired_time` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '过期时间,永久为空',
  `is_valid` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效',
  `share_pwd` char(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分享密码',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '分享的文件类型1.文件2文件夹（如果有多个，则有文件夹则优先文件夹）',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1629476827956600841 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分享表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_share
-- ----------------------------
INSERT INTO `tb_share` VALUES (1629476827956600840, 1617169131327459332, '文件夹1', '文件夹1', '2023-03-15T12:52:33Z', 1, '', 2, NULL, '2023-03-12 12:50:35', NULL, NULL);

-- ----------------------------
-- Table structure for tb_share_item
-- ----------------------------
DROP TABLE IF EXISTS `tb_share_item`;
CREATE TABLE `tb_share_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_id` char(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分享的文件id（个人云盘的fileid）',
  `share_id` char(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分享id',
  `type` int(11) NULL DEFAULT NULL COMMENT '文件类型',
  `disk_id` bigint(20) NULL DEFAULT NULL COMMENT '网盘id',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分享item' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_share_item
-- ----------------------------
INSERT INTO `tb_share_item` VALUES (6, '8nc1w0vx63njwjc6ant0m9ur0huko9qzgy5uaub3', '1629476827956600840', 2, 1617169131327459332, NULL, '2023-03-12 12:50:45', NULL, NULL);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态：1正常0禁用',
  `img_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE,
  UNIQUE INDEX `mobile`(`mobile`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (25, 'yefeng', '$2a$10$XWPCEex.L.44wPTF9AFfu.oex2WJiyYp2F.byNK6LnAle77l0WqzK', '$2a$10$XWPCEex.L.44wPTF9AFfu.', '1226325909@qq.com', NULL, 1, NULL, NULL, '2023-03-12 11:09:30', NULL, NULL);
INSERT INTO `tb_user` VALUES (32, NULL, NULL, '$2a$10$nKe7eb1GE1I3AWoRdQV4H.', NULL, '18312847020', NULL, NULL, NULL, '2023-03-12 17:54:26', NULL, NULL);

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `role_id` int(10) NULL DEFAULT NULL COMMENT '角色id',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for user_third_auth
-- ----------------------------
DROP TABLE IF EXISTS `user_third_auth`;
CREATE TABLE `user_third_auth`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `openid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方用户唯一标识\r\n第三方用户唯一标识',
  `login_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方平台标识(qq、wechat...)',
  `access_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三方获取的access_token,校验使用\r\n',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_user` bigint(20) NULL DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE,
  UNIQUE INDEX `openid`(`openid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_third_auth
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

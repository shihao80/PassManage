/*
 Navicat Premium Data Transfer

 Source Server         : 3307
 Source Server Type    : MySQL
 Source Server Version : 50646
 Source Host           : localhost:3307
 Source Schema         : us

 Target Server Type    : MySQL
 Target Server Version : 50646
 File Encoding         : 65001

 Date: 17/05/2020 14:09:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for p_pass_instant
-- ----------------------------
DROP TABLE IF EXISTS `p_pass_instant`;
CREATE TABLE `p_pass_instant`  (
  `pass_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '密钥UUID',
  `pass_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥名称',
  `pass_length` int(255) NULL DEFAULT NULL COMMENT '密钥长度',
  `pass_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥类型',
  `pass_childfir` varchar(1000) CHARACTER SET utf32 COLLATE utf32_general_ci NULL DEFAULT NULL COMMENT '第一子密钥',
  `pass_childsec` varchar(1000) CHARACTER SET utf32 COLLATE utf32_general_ci NULL DEFAULT NULL COMMENT '第二子密钥',
  `pass_childthi` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三子密钥',
  `pass_expiry` date NULL DEFAULT NULL COMMENT '密钥有效期',
  `pass_createtime` date NULL DEFAULT NULL COMMENT '密钥上传时间',
  `pass_userid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥用户',
  PRIMARY KEY (`pass_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 987987988 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of p_pass_instant
-- ----------------------------
INSERT INTO `p_pass_instant` VALUES (123555, 'SM2', 64, 'SM2', 'e69576dc8b2bfa5b3432900d9d047e50eae96d206db59288c038aa5871154111', '03fa6198f0b8f38f19bb496ffc0ceb73c4f2c48949c4fbf866e46ebbd6ee17b1ad', NULL, '2020-06-17', '2020-05-17', '18');
INSERT INTO `p_pass_instant` VALUES (123556, NULL, 66, 'SM2', '03fa6198f0b8f38f19bb496ffc0ceb73c4f2c48949c4fbf866e46ebbd6ee17b1ad', 'e69576dc8b2bfa5b3432900d9d047e50eae96d206db59288c038aa5871154111\n\n', NULL, '2020-06-17', '2020-05-17', '18');
INSERT INTO `p_pass_instant` VALUES (56165165, 'RSA', 868, 'RSA', 'MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIits7jPUYTkNzOg24L2MV2VHsY3\r\n0W5ZK0twE0goCQlW4dZUmDaa5XzL7QE9459flxd0vc8dlrhOd9O0If1BBHuYXp/iIXjFOvRhXyus\r\n58ZfLlZjrl7noT9H9rHqa5DBXzkftkrhMxWHoDRd+mnYDZHFdWGgLXoPCM8WYPL147lPAgMBAAEC\r\ngYBQFf/l/UyV4qdZ0BpvfVRlslfq65AC6arNtCkIMbVBzK/dx4m9DTwEyPThsmLq4rcEnnoNnxfO\r\nfOQHn1f/LhayfTUlwFGAxlV6oVFG/EZQ1uOAQvTDXmm5QiQB5Sh9VmBsnphq6E+ehLtV+EipJZhR\r\nI4AWWyOGDEDbzbx+RvVCWQJBAMn509t3ChHMEp2zYK8q2Vbq9Rk+F1O4tFQWh1hOuqFJVVVPwvi5\r\nKX19qkEM5m1K+yYKS+iwf8TjMcts9rYWABsCQQCtPK2kyxPe+8/RypOn1OC7Mj8D6eGE7ZrW/Osr\r\n2k0wli+96fbQToSDZj8T1k+i/SfA5Vsevw4Ct1EhfEnS/wbdAkBEJWa1xZRALPqa+srET1DFYsEP\r\no0N7m/Y76N4BjY6qnHOONTszcEXvcVbgW9njRkk2rIKwH9z8MSu71yfmh8XBAkAu4pI4XIHnQo2P\r\nQN1PeezMnR+y5nSTrl7OuqrHNpDXin4nTw/udqGkkmKV8tWgzhUZPrm3i4u2Lnj1sBam1UC9AkBq\r\nDvSAyw6uBM9fb0ZdISfzKO6iPU6/PkDE0Xgx614TMUAfw6zqW+a56a+ruBmQhkenIy55zx37HfTQ\r\nyIUG83pI\r\n', 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGVTeJ8P1ay0zauA1sEhMDiRASimfJa8mGHzKj\r\nNBH5QbCzw+M/IyyKBhoPU3B417EkDCSiOWsyQtynHLCMwxc6uyriUMLtJEgUT6WAOcYSnn8O1OMo\r\nZFdBnCCvFNxJXZPqsz3KTqhPrh4BN/WnrhzQR/NKDJE8uX48FrHFnD7D9wIDAQAB\r\n', NULL, '2020-06-17', '2020-05-17', '18');
INSERT INTO `p_pass_instant` VALUES (987987987, '654654', 64, 'SM4', '730603020f767c090f78790078717d061912661a141b1a10681e131119116f19', NULL, NULL, '2021-02-16', '2020-04-21', '3');

-- ----------------------------
-- Table structure for p_pass_old
-- ----------------------------
DROP TABLE IF EXISTS `p_pass_old`;
CREATE TABLE `p_pass_old`  (
  `pass_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '密钥UUID',
  `pass_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥名称',
  `pass_length` int(255) NULL DEFAULT NULL COMMENT '密钥长度',
  `pass_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥类型',
  `pass_childfir` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第一子密钥',
  `pass_childsec` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第二子密钥',
  `pass_childthi` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第三子密钥',
  `pass_expiry` date NULL DEFAULT NULL COMMENT '密钥有效期',
  `pass_createtime` date NULL DEFAULT NULL COMMENT '密钥上传时间',
  `pass_userid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密钥用户',
  PRIMARY KEY (`pass_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5555556 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of p_pass_old
-- ----------------------------
INSERT INTO `p_pass_old` VALUES (1, 'SM2TEST2', 66, 'SM2', 'YPS]\0\\X\0Y^FOALHAs. pu,&{\'()y+)+88729<3313l97l9oK', 'T	TVP	\rZWEFBIL\Z#/ \'*r&r/.).).{~79e5857<h48?g>jj', NULL, '2020-03-10', '2020-05-07', '3');
INSERT INTO `p_pass_old` VALUES (123456, 'SM2TEST', 132, 'SM2', '06040204555f07545a095a085d0807081e4743464048461d1c4d1a11454d4d1d2572707720252c287d7b292b29292f2d353f39306131363a6f3f353e343b6b364217', '570e0304010d02035c5b0c0e5d0d065f4c461612451c411c1c1c1b401d484f1d702d7170732770292f27292c2b2e2378366134303163306c6d6a6c3a3034396b', NULL, '2020-11-04', '2020-05-07', '3');
INSERT INTO `p_pass_old` VALUES (5555555, 'SM4TEST', 32, 'SM4', 'wws	\r}xrbdao\Zo', NULL, NULL, '2020-06-07', '2020-05-07', '3');

-- ----------------------------
-- Table structure for p_random
-- ----------------------------
DROP TABLE IF EXISTS `p_random`;
CREATE TABLE `p_random`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `random_str` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `key_iD` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of p_random
-- ----------------------------
INSERT INTO `p_random` VALUES (29, 'admin', '16553287435928518469895643197905637406784463715593003199734802744722163696489726822880437023631366012110954324680470244282879779', 987987987);
INSERT INTO `p_random` VALUES (30, 'admin', '667659727733238194067818108983576566662706111752763630296570916173', 123456);
INSERT INTO `p_random` VALUES (31, 'admin', '188650791472881541212324695028020616928726467063106688331943941433', 3123123);
INSERT INTO `p_random` VALUES (32, 'admin', '60352401007216946536636466100537', 5555555);
INSERT INTO `p_random` VALUES (33, 'admin', '492569748053956276821090647158936849279052738807932339148045242987401024832372924047714172690776961291932656898584658773101926007044307187736672041092624867223824414734022730320689428207443166878881761078853780352774798784', 43234);
INSERT INTO `p_random` VALUES (34, 'admin', '146596007404455505759058259405632684924090215986375750814674095005713758534224529198666652981285827072660701274653495556992700045997340994960017167156812553805254157484268887299595299497471699848297400756573939428686785472', 2250485);
INSERT INTO `p_random` VALUES (35, 'admin', '793164523144199523468363229366456053950804449121305389268947853946802843152575300443108874730941317862674978243731807820025908964821949998783267673037652866831167548500236611809371838574498785015760513940053268761922978242', 5245241);
INSERT INTO `p_random` VALUES (36, 'admin', '657111261931293625352863088769367660378429160693011194427881011799024451693952936167750771661440550649483302506158486627825816102827192820226911281582406427500185360363377525358410498228340837537352401660029851509879531902', 20202020);
INSERT INTO `p_random` VALUES (37, 'admin', '200778893120222603333980740998160652400432485039932144994713065417971916314640129248163503439335884410436316948243528943627806427687648761541328082604047466331301412926518391967410323751248269891856404597600367445391452522', 3424);
INSERT INTO `p_random` VALUES (38, 'admin', '054834899381297651328158176068360491778164375027811913670387022543174211300544598181374720016325632212636815501973635804707836728824043027796394587034570281433091413926957524172509115329008451461994232043403244614704269129', 3424);
INSERT INTO `p_random` VALUES (39, 'yg9531', '012706718950017617346168017118540487196354702147546428341829672374', 123555);
INSERT INTO `p_random` VALUES (40, 'yg9531', '806607412772634472891159985553754879049352999565086453761450821437188823559518361752029838106366353162085339947925086992305505944123763940858403229873452576857526871517801371431200531146527013757190352901487898448538717737', 123123);
INSERT INTO `p_random` VALUES (41, 'yg9531', '920321266013744662189653353618979539199635087316945722870140628237270940544288258743364365171383367932946084401808841571735854418317406696564630630558809902333983319723822356604797864555578773490715282980520692275129854966', 56165165);
INSERT INTO `p_random` VALUES (42, 'yg9531', '002556194548962672201093007382237710889686596521475282279016334220', 123555);
INSERT INTO `p_random` VALUES (43, 'yg9531', '792304488043650608482724551163422605545776953071757848703588127587', 123555);
INSERT INTO `p_random` VALUES (44, 'yg9531', '285364649474419199516345235060142068893117956176260161469069950027', 123556);

-- ----------------------------
-- Table structure for p_sm4pub
-- ----------------------------
DROP TABLE IF EXISTS `p_sm4pub`;
CREATE TABLE `p_sm4pub`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pubkey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of p_sm4pub
-- ----------------------------
INSERT INTO `p_sm4pub` VALUES (1, '039fc72d0d61b1561f0058e224fe07634f7a56c977700697f389c3672338dc5388', 'yg9531');

-- ----------------------------
-- Table structure for p_username
-- ----------------------------
DROP TABLE IF EXISTS `p_username`;
CREATE TABLE `p_username`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` date NULL DEFAULT NULL,
  `if_close` int(11) NULL DEFAULT NULL,
  `pri_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of p_username
-- ----------------------------
INSERT INTO `p_username` VALUES (1, 'admin', '2020-04-19', 1, NULL);
INSERT INTO `p_username` VALUES (2, 'student7', '2020-04-27', 0, NULL);
INSERT INTO `p_username` VALUES (3, 'student8', '2020-04-27', 0, NULL);
INSERT INTO `p_username` VALUES (4, 'student9', '2020-04-27', 0, NULL);
INSERT INTO `p_username` VALUES (5, 'student10', '2020-04-27', 0, NULL);
INSERT INTO `p_username` VALUES (6, 'yg9531', '2020-05-17', 0, '4cfbc45658b39156f0c12a2ab0a18996953fd28a93e393628680d3c211c9cf58');

-- ----------------------------
-- Table structure for sys_admin_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_admin_user`;
CREATE TABLE `sys_admin_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '密码',
  `real_name` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '真名',
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '邮箱',
  `telephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '座机号',
  `mobile_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '手机号',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '地址',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `role_id` bigint(20) NULL DEFAULT 0 COMMENT '角色',
  `account_non_expired` tinyint(1) NULL DEFAULT 0 COMMENT '是否未失效',
  `account_non_locked` tinyint(1) NULL DEFAULT 0 COMMENT '是否未锁定',
  `credentials_non_expired` tinyint(1) NULL DEFAULT NULL COMMENT '是否未失效',
  `enabled` tinyint(1) NULL DEFAULT NULL COMMENT '是否可用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_name`(`username`) USING BTREE,
  INDEX `s_a_r_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_admin_user
-- ----------------------------
INSERT INTO `sys_admin_user` VALUES (3, 'admin', '21232f297a57a5a743894a0e4a801fc3', '管理员', '', '', '', '', '2018-07-30 00:00:00', '2018-07-30 00:00:00', 1, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (4, 'test', '098f6bcd4621d373cade4e832627b4f6', '测试人员', '', '', '', '', '2018-09-03 00:00:00', '2018-09-03 00:00:00', 2, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (7, 'magicalcoder', '6860c37b00ed4e444a7d2c6e8fb66886', '系统默认超级管理员账号', '', '', '', '', '2018-09-06 00:00:00', '2018-09-06 00:00:00', 1, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (8, 'student', '4995ec381d6071f48a60170c42ebab72', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (9, 'students', '4995ec381d6071f48a60170c42ebab72', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (10, 'students1', '4995ec381d6071f48a60170c42ebab72', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (11, 'students2', '4995ec381d6071f48a60170c42ebab72', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (12, 'student6', '4995ec381d6071f48a60170c42ebab72', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (13, 'student7', '4995ec381d6071f48a60170c42ebab72', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (14, 'student8', '4995ec381d6071f48a60170c42ebab72', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (15, 'student9', '4995ec381d6071f48a60170c42ebab72', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (16, 'student10', 'e10adc3949ba59abbe56e057f20f883e', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (17, 'yg9530', 'badbdc3d248f1d599b08396e21811290', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 0, 0, 0, 1);
INSERT INTO `sys_admin_user` VALUES (18, 'yg9531', '312ad832b9455b1acab776dc82b33cab', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 0, 0, 0, 1);

-- ----------------------------
-- Table structure for sys_global_permit_url
-- ----------------------------
DROP TABLE IF EXISTS `sys_global_permit_url`;
CREATE TABLE `sys_global_permit_url`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `permit_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '通过名称',
  `backend_url_reg` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '后端的地址正则,校验当前请求url是否有权限,get|post统一会按照参数首字母排序',
  `module_id` bigint(20) NULL DEFAULT NULL COMMENT '所属模块',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sy_m_id`(`module_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后端请求地址全局允许过滤器,在此表的统一不用再去权限表匹配了' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_global_permit_url
-- ----------------------------
INSERT INTO `sys_global_permit_url` VALUES (2, '允许进入后台,使用有些通用接口（请勿删除）', '/admin/(rmp|page|page_v2|common_file_rest).*', NULL);
INSERT INTO `sys_global_permit_url` VALUES (3, '通用获取权限相关接口（请勿删除）', '/admin/rest_rmp/(get_permission_list|get_module_list|user_info).*', NULL);

-- ----------------------------
-- Table structure for sys_log_admin_operate
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_admin_operate`;
CREATE TABLE `sys_log_admin_operate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `admin_user_id` bigint(20) NULL DEFAULT 0 COMMENT '管理员',
  `user_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '管理员名称',
  `table_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '表名',
  `operate_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作类型',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '链接',
  `primary_id_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '主键值',
  `form_body` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '提交内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 316 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_log_admin_operate
-- ----------------------------
INSERT INTO `sys_log_admin_operate` VALUES (308, 3, '管理员', 'sys_log_admin_operate_rest', 'delete', '/admin/sys_log_admin_operate_rest/delete/307', '', '{}', '2018-09-10 17:00:43');
INSERT INTO `sys_log_admin_operate` VALUES (309, 3, '管理员', 'sys_module_category_rest', 'update', '/admin/sys_module_category_rest/update/1', NULL, '{\"moduleCategoryName\":[\"密钥管理\"]}', '2020-04-11 19:03:40');
INSERT INTO `sys_log_admin_operate` VALUES (310, 3, '管理员', 'sys_module_rest', 'update', '/admin/sys_module_rest/update/34', NULL, '{\"moduleTitle\":[\"现用密钥\"]}', '2020-04-11 19:04:10');
INSERT INTO `sys_log_admin_operate` VALUES (311, 3, '管理员', 'sys_module_rest', 'update', '/admin/sys_module_rest/update/35', NULL, '{\"moduleTitle\":[\"历史密钥\"]}', '2020-04-11 19:04:15');
INSERT INTO `sys_log_admin_operate` VALUES (312, 3, '管理员', 'rest_rmp', 'save_tree_data', '/admin/rest_rmp/save_tree_data/1', NULL, '{\"ids[]\":[\"category_1\",\"module_34\",\"permission_39\",\"module_35\",\"permission_40\",\"category_3\",\"module_17\",\"permission_21\",\"module_5\",\"module_11\",\"module_7\",\"module_8\",\"module_4\",\"module_20\",\"permission_25\"]}', '2020-04-11 19:06:34');
INSERT INTO `sys_log_admin_operate` VALUES (313, 3, '管理员', 'sys_role_rest', 'update', '/admin/sys_role_rest/update/2', NULL, '{\"roleName\":[\"用戶账号\"]}', '2020-05-10 08:48:32');
INSERT INTO `sys_log_admin_operate` VALUES (314, 3, '管理员', 'rest_rmp', 'save_tree_data', '/admin/rest_rmp/save_tree_data/2', NULL, '{\"ids[]\":[\"category_1\",\"module_34\",\"permission_39\",\"module_35\",\"permission_40\"]}', '2020-05-10 08:48:49');
INSERT INTO `sys_log_admin_operate` VALUES (315, 3, '管理员', 'p_pass_instant_rest', 'update', '/admin/p_pass_instant_rest/update/3123123', NULL, '{\"passExpiry\":[\"2020-03-10 00:00:00\"]}', '2020-05-10 10:00:51');

-- ----------------------------
-- Table structure for sys_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_module`;
CREATE TABLE `sys_module`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `module_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '模块唯一键',
  `module_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '模块URL',
  `module_category_id` bigint(20) NULL DEFAULT 0 COMMENT '模块分类',
  `sort_num` int(2) NULL DEFAULT 0 COMMENT '排序',
  `module_title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `if_show` tinyint(1) NULL DEFAULT 1 COMMENT '是否显示',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_module`(`module_category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '模块' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_module
-- ----------------------------
INSERT INTO `sys_module` VALUES (4, 'sys_log_admin_operate', 'admin/page_v2/sys_log_admin_operate/list', 3, 7, '系统日志', 1);
INSERT INTO `sys_module` VALUES (5, 'sys_module_category', 'admin/page_v2/sys_module_category/list', 3, 1, '菜单管理', 1);
INSERT INTO `sys_module` VALUES (7, 'sys_admin_user', 'admin/page_v2/sys_admin_user/list', 3, 3, '管理员', 1);
INSERT INTO `sys_module` VALUES (8, 'sys_global_permit_url', 'admin/page_v2/sys_global_permit_url/list', 3, 6, '全局地址过滤', 1);
INSERT INTO `sys_module` VALUES (11, 'sys_role', 'admin/page_v2/sys_role/list', 3, 2, '角色管理', 1);
INSERT INTO `sys_module` VALUES (17, 'sys', '', 3, -1, '系统表权限载体（请勿删除）', 0);
INSERT INTO `sys_module` VALUES (20, 'dict', 'admin/page_v2/dict/list', 3, 10, '字典配置', 1);
INSERT INTO `sys_module` VALUES (34, 'p_pass_instant', 'admin/page_v2/p_pass_instant/list', 1, 0, '现用密钥', 1);
INSERT INTO `sys_module` VALUES (35, 'p_pass_old', 'admin/page_v2/p_pass_old/list', 1, 0, '历史密钥', 1);

-- ----------------------------
-- Table structure for sys_module_category
-- ----------------------------
DROP TABLE IF EXISTS `sys_module_category`;
CREATE TABLE `sys_module_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `module_category_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '模块名称',
  `sort_num` int(2) NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '模块分类' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_module_category
-- ----------------------------
INSERT INTO `sys_module_category` VALUES (1, '密钥管理', 1);
INSERT INTO `sys_module_category` VALUES (3, '系统模块', 2);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `permission_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '过滤器名称:审核通过',
  `filter_platform` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '过滤端:front|backend|front_backend',
  `backend_url_reg` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '后端的地址正则,校验当前请求url是否有权限,get|post统一会按照参数首字母排序',
  `front_dom` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '前端Key,多个逗号分割',
  `front_action` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '前端处理方式show|disabled',
  `module_id` bigint(20) NULL DEFAULT NULL COMMENT '所属模块',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sy_m_id`(`module_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后端请求地址允许过滤器,不在过滤器的统一拒绝' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (2, '查询权限', 'backend', '/admin/goods_category_rest/.*', '', '', 2);
INSERT INTO `sys_permission` VALUES (13, '全类型测试', 'backend', '/admin/all_type_rest/.*', NULL, '', 14);
INSERT INTO `sys_permission` VALUES (15, '商品模块保存权限', 'front_backend', '/admin/goods_rest/(update|save).*', '.security_edit_form_operate_save', 'show', 1);
INSERT INTO `sys_permission` VALUES (17, '商品模块添加按钮', 'front', '', '.security_list_query_operate_add_news', 'show', 1);
INSERT INTO `sys_permission` VALUES (18, '商品模块(批量)删除权限', 'front_backend', '/admin/goods_rest/(delete|batch_delete).*', '.security_list_table_operate_del,.security_list_query_operate_del_all', 'show', 1);
INSERT INTO `sys_permission` VALUES (19, '商品模块查询权限', 'backend', '/admin/goods_rest/(search|page|get).*', '', '', 1);
INSERT INTO `sys_permission` VALUES (21, '所有系统表的所有权限(请勿删除)', 'backend', '/admin/(rest_rmp|sys_[a-z_]+_rest)/.*', '', 'show', 17);
INSERT INTO `sys_permission` VALUES (22, '测试商品模块disabled', 'front', '', '.security_edit_form_param_goodsName,.security_edit_form_param_imgSrc,.security_edit_form_param_publishStatus,.security_edit_form_param_goodsStatus,.security_edit_form_param_price,.security_edit_form_param_shortBrief,.security_edit_form_param_goodsDescription,.security_edit_form_param_createTime,.security_edit_form_param_goodsCategoryId', 'show', 1);
INSERT INTO `sys_permission` VALUES (23, '商品图集全部权限', 'front_backend', '/admin/goods_img_rest/.*', '', 'show', 18);
INSERT INTO `sys_permission` VALUES (24, '商品附件全部权限', 'front_backend', '/admin/goods_file_rest/.*', '', 'show', 19);
INSERT INTO `sys_permission` VALUES (25, '字典全部权限', 'front_backend', '/admin/dict_rest/.*', '', 'show', 20);
INSERT INTO `sys_permission` VALUES (39, '全部权限', 'backend', '/admin/p_pass_instant_rest/[\\s\\S]*', '', '', 34);
INSERT INTO `sys_permission` VALUES (40, '全部权限', 'backend', '/admin/p_pass_old_rest/[\\s\\S]*', '', '', 35);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '角色名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '用戶账号');

-- ----------------------------
-- Table structure for sys_role_module_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_module_permission`;
CREATE TABLE `sys_role_module_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色',
  `module_id` bigint(20) NULL DEFAULT NULL COMMENT '模块',
  `permission_id` bigint(20) NULL DEFAULT NULL COMMENT '权限',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `s_r_id`(`role_id`) USING BTREE,
  INDEX `s_m_id`(`module_id`) USING BTREE,
  INDEX `s_pid`(`permission_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 444 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色模块权限' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role_module_permission
-- ----------------------------
INSERT INTO `sys_role_module_permission` VALUES (433, 1, 34, 39);
INSERT INTO `sys_role_module_permission` VALUES (434, 1, 35, 40);
INSERT INTO `sys_role_module_permission` VALUES (435, 1, 4, NULL);
INSERT INTO `sys_role_module_permission` VALUES (436, 1, 5, NULL);
INSERT INTO `sys_role_module_permission` VALUES (437, 1, 7, NULL);
INSERT INTO `sys_role_module_permission` VALUES (438, 1, 8, NULL);
INSERT INTO `sys_role_module_permission` VALUES (439, 1, 11, NULL);
INSERT INTO `sys_role_module_permission` VALUES (440, 1, 17, 21);
INSERT INTO `sys_role_module_permission` VALUES (441, 1, 20, 25);
INSERT INTO `sys_role_module_permission` VALUES (442, 2, 34, 39);
INSERT INTO `sys_role_module_permission` VALUES (443, 2, 35, 40);

SET FOREIGN_KEY_CHECKS = 1;

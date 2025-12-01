/*
 Navicat Premium Data Transfer

 Source Server         : myMySQL
 Source Server Type    : MySQL
 Source Server Version : 80044
 Source Host           : localhost:3306
 Source Schema         : aihis

 Target Server Type    : MySQL
 Target Server Version : 80044
 File Encoding         : 65001

 Date: 01/12/2025 21:35:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for emr
-- ----------------------------
DROP TABLE IF EXISTS `emr`;
CREATE TABLE `emr`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '就诊记录ID（主键）',
  `patient_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '患者姓名',
  `gender` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '性别（男/女/其他）',
  `age` tinyint(0) UNSIGNED NOT NULL COMMENT '年龄（0-120）',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话（支持固定电话+手机号）',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `visit_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '就诊号（唯一，如MZ07882405098）',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '就诊科室',
  `visit_time` datetime(0) NOT NULL COMMENT '就诊时间',
  `visit_type` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '就诊类型（初诊/复诊）',
  `main_complaint` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主诉',
  `present_illness` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '现病史',
  `past_illness` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '既往史',
  `allergy_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '过敏史',
  `diagnosis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '诊断结果',
  `prescription` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '诊断处方',
  `suggestion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '医嘱建议',
  `doctor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '医师姓名',
  `signature_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医师手签图片URL',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '记录更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_visit_no`(`visit_no`) USING BTREE,
  INDEX `idx_dept_name`(`dept_name`) USING BTREE,
  INDEX `idx_visit_time`(`visit_time`) USING BTREE,
  INDEX `idx_patient_name`(`patient_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '患者就诊记录表（存储就诊+病历核心信息）' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

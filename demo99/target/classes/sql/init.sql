-- 医疗AI导诊系统数据库初始化脚本

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `aihis` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE `aihis`;

-- 创建患者表
CREATE TABLE IF NOT EXISTS `patient` (
  `patient_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '患者编号',
  `name` VARCHAR(50) NOT NULL COMMENT '患者姓名',
  `phone` VARCHAR(20) NOT NULL COMMENT '患者手机号',
  `age` INT COMMENT '患者年龄',
  `gender` INT COMMENT '患者性别（0-女，1-男）',
  `symptoms` TEXT COMMENT '主要症状描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='患者信息表';

-- 创建科室表
CREATE TABLE IF NOT EXISTS `department` (
  `dept_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '科室编号',
  `dept_name` VARCHAR(50) NOT NULL COMMENT '科室名称',
  `description` TEXT COMMENT '科室描述',
  `main_symptoms` TEXT COMMENT '科室主治症状（用于AI匹配）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科室信息表';

-- 创建医生表
CREATE TABLE IF NOT EXISTS `doctor` (
  `doctor_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '医生编号',
  `name` VARCHAR(50) NOT NULL COMMENT '医生姓名',
  `dept_id` INT NOT NULL COMMENT '所属科室编号',
  `title` VARCHAR(50) COMMENT '医生职称',
  `specialty` VARCHAR(200) COMMENT '擅长领域',
  `is_available` INT DEFAULT 1 COMMENT '是否在诊（0-不在诊，1-在诊）',
  `schedule_date` VARCHAR(20) COMMENT '坐诊日期（格式：YYYY-MM-DD）',
  `schedule_time` VARCHAR(20) COMMENT '坐诊时间段（如：上午、下午、全天）',
  FOREIGN KEY (`dept_id`) REFERENCES `department`(`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生信息表';

-- 创建挂号记录表
CREATE TABLE IF NOT EXISTS `registration` (
  `registration_id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '挂号编号',
  `patient_id` INT NOT NULL COMMENT '患者编号',
  `doctor_id` INT NOT NULL COMMENT '医生编号',
  `dept_id` INT NOT NULL COMMENT '科室编号',
  `appointment_date` VARCHAR(20) NOT NULL COMMENT '挂号日期',
  `appointment_time` VARCHAR(20) COMMENT '挂号时间段',
  `status` INT DEFAULT 1 COMMENT '挂号状态（0-已取消，1-已预约，2-已完成）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (`patient_id`) REFERENCES `patient`(`patient_id`),
  FOREIGN KEY (`doctor_id`) REFERENCES `doctor`(`doctor_id`),
  FOREIGN KEY (`dept_id`) REFERENCES `department`(`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='挂号记录表';

-- 插入示例科室数据（如果不存在）
INSERT IGNORE INTO `department` (`dept_id`, `dept_name`, `description`, `main_symptoms`) VALUES
(1, '内科', '内科是医院的主要科室之一，主要诊治各种内科疾病', '头痛,发烧,咳嗽,胸闷,心慌,腹痛,腹泻,高血压,糖尿病'),
(2, '外科', '外科主要进行各种手术治疗', '外伤,骨折,肿瘤,阑尾炎,胆囊炎,疝气'),
(3, '儿科', '专门为儿童提供医疗服务', '儿童发烧,儿童咳嗽,儿童腹泻,儿童感冒,儿童肺炎'),
(4, '妇科', '专门为女性提供医疗服务', '月经不调,痛经,妇科炎症,不孕不育,妇科肿瘤'),
(5, '骨科', '专门诊治骨骼相关疾病', '骨折,关节疼痛,腰椎间盘突出,颈椎病,关节炎'),
(6, '皮肤科', '专门诊治皮肤相关疾病', '皮肤瘙痒,湿疹,皮炎,痤疮,脱发'),
(7, '眼科', '专门诊治眼部疾病', '视力下降,眼睛疼痛,眼干,结膜炎,白内障'),
(8, '耳鼻喉科', '专门诊治耳鼻喉相关疾病', '耳鸣,鼻塞,流鼻涕,咽喉疼痛,听力下降');

-- 插入示例医生数据（如果不存在）
-- 注意：schedule_date使用固定日期，您可以根据实际需要修改为未来日期
-- 建议格式：'YYYY-MM-DD'，例如：'2025-12-31'
INSERT IGNORE INTO `doctor` (`doctor_id`, `name`, `dept_id`, `title`, `specialty`, `is_available`, `schedule_date`, `schedule_time`) VALUES
(1, '张医生', 1, '主任医师', '心血管疾病,高血压,冠心病', 1, '2025-12-31', '上午'),
(2, '李医生', 1, '副主任医师', '消化系统疾病,胃炎,胃溃疡', 1, '2025-12-31', '下午'),
(3, '王医生', 2, '主任医师', '普外科手术,腹腔镜手术', 1, '2025-12-31', '全天'),
(4, '赵医生', 3, '主任医师', '儿童常见病,儿童呼吸系统疾病', 1, '2025-12-31', '上午'),
(5, '刘医生', 4, '副主任医师', '妇科炎症,月经不调', 1, '2025-12-31', '下午'),
(6, '陈医生', 5, '主任医师', '骨折治疗,关节置换', 1, '2025-12-31', '全天'),
(7, '周医生', 6, '主治医师', '皮肤病,皮肤美容', 1, '2025-12-31', '上午'),
(8, '吴医生', 7, '副主任医师', '白内障手术,眼底病', 1, '2025-12-31', '下午'),
(9, '郑医生', 8, '主治医师', '鼻炎,中耳炎,咽喉炎', 1, '2025-12-31', '全天');


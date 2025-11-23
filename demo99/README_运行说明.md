# 医疗AI导诊系统 - 运行说明

## ✅ 代码已检查并修复，现在可以正常运行

### 快速启动步骤

#### 1. 数据库准备（必须）
```sql
-- 方式1：使用MySQL客户端执行
-- 打开MySQL客户端，执行以下脚本：
source demo99/src/main/resources/sql/init.sql

-- 方式2：手动执行
-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS `aihis` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. 执行 demo99/src/main/resources/sql/init.sql 中的所有SQL语句
```

#### 2. 检查数据库配置
确认 `demo99/src/main/resources/application.yaml` 中的配置：
- 数据库名：`aihis`
- 用户名：`root`
- 密码：`root`
- 如果不同，请修改配置

#### 3. 检查API密钥
确认DeepSeek API密钥有效（在 `application.yaml` 中）

#### 4. 编译项目
```bash
mvn clean install
```

#### 5. 启动应用
运行 `Demo99App.java` 主类

#### 6. 测试接口
启动成功后，访问：
```
GET http://localhost:18080/d/c1?msg=你好，我需要看病&chatId=test-001
```

## 📋 已修复的问题

1. ✅ SQL脚本优化：使用 `INSERT IGNORE` 避免重复插入
2. ✅ 日期设置：使用未来日期（2025-12-31）确保医生可预约
3. ✅ 代码检查：所有实体类、Service、Mapper都已正确配置
4. ✅ 依赖检查：所有必要的依赖都已配置

## 🔧 系统功能

1. **大模型询问患者，并获得关键信息**
   - 收集患者姓名、手机号、年龄、性别、症状

2. **AI协助患者确定科室**
   - 根据症状智能推荐科室

3. **大模型结合病情和医生坐诊情况列出出诊医生名单**
   - 根据科室和日期查询可用医生

4. **使用ToolCalling技术完成挂号**
   - 自动调用挂号工具完成预约

## 📝 注意事项

1. **数据库日期**：SQL脚本中医生坐诊日期设置为 `2025-12-31`，如需测试，请使用该日期或修改为其他未来日期

2. **API密钥**：确保DeepSeek API密钥有效，否则AI功能无法使用

3. **端口冲突**：默认端口为 `18080`，如被占用请修改 `application.yaml`

## 🐛 常见问题

### 问题1：数据库连接失败
**解决**：检查MySQL是否启动，确认数据库配置正确

### 问题2：表不存在
**解决**：执行SQL脚本创建表

### 问题3：ChatMemory Bean未找到
**解决**：Spring AI会自动配置，如报错请检查依赖

### 问题4：API调用失败
**解决**：检查网络连接和API密钥

## 📞 测试示例

```bash
# 测试1：开始导诊
curl "http://localhost:18080/d/c1?msg=你好，我需要看病&chatId=test-001"

# 测试2：描述症状
curl "http://localhost:18080/d/c1?msg=我头痛发烧&chatId=test-001"

# 测试3：提供个人信息
curl "http://localhost:18080/d/c1?msg=我叫张三，手机号13800138000，25岁，男性&chatId=test-001"
```



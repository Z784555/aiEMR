package com.edu.neu.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Provides structured default prompts so that the AI endpoints
 * can still be exercised when upstream systems (如挂号、处方)
 * 暂无可用数据。
 */
@Service
public class PromptTemplateService {

    // 预置一段覆盖患者基本信息、主诉、诊断与处方的示例场景
    private static final String DEFAULT_PROMPT = """
            患者信息：姓名张三，性别男，年龄 42 岁，联系电话 13800138000，住址北京市朝阳区建国路 88 号，就诊编号 202410260089，就诊科室呼吸内科，就诊类型复诊，接诊医生李四。
            主诉：反复咳嗽、咳痰 1 周，伴胸闷 2 天。
            现病史：1 周前患者受凉后出现咳嗽，为阵发性干咳，无明显咳痰，自行口服 “止咳糖浆”（具体不详）后症状无改善；2 天前出现咳少量白色黏液痰，伴活动后胸闷，无胸痛、呼吸困难，无发热、寒战，无鼻塞、流涕。昨日于社区医院查血常规提示：白细胞 6.8×10⁹/L，中性粒细胞比例 58%，淋巴细胞比例 35%；胸片示 “双肺纹理增多、增粗”，社区医院给予 “阿莫西林胶囊” 口服 1 次（0.5g，每日 3 次），服药后无明显不适，但症状未缓解，今日来我院复诊。
            既往史：高血压病史 3 年，长期口服硝苯地平缓释片（20mg，每日 1 次），血压控制在 130/80mmHg 左右；无手术、外伤史，无输血史。
            过敏史：青霉素类药物无过敏（社区医院皮试阴性），无食物及其他药物过敏史。
            检查发现：体格检查：体温 36.8℃，脉搏 86 次 / 分，呼吸 20 次 / 分，血压 135/85mmHg；神志清楚，精神可，咽部黏膜轻度充血，双侧扁桃体无肿大；胸廓对称，双肺呼吸音粗，可闻及散在干性啰音，未闻及湿性啰音及胸膜摩擦音；心率 86 次 / 分，律齐，各瓣膜听诊区未闻及病理性杂音；腹平软，无压痛、反跳痛。辅助检查：携带社区医院血常规及胸片报告（编号：SQ20241025001）。
            医生初步诊断建议：急性支气管炎（ICD-10：J20.901）；高血压 1 级（ICD-10：I10.x01）。
            个性化处方方案：1. 左氧氟沙星片 0.5g，口服，每日 1 次，疗程 7 天；2. 氨溴索口服溶液 10ml，口服，每日 3 次，疗程 7 天；3. 硝苯地平缓释片 20mg，口服，每日 1 次，继续维持原剂量控制血压。
            治疗与随访建议：注意休息，避免劳累、受凉，多饮水，清淡饮食，戒烟戒酒；避免接触粉尘、烟雾等刺激性物质；规律监测血压，每日记录 1 次（早晚各 1 次）；3 天后门诊复诊，复查血常规，评估咳嗽、胸闷症状改善情况；若出现高热（≥38.5℃）、胸闷加重、呼吸困难等情况，立即急诊就诊。
            风险提示：左氧氟沙星片可能引起胃肠道不适、头晕等不良反应，若出现明显不适需立即停药并就医；服药期间禁止饮酒，避免加重肝脏负担。
            """;

    /**
     * Returns doctor input when provided; otherwise falls back to
     * a demo scenario so the downstream大模型能力可以被测试。
     */
    public String resolvePrompt(String doctorInput) {
        return StringUtils.hasText(doctorInput) ? doctorInput : DEFAULT_PROMPT;
    }
}


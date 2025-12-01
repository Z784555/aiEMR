/**
 * EMR 数据模型类型定义
 * 对应后端 EMR 表结构
 */

/**
 * 病历记录接口（前端格式：驼峰命名）
 */
export interface EmrRecord {
  /** 就诊记录ID（主键，自增） */
  id: number | null
  /** 患者姓名 */
  patientName: string
  /** 性别（男/女/其他） */
  gender: string
  /** 年龄（0-120） */
  age: number
  /** 联系电话 */
  phone?: string
  /** 家庭住址 */
  address?: string
  /** 就诊号（唯一） */
  visitNo: string
  /** 就诊科室 */
  deptName: string
  /** 就诊时间（格式：YYYY-MM-DD HH:MM:SS） */
  visitTime: string
  /** 就诊类型（初诊/复诊） */
  visitType: string
  /** 主诉 */
  mainComplaint: string
  /** 现病史 */
  presentIllness?: string
  /** 既往史 */
  pastIllness?: string
  /** 过敏史 */
  allergyHistory?: string
  /** 诊断结果 */
  diagnosis?: string
  /** 诊断处方 */
  prescription?: string
  /** 医嘱建议 */
  suggestion?: string
  /** 医师姓名 */
  doctorName: string
  /** 医师手签图片URL */
  signatureUrl?: string
  /** 记录创建时间 */
  createTime?: string
  /** 记录更新时间 */
  updateTime?: string
  /** 医院名称（仅前端展示用，不发送到后端） */
  hospitalName?: string
  /** 后端大模型回复 */
  reply?: string
}

/**
 * 病历记录接口（后端格式：下划线命名）
 */
export interface EmrRecordBackend {
  id: number | null
  patient_name: string
  gender: string
  age: number
  phone?: string
  address?: string
  visit_no: string
  dept_name: string
  visit_time: string
  visit_type: string
  main_complaint: string
  present_illness?: string
  past_illness?: string
  allergy_history?: string
  diagnosis?: string
  prescription?: string
  suggestion?: string
  doctor_name: string
  signature_url?: string
  create_time?: string
  update_time?: string
  reply?: string
}

/**
 * 从对话中提取的结构化数据（用于生成病历）
 */
export interface ExtractedEmrData {
  patientName?: string
  gender?: string
  age?: number
  phone?: string
  address?: string
  deptName?: string
  visitType?: string
  mainComplaint?: string
  presentIllness?: string
  pastIllness?: string
  allergyHistory?: string
  diagnosis?: string
  prescription?: string
  suggestion?: string
  doctorName?: string
  signatureUrl?: string

  reply?: string
}

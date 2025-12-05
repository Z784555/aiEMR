import { API_CONFIG } from './apiService'
import type { EmrRecord } from '../types/emr'

const BASE_URL = (API_CONFIG.baseURL || '').replace(/\/$/, '')

function buildEndpoint(path: string): string {
  if (!BASE_URL) return path
  return path.startsWith('/') ? `${BASE_URL}${path}` : `${BASE_URL}/${path}`
}

async function requestEmr(
  path: string,
  input: string,
  paramName: string = 'msg',
  chatId?: string,
): Promise<string> {
  const endpoint = buildEndpoint(path)
  // 构建请求体数据（不再拼到URL）
  const requestBody: Record<string, string> = {}
  if (input) requestBody[paramName] = input
  if (chatId) requestBody['chatId'] = chatId

  const response = await fetch(endpoint, {
    method: 'POST', // 改为POST请求
    headers: {
      Accept: 'application/json, text/plain',
      'Content-Type': 'application/json', // 新增：指定请求体为JSON格式
    },
    mode: 'cors',
    body: JSON.stringify(requestBody), // 数据放入请求体，转为JSON字符串
  })

  if (!response.ok) {
    const body = await response.text().catch(() => '')
    throw new Error(body || `后端接口 ${path} 响应异常(${response.status})`)
  }

  return await response.text()
}

export async function fetchEmrRecord(input: string, chatId?: string): Promise<string> {
  return await requestEmr('/emr/gne', input, 'msg', chatId)
}

// 尝试从文本中解析JSON，支持带代码块的情况
export function tryParseJsonFromText(text: string): Record<string, unknown> | null {
  if (!text) return null
  const stripped = stripCodeFences(text)
  try {
    return JSON.parse(stripped)
  } catch {
    // 如果直接解析失败，尝试提取第一个JSON对象
    const start = stripped.indexOf('{')
    const end = stripped.lastIndexOf('}')
    if (start !== -1 && end !== -1 && end > start) {
      try {
        return JSON.parse(stripped.slice(start, end + 1))
      } catch {
        return null
      }
    }
    return null
  }
}

// 将后端返回的数据映射为前端格式，处理字段名转换和默认值
export function mapBackendRecordToEmr(data: Record<string, unknown>): EmrRecord {
  const nowIso = new Date().toISOString()
  return {
    id: null,
    patientName: requiredString(readValue(data, ['patientName', 'patient_name']), '信息不足'),
    gender: requiredString(readValue(data, ['gender']), '信息不足'),
    age: normalizeAge(readValue(data, ['age'])),
    phone: optionalString(readValue(data, ['phone'])),
    address: optionalString(readValue(data, ['address'])),
    visitNo: requiredString(readValue(data, ['visitNo', 'visit_no']), generateVisitNo()),
    deptName: requiredString(readValue(data, ['deptName', 'dept_name']), '信息不足'),
    visitTime: normalizeDateTime(readValue(data, ['visitTime', 'visit_time'])) || nowIso,
    visitType: requiredString(readValue(data, ['visitType', 'visit_type']), '初诊'),
    mainComplaint: requiredString(readValue(data, ['mainComplaint', 'main_complaint']), '信息不足'),
    presentIllness: optionalString(readValue(data, ['presentIllness', 'present_illness'])),
    pastIllness: optionalString(readValue(data, ['pastIllness', 'past_illness'])),
    allergyHistory: optionalString(readValue(data, ['allergyHistory', 'allergy_history'])),
    diagnosis: optionalString(readValue(data, ['diagnosis'])),
    prescription: optionalString(readValue(data, ['prescription'])),
    suggestion: optionalString(readValue(data, ['suggestion'])),
    doctorName: requiredString(readValue(data, ['doctorName', 'doctor_name']), '信息不足'),
    signatureUrl: optionalString(readValue(data, ['signatureUrl', 'signature_url'])),
    createTime: normalizeDateTime(readValue(data, ['createTime', 'create_time'])) || nowIso,
    updateTime: normalizeDateTime(readValue(data, ['updateTime', 'update_time'])) || nowIso,
    hospitalName: optionalString(readValue(data, ['hospitalName'])) || '云脑诊疗系统互联网医院',
    // 从后端返回数据中读取 reply，若无则 undefined
    reply: optionalString(readValue(data, ['reply'])),
  }
}

// 从对象中按优先级读取值，支持多个可能的字段名
function readValue(source: Record<string, unknown>, paths: string[]): unknown {
  for (const key of paths) {
    if (key in source && source[key] !== undefined && source[key] !== null) {
      return source[key]
    }
  }
  return undefined
}

// 去掉代码块标记
function stripCodeFences(text: string): string {
  return text
    .replace(/```json/gi, '')
    .replace(/```/g, '')
    .trim()
}

// 必填字段处理，空值返回默认值
function requiredString(value: unknown, fallback: string): string {
  return typeof value === 'string' && value.trim() ? value.trim() : fallback
}

// 可选字段处理，空值返回undefined
function optionalString(value: unknown): string | undefined {
  if (typeof value === 'string') {
    const trimmed = value.trim()
    return trimmed || undefined
  }
  return undefined
}

// 年龄标准化，支持字符串和数字，范围0-120
function normalizeAge(value: unknown): number {
  if (typeof value === 'number' && Number.isFinite(value)) {
    return Math.max(0, Math.min(120, Math.round(value)))
  }
  if (typeof value === 'string') {
    const digits = value.match(/\d+/)
    if (digits) {
      const parsed = parseInt(digits[0], 10)
      if (!Number.isNaN(parsed)) {
        return Math.max(0, Math.min(120, parsed))
      }
    }
  }
  return 0
}

// 日期时间标准化，转换为ISO格式
function normalizeDateTime(value: unknown): string | undefined {
  if (typeof value === 'string' && value.trim()) {
    const normalized = value
      .trim()
      .replace(/\//g, '-')
      .replace('年', '-')
      .replace('月', '-')
      .replace('日', '')
    const withT = normalized.includes('T') ? normalized : normalized.replace(' ', 'T')
    const date = new Date(withT)
    if (!Number.isNaN(date.getTime())) {
      return date.toISOString()
    }
  }
  return undefined
}

// 生成就诊号
function generateVisitNo(): string {
  return `MZ${Date.now().toString()}`
}

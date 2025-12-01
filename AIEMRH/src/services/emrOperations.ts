import { fetchEmrRecord, tryParseJsonFromText, mapBackendRecordToEmr } from './backendService'
import type { EmrRecord } from '../types/emr'

export interface OperationResult {
  success: boolean
  message: string
  data?: EmrRecord
  id?: number | null
}

// 查询当前病历
export async function queryEmr(): Promise<OperationResult> {
  try {
    const response = await fetchEmrRecord('显示当前病历信息')
    const parsed = tryParseJsonFromText(response)

    if (parsed) {
      return {
        success: true,
        message: (parsed.reply as string) || '查询成功',
        data: mapBackendRecordToEmr(parsed),
        id: (parsed.id as number | null) || null,
      }
    }

    return {
      success: false,
      message: '查询失败：无法解析响应',
    }
  } catch (error) {
    return {
      success: false,
      message: error instanceof Error ? error.message : '查询失败',
    }
  }
}

// 修改病历字段
export async function updateEmrField(field: string, value: string): Promise<OperationResult> {
  try {
    const instruction = `${field}改为${value}`
    const response = await fetchEmrRecord(instruction)
    const parsed = tryParseJsonFromText(response)

    if (parsed) {
      return {
        success: true,
        message: (parsed.reply as string) || '修改成功',
        data: mapBackendRecordToEmr(parsed),
        id: (parsed.id as number | null) || null,
      }
    }

    return {
      success: false,
      message: '修改失败：无法解析响应',
    }
  } catch (error) {
    return {
      success: false,
      message: error instanceof Error ? error.message : '修改失败',
    }
  }
}

// 保存病历（接受可选 record，若未传则从 sessionStorage 读取）
export async function saveEmr(record?: EmrRecord): Promise<OperationResult> {
  try {
    let emrToSave = record
    if (!emrToSave) {
      const raw = sessionStorage.getItem('current_emr')
      if (raw) {
        try {
          emrToSave = JSON.parse(raw) as EmrRecord
        } catch {
          /* ignore */
        }
      }
    }

    if (!emrToSave) {
      return { success: false, message: '没有可保存的病历数据' }
    }

    // 将整个病历对象序列化并附加到 msg 中，后端解析并返回新的病历对象
    const msg = `保存病历\n\n病历数据：${JSON.stringify(emrToSave)}`

    const responseText = await fetchEmrRecord(msg)
    const parsed = tryParseJsonFromText(responseText)

    if (parsed) {
      const mapped = mapBackendRecordToEmr(parsed)
      return {
        success: true,
        message: '保存成功',
        data: mapped,
        id: (parsed as any).id ?? mapped.id ?? null,
      }
    }

    return { success: false, message: '保存失败：后端未返回有效数据' }
  } catch (error) {
    return {
      success: false,
      message: error instanceof Error ? error.message : '保存失败',
    }
  }
}

// 更新病历
export async function updateEmr(): Promise<OperationResult> {
  try {
    const response = await fetchEmrRecord('更新病历')
    const parsed = tryParseJsonFromText(response)

    if (parsed) {
      return {
        success: true,
        message: (parsed.reply as string) || '更新成功',
        data: mapBackendRecordToEmr(parsed),
        id: (parsed.id as number | null) || null,
      }
    }

    return {
      success: false,
      message: '更新失败：无法解析响应',
    }
  } catch (error) {
    return {
      success: false,
      message: error instanceof Error ? error.message : '更新失败',
    }
  }
}

// 删除病历
export async function deleteEmr(emrId?: number): Promise<OperationResult> {
  try {
    const instruction = emrId ? `删除病历ID为${emrId}的记录` : '删除当前病历'
    const response = await fetchEmrRecord(instruction)
    const parsed = tryParseJsonFromText(response)

    if (parsed) {
      const reply = (parsed.reply as string) || ''
      const isSuccess = reply.includes('删除成功') || reply.includes('已删除')

      return {
        success: isSuccess,
        message: reply || '删除成功',
      }
    }

    return {
      success: false,
      message: '删除失败：无法解析响应',
    }
  } catch (error) {
    return {
      success: false,
      message: error instanceof Error ? error.message : '删除失败',
    }
  }
}

// 归档病历
export async function archiveEmr(record?: EmrRecord): Promise<OperationResult> {
  try {
    let emrToSave = record
    if (!emrToSave) {
      const raw = sessionStorage.getItem('current_emr')
      if (raw) {
        try {
          emrToSave = JSON.parse(raw) as EmrRecord
        } catch {
          /* ignore */
        }
      }
    }

    if (!emrToSave) {
      return { success: false, message: '没有可归档的病历数据' }
    }

    // 将整个病历对象序列化并附加到 msg 中，后端解析并返回新的病历对象
    const msg = `归档病历\n\n病历数据：${JSON.stringify(emrToSave)}`

    const response = await fetchEmrRecord(msg)
    const parsed = tryParseJsonFromText(response)

    if (parsed) {
      const reply = (parsed.reply as string) || ''
      const isSuccess = reply.includes('归档成功') || reply.includes('已归档')

      return {
        success: isSuccess,
        message: reply || '归档成功',
        data: mapBackendRecordToEmr(parsed),
        id: (parsed.id as number | null) || null,
      }
    }

    return {
      success: false,
      message: '归档失败：无法解析响应',
    }
  } catch (error) {
    return {
      success: false,
      message: error instanceof Error ? error.message : '归档失败',
    }
  }
}

// 发送自定义指令
export async function sendCustomInstruction(instruction: string): Promise<OperationResult> {
  try {
    const response = await fetchEmrRecord(instruction)
    const parsed = tryParseJsonFromText(response)

    if (parsed) {
      return {
        success: true,
        message: (parsed.reply as string) || '操作成功',
        data: mapBackendRecordToEmr(parsed),
        id: (parsed.id as number | null) || null,
      }
    }

    return {
      success: false,
      message: '操作失败：无法解析响应',
    }
  } catch (error) {
    return {
      success: false,
      message: error instanceof Error ? error.message : '操作失败',
    }
  }
}

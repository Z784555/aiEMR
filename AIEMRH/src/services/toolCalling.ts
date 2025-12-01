// 兼容层，保留旧接口
// 新代码建议直接使用 emrOperations.ts

import { saveEmr, archiveEmr } from './emrOperations'
import type { EmrRecord } from '../types/emr'
import type { SaveEmrResponse, ArchiveEmrResponse } from '../types/api'

export async function saveEmrTool(emrRecord: EmrRecord): Promise<SaveEmrResponse> {
  console.log('[Tool Calling] 调用 save_emr 工具，参数:', emrRecord)
  const result = await saveEmr()
  return {
    success: result.success,
    message: result.message,
    data: result.id ? { id: result.id, updateTime: result.data?.updateTime } : { id: 0 },
  }
}

export async function archiveEmrTool(emrRecord: EmrRecord): Promise<ArchiveEmrResponse> {
  console.log('[Tool Calling] 调用 archive_emr 工具，参数:', emrRecord)
  const result = await archiveEmr()
  return {
    success: result.success,
    message: result.message,
    data: {
      archiveId: result.id ? `ARCH-${result.id}` : 'ARCH-UNKNOWN',
      archivedAt: new Date().toISOString(),
    },
  }
}


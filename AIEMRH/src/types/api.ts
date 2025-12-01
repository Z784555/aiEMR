/**
 * API 接口类型定义
 */

/**
 * 通用API响应格式
 */
export interface ApiResponse<T = unknown> {
  /** 请求是否成功 */
  success: boolean
  /** 响应数据 */
  data: T
  /** 响应消息 */
  message: string
}

/**
 * 生成病历API响应
 */
export interface GenerateEmrResponse extends ApiResponse<EmrRecord> {}

/**
 * 保存病历API响应
 */
export interface SaveEmrResponse extends ApiResponse<{ id: number; updateTime?: string }> {}

/**
 * 归档病历API响应
 */
export interface ArchiveEmrResponse extends ApiResponse<{ archiveId: string; archivedAt: string }> {}

/**
 * API请求选项
 */
export interface ApiRequestOptions {
  /** HTTP方法 */
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH'
  /** 请求数据 */
  data?: unknown
}

/**
 * AI对话消息
 */
export interface ChatMessage {
  /** 消息角色 */
  role: 'user' | 'ai'
  /** 消息内容 */
  content: string
  /** 消息ID */
  id?: number
  /** 消息时间 */
  time?: string
  /** 工具调用信息（可选） */
  toolCall?: ToolCall
  /** 是否正在思考中（仅AI消息） */
  isThinking?: boolean
}

/**
 * 工具调用信息
 */
export interface ToolCall {
  /** 工具名称 */
  name: string
  /** 工具参数 */
  params?: Record<string, unknown>
  /** 工具执行结果 */
  result?: unknown
}

/**
 * AI对话API请求
 */
export interface AiChatRequest {
  /** 用户消息 */
  message: string
  /** 历史对话 */
  chatHistory: Array<{
    role: 'user' | 'ai'
    content: string
  }>
}

/**
 * AI对话API响应数据
 */
export interface AiChatResponseData {
  /** AI回复内容 */
  content: string
  /** 工具调用信息（可选） */
  toolCall?: ToolCall
}

/**
 * AI对话API响应
 */
export interface AiChatResponse extends ApiResponse<AiChatResponseData> {}

// 导入 EmrRecord 类型
import type { EmrRecord } from './emr'


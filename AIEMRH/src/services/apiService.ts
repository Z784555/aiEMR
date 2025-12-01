// API配置和工具函数
// 实际接口调用在 backendService.ts 和 emrOperations.ts

// eslint-disable-next-line @typescript-eslint/no-explicit-any
const env = import.meta.env as any
export const API_CONFIG = {
  baseURL: env.VITE_API_BASE_URL || 'http://localhost:9000',
  timeout: 100000,
} as const

// 驼峰转下划线
function camelToSnake(str: string): string {
  return str.replace(/[A-Z]/g, (letter) => `_${letter.toLowerCase()}`)
}

// 下划线转驼峰
function snakeToCamel(str: string): string {
  return str.replace(/_([a-z])/g, (_, letter: string) => letter.toUpperCase())
}

// 对象字段名转换：驼峰 -> 下划线
export function convertToSnakeCase<T extends Record<string, unknown>>(
  obj: T,
): Record<string, unknown> {
  if (!obj || typeof obj !== 'object') return obj
  if (Array.isArray(obj)) return obj.map(convertToSnakeCase) as unknown as Record<string, unknown>

  const result: Record<string, unknown> = {}
  for (const [key, value] of Object.entries(obj)) {
    if (key === 'hospitalName') continue
    const snakeKey = camelToSnake(key)
    result[snakeKey] =
      typeof value === 'object' && value !== null
        ? convertToSnakeCase(value as Record<string, unknown>)
        : value
  }
  return result
}

// 对象字段名转换：下划线 -> 驼峰
export function convertToCamelCase<T extends Record<string, unknown>>(
  obj: T,
): Record<string, unknown> {
  if (!obj || typeof obj !== 'object') return obj
  if (Array.isArray(obj)) return obj.map(convertToCamelCase) as unknown as Record<string, unknown>

  const result: Record<string, unknown> = {}
  for (const [key, value] of Object.entries(obj)) {
    const camelKey = snakeToCamel(key)
    result[camelKey] =
      typeof value === 'object' && value !== null
        ? convertToCamelCase(value as Record<string, unknown>)
        : value
  }
  return result
}

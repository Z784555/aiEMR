<script setup>
import { ref, computed, watch, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import {
  fetchEmrRecord,
  tryParseJsonFromText,
  mapBackendRecordToEmr,
} from '../services/backendService'
import {
  saveEmr as saveEmrOperation,
  archiveEmr as archiveEmrOperation,
} from '../services/emrOperations'

const router = useRouter()
const CACHE_KEY = 'emr_records_cache'
const CURRENT_KEY = 'current_emr'

// 默认病历数据，字段未知时显示"信息不足"
const defaultEmrRecord = {
  id: null,
  hospitalName: '云脑诊疗系统互联网医院',
  deptName: '信息不足',
  visitNo: '',
  visitTime: new Date().toISOString(),
  visitType: '初诊',
  patientName: '信息不足',
  gender: '信息不足',
  age: 0,
  phone: '',
  address: '',
  mainComplaint: '信息不足',
  presentIllness: '',
  pastIllness: '',
  allergyHistory: '',
  diagnosis: '',
  prescription: '',
  suggestion: '',
  doctorName: '信息不足',
  signatureUrl: '',
}

const chatHistory = ref([
  {
    id: 1,
    role: 'ai',
    time: getCurrentTime(),
    content:
      '您好，我是病历助手“智护医”。请按对话输入患者基本信息、主诉、诊断、处方等内容，右侧将实时生成病历。',
  },
])
const userInput = ref('')
const chatContainer = ref(null)
const infoMessage = ref('')
const errorMessage = ref('')

const emrRecord = ref({ ...defaultEmrRecord })
const cachedRecords = ref([])
const isEditing = ref(false)
const isSaving = ref(false)
const isArchiving = ref(false)
const saveMessage = ref('')
const archiveMessage = ref('')

const searchForm = ref({
  deptName: '',
  visitNo: '',
})
const searchResults = ref([])

const doctorNarrative = computed(() =>
  chatHistory.value
    .filter((msg) => msg.role === 'user')
    .map((msg) => msg.content)
    .join('\n'),
)

const formatVisitTime = computed(() => {
  const raw = emrRecord.value.visitTime
  if (!raw) return '—'
  const date = new Date(raw)
  if (Number.isNaN(date.getTime())) return raw
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(
    date.getMinutes(),
  ).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
})

watch(
  chatHistory,
  () => {
    nextTick(() => {
      if (chatContainer.value) {
        chatContainer.value.scrollTop = chatContainer.value.scrollHeight
      }
    })
  },
  { deep: true },
)

watch(
  emrRecord,
  () => {
    persistCurrentRecord()
  },
  { deep: true },
)

onMounted(() => {
  loadCachedRecords()
  loadCurrentRecord()
  searchResults.value = cachedRecords.value.filter(
    (item) => item.visitNo && item.visitNo.trim() !== '' && item.visitNo !== '信息不足',
  )
})

function getCurrentTime() {
  const now = new Date()
  return `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`
}

function buildAutoReply(content) {
  if (/主诉|症状|疼|痛/.test(content)) {
    return '已记录主诉信息，可继续补充现病史、既往史及检查结果。'
  }
  if (/处方|用药|治疗/.test(content)) {
    return '已记录处方/治疗建议，病历已实时更新。'
  }
  if (/就诊|科室|编号/.test(content)) {
    return '就诊信息已登记，病历已实时更新。'
  }
  return '信息已记录，病历已实时更新。'
}

async function sendMessage() {
  if (!userInput.value.trim()) return
  const text = userInput.value.trim()
  userInput.value = ''

  chatHistory.value.push({
    id: Date.now(),
    role: 'user',
    time: getCurrentTime(),
    content: text,
  })

  const thinkingId = Date.now() + 1
  chatHistory.value.push({
    id: thinkingId,
    role: 'ai',
    time: getCurrentTime(),
    content: '助手正在分析对话并更新病历...',
    isThinking: true,
  })

  try {
    const narrative = doctorNarrative.value.trim()
    // 将当前病历对象序列化并附加到请求 msg 中
    const emrString = recordToString(emrRecord.value, false)
    const baseInput = narrative || text
    const inputText = `${baseInput}\n\n病历数据：${emrString}`

    const responseText = await fetchEmrRecord(inputText)
    const parsed = tryParseJsonFromText(responseText)

    if (parsed) {
      const mapped = mapBackendRecordToEmr(parsed)
      applyEmrRecord(mapped)
      const aiReply = mapped.reply || parsed.reply || buildAutoReply(text)
      replaceThinkingMessage(thinkingId, aiReply)
    } else {
      replaceThinkingMessage(thinkingId, buildAutoReply(text))
    }
  } catch (error) {
    console.error('调用后端失败:', error)
    const message = error instanceof Error ? error.message : '请求失败，请稍后再试'
    replaceThinkingMessage(thinkingId, `❌ 调用失败：${message}`)
  }
}

function replaceThinkingMessage(id, content) {
  const index = chatHistory.value.findIndex((msg) => msg.id === id)
  if (index !== -1) {
    chatHistory.value[index] = {
      ...chatHistory.value[index],
      content,
      isThinking: false,
    }
  }
}

function applyEmrRecord(record, message) {
  const merged = { ...emrRecord.value }

  // 只合并非空字段，避免覆盖已有数据
  Object.keys(record).forEach((key) => {
    const value = record[key]
    if (typeof value === 'string' && value.trim() !== '') {
      merged[key] = value
    } else if (typeof value === 'number' && value !== 0) {
      merged[key] = value
    } else if (typeof value === 'boolean') {
      merged[key] = value
    } else if (key === 'id' && value !== null && value !== undefined) {
      merged[key] = value
    } else if (value !== null && value !== undefined) {
      merged[key] = value
    }
  })

  merged.hospitalName = merged.hospitalName || record.hospitalName || '云脑诊疗系统互联网医院'

  emrRecord.value = merged
  cacheRecord(emrRecord.value)
  sessionStorage.setItem(CURRENT_KEY, JSON.stringify(emrRecord.value))
  if (message) {
    chatHistory.value.push({
      id: Date.now() + 2,
      role: 'ai',
      time: getCurrentTime(),
      content: message,
    })
  }
}

function cacheRecord(record) {
  if (!record.visitNo || record.visitNo.trim() === '') {
    return
  }

  const list = [...cachedRecords.value]
  const idx = list.findIndex((item) => item.visitNo && item.visitNo === record.visitNo)
  if (idx !== -1) {
    list[idx] = { ...record }
  } else {
    list.unshift({ ...record })
  }
  cachedRecords.value = list
  localStorage.setItem(CACHE_KEY, JSON.stringify(list))
}

function loadCachedRecords() {
  const cached = localStorage.getItem(CACHE_KEY)
  if (!cached) return
  try {
    const list = JSON.parse(cached)
    if (Array.isArray(list) && list.length > 0) {
      cachedRecords.value = list.filter(
        (item) => item.visitNo && item.visitNo.trim() !== '' && item.visitNo !== '信息不足',
      )
    }
  } catch {
    // 缓存损坏时忽略
  }
}

function loadCurrentRecord() {
  const stored = sessionStorage.getItem(CURRENT_KEY)
  if (stored) {
    try {
      const parsed = JSON.parse(stored)
      emrRecord.value = { ...defaultEmrRecord, ...parsed }
      cacheRecord(emrRecord.value)
      return
    } catch {
      // ignore
    }
  }
  persistCurrentRecord()
}

function persistCurrentRecord() {
  sessionStorage.setItem(CURRENT_KEY, JSON.stringify(emrRecord.value))
  cacheRecord(emrRecord.value)
}

// 新增：将 emrRecord 转为字符串（类似 Java 的 toString），默认不美化以减小 URL 长度
function recordToString(record = emrRecord.value, pretty = false) {
  try {
    return pretty ? JSON.stringify(record, null, 2) : JSON.stringify(record)
  } catch {
    return String(record)
  }
}

function toggleEdit() {
  isEditing.value = !isEditing.value
  if (!isEditing.value) {
    saveMessage.value = ''
  }
}

async function saveEmr() {
  if (isSaving.value) return
  isSaving.value = true
  saveMessage.value = ''
  try {
    const result = await saveEmrOperation(emrRecord.value)
    if (result.success) {
      saveMessage.value = `✅ ${result.message}`
      infoMessage.value = result.message
      if (result.id && result.data) {
        emrRecord.value.id = result.id
        applyEmrRecord(result.data)
      }
    } else {
      saveMessage.value = `❌ ${result.message}`
    }
  } catch (error) {
    console.error('保存病历失败:', error)
    const message = error instanceof Error ? error.message : '未知错误'
    saveMessage.value = `❌ 保存失败: ${message}`
  } finally {
    isSaving.value = false
    isEditing.value = false
  }
}

async function archiveEmr() {
  if (isArchiving.value) return
  if (!confirm('确定要归档该病历并提交后台系统吗？')) return
  isArchiving.value = true
  archiveMessage.value = '正在归档...'
  try {
    const result = await archiveEmrOperation(emrRecord.value)
    if (result.success) {
      archiveMessage.value = `✅ ${result.message}`
    } else {
      archiveMessage.value = `❌ ${result.message}`
    }
  } catch (error) {
    console.error('归档病历失败:', error)
    const message = error instanceof Error ? error.message : '未知错误'
    archiveMessage.value = `❌ 归档失败: ${message}`
  } finally {
    isArchiving.value = false
    setTimeout(() => {
      archiveMessage.value = ''
    }, 4000)
  }
}

function printEmr() {
  window.print()
}

function goToEmrPage() {
  router.push({ name: 'emr' })
}

function runSearch() {
  const dept = searchForm.value.deptName.trim().toLowerCase()
  const visitNo = searchForm.value.visitNo.trim().toLowerCase()
  const validRecords = cachedRecords.value.filter(
    (item) => item.visitNo && item.visitNo.trim() !== '' && item.visitNo !== '信息不足',
  )
  searchResults.value = validRecords.filter((item) => {
    const deptMatch = dept ? item.deptName.toLowerCase().includes(dept) : true
    const visitMatch = visitNo ? item.visitNo.toLowerCase().includes(visitNo) : true
    return deptMatch && visitMatch
  })
}

function resetSearch() {
  searchForm.value = { deptName: '', visitNo: '' }
  searchResults.value = cachedRecords.value.filter(
    (item) => item.visitNo && item.visitNo.trim() !== '' && item.visitNo !== '信息不足',
  )
}

function loadRecord(record) {
  emrRecord.value = { ...record }
  persistCurrentRecord()
  chatHistory.value.push({
    id: Date.now() + 3,
    role: 'ai',
    time: getCurrentTime(),
    content: `已加载就诊号 ${record.visitNo || '未知'} 的病历，可继续通过对话进行修改。`,
  })
}
</script>

<template>
  <div class="workspace">
    <section class="chat-panel">
      <header>
        <p class="tag">云脑诊疗系统 · 病历助手</p>
        <h2>像聊天一样完成诊疗流程</h2>
        <p class="desc">在对话框中描述患者信息，右侧病历会实时同步更新。</p>
      </header>

      <div class="chat-history" ref="chatContainer">
        <ul>
          <li v-for="message in chatHistory" :key="message.id" :class="['chat-row', message.role]">
            <div class="bubble" :class="{ thinking: message.isThinking }">
              <p class="bubble-content">
                <span v-if="message.isThinking" class="thinking-dots">
                  <span></span><span></span><span></span>
                </span>
                <span v-else>{{ message.content }}</span>
              </p>
              <div class="bubble-footer">
                <span class="bubble-meta"
                  >{{ message.role === 'user' ? '医生' : '助手' }} · {{ message.time }}</span
                >
              </div>
            </div>
          </li>
        </ul>
      </div>

      <footer class="chat-input">
        <input
          v-model="userInput"
          type="text"
          placeholder="请输入患者信息、主诉、诊断、处方等内容..."
          @keyup.enter="sendMessage"
        />
        <button type="button" @click="sendMessage" :disabled="!userInput.trim()">发送</button>
      </footer>

      <p v-if="errorMessage" class="message error">{{ errorMessage }}</p>
      <p v-if="infoMessage" class="message info">{{ infoMessage }}</p>
    </section>

    <section class="workbench">
      <article class="emr-card">
        <header class="emr-header">
          <div>
            <h3>{{ emrRecord.hospitalName }}</h3>
            <p>互联网医院电子病历</p>
          </div>
          <div class="header-actions">
            <button type="button" class="btn-edit" v-if="!isEditing" @click="toggleEdit">
              编辑
            </button>
            <button
              type="button"
              class="btn-save"
              v-if="isEditing"
              :disabled="isSaving"
              @click="saveEmr"
            >
              {{ isSaving ? '保存中...' : '保存' }}
            </button>
            <button type="button" class="btn-cancel" v-if="isEditing" @click="toggleEdit">
              取消
            </button>
            <button
              type="button"
              class="btn-archive"
              :disabled="isEditing || isArchiving"
              @click="archiveEmr"
            >
              {{ isArchiving ? '归档中...' : '归档' }}
            </button>
            <button type="button" class="btn-print" @click="printEmr">打印</button>
            <button type="button" class="btn-back" @click="goToEmrPage">打开完整病历页</button>
          </div>
        </header>

        <div v-if="saveMessage" class="message success">{{ saveMessage }}</div>
        <div v-if="archiveMessage" class="message info">{{ archiveMessage }}</div>

        <div class="emr-row info">
          <span v-if="!isEditing">姓名：{{ emrRecord.patientName || '信息不足' }}</span>
          <span v-else>姓名：<input v-model="emrRecord.patientName" class="edit-input" /></span>
          <span v-if="!isEditing">性别：{{ emrRecord.gender || '信息不足' }}</span>
          <span v-else>
            性别：
            <select v-model="emrRecord.gender" class="edit-select">
              <option>男</option>
              <option>女</option>
              <option>其他</option>
            </select>
          </span>
          <span v-if="!isEditing"
            >年龄：{{ emrRecord.age && emrRecord.age > 0 ? emrRecord.age : '信息不足' }}</span
          >
          <span v-else
            >年龄：<input v-model.number="emrRecord.age" class="edit-input short" type="number"
          /></span>
        </div>

        <div class="emr-row info">
          <span v-if="!isEditing">就诊科室：{{ emrRecord.deptName || '信息不足' }}</span>
          <span v-else>就诊科室：<input v-model="emrRecord.deptName" class="edit-input" /></span>
          <span>就诊号：{{ emrRecord.visitNo || '信息不足' }}</span>
        </div>

        <div class="emr-row info">
          <span>就诊时间：{{ formatVisitTime }}</span>
          <span v-if="!isEditing">就诊类型：{{ emrRecord.visitType || '初诊' }}</span>
          <span v-else>
            就诊类型：
            <select v-model="emrRecord.visitType" class="edit-select">
              <option>初诊</option>
              <option>复诊</option>
              <option>急诊</option>
              <option>转诊</option>
            </select>
          </span>
        </div>

        <div class="emr-row info">
          <span v-if="!isEditing">联系电话：{{ emrRecord.phone || '—' }}</span>
          <span v-else>联系电话：<input v-model="emrRecord.phone" class="edit-input" /></span>
          <span v-if="!isEditing">家庭住址：{{ emrRecord.address || '—' }}</span>
          <span v-else>家庭住址：<input v-model="emrRecord.address" class="edit-input" /></span>
        </div>

        <dl class="emr-section">
          <dt>主诉：</dt>
          <dd v-if="!isEditing">{{ emrRecord.mainComplaint || '信息不足' }}</dd>
          <dd v-else>
            <textarea v-model="emrRecord.mainComplaint" class="edit-textarea" rows="2"></textarea>
          </dd>
        </dl>

        <dl class="emr-section">
          <dt>现病史：</dt>
          <dd v-if="!isEditing">{{ emrRecord.presentIllness || '—' }}</dd>
          <dd v-else>
            <textarea v-model="emrRecord.presentIllness" class="edit-textarea" rows="3"></textarea>
          </dd>
        </dl>

        <dl class="emr-section">
          <dt>既往史：</dt>
          <dd v-if="!isEditing">{{ emrRecord.pastIllness || '—' }}</dd>
          <dd v-else>
            <textarea v-model="emrRecord.pastIllness" class="edit-textarea" rows="2"></textarea>
          </dd>
        </dl>

        <dl class="emr-section">
          <dt>过敏史：</dt>
          <dd v-if="!isEditing">{{ emrRecord.allergyHistory || '—' }}</dd>
          <dd v-else>
            <textarea v-model="emrRecord.allergyHistory" class="edit-textarea" rows="2"></textarea>
          </dd>
        </dl>

        <dl class="emr-section">
          <dt>诊断：</dt>
          <dd v-if="!isEditing">{{ emrRecord.diagnosis || '—' }}</dd>
          <dd v-else>
            <textarea v-model="emrRecord.diagnosis" class="edit-textarea" rows="2"></textarea>
          </dd>
        </dl>

        <dl class="emr-section">
          <dt>处方：</dt>
          <dd v-if="!isEditing">{{ emrRecord.prescription || '—' }}</dd>
          <dd v-else>
            <textarea v-model="emrRecord.prescription" class="edit-textarea" rows="3"></textarea>
          </dd>
        </dl>

        <dl class="emr-section">
          <dt>医嘱：</dt>
          <dd v-if="!isEditing">{{ emrRecord.suggestion || '—' }}</dd>
          <dd v-else>
            <textarea v-model="emrRecord.suggestion" class="edit-textarea" rows="2"></textarea>
          </dd>
        </dl>

        <div class="emr-footer">
          <span v-if="!isEditing">医师签字：{{ emrRecord.doctorName || '信息不足' }}</span>
          <span v-else>医师签字：<input v-model="emrRecord.doctorName" class="edit-input" /></span>
          <span>手签：{{ emrRecord.signatureUrl ? '已上传' : '待上传' }}</span>
        </div>
      </article>

      <section class="search-panel">
        <div class="search-header">
          <div>
            <p class="search-title">病历查询</p>
            <p class="search-desc">根据就诊科室、就诊号筛选已生成的病历</p>
          </div>
          <div class="search-actions">
            <button type="button" @click="runSearch">查询</button>
            <button type="button" class="ghost" @click="resetSearch">重置</button>
          </div>
        </div>

        <div class="search-form">
          <label>
            就诊科室
            <input v-model="searchForm.deptName" placeholder="如：呼吸内科" />
          </label>
          <label>
            就诊号
            <input v-model="searchForm.visitNo" placeholder="如：MZ20241122001" />
          </label>
        </div>

        <div class="result-list" v-if="searchResults.length">
          <article v-for="record in searchResults" :key="record.visitNo" class="result-item">
            <div>
              <p class="record-title">
                {{
                  record.patientName && record.patientName !== '信息不足'
                    ? record.patientName
                    : '未知患者'
                }}
                ·
                {{
                  record.deptName && record.deptName !== '信息不足' ? record.deptName : '未知科室'
                }}
              </p>
              <p class="record-meta">
                就诊号：{{ record.visitNo || '—' }} ｜ 主诉：{{
                  record.mainComplaint && record.mainComplaint !== '信息不足'
                    ? record.mainComplaint
                    : '—'
                }}
              </p>
            </div>
            <button type="button" @click="loadRecord(record)">加载病历</button>
          </article>
        </div>
        <p v-else class="placeholder">暂无匹配记录，可先完成一次生成。</p>
      </section>
    </section>
  </div>
</template>

<style scoped>
.workspace {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1.1fr);
  gap: 20px;
  padding: 16px;
  width: 100%;
  max-width: 100%;
  margin: 0;
  box-sizing: border-box;
}

.chat-panel,
.workbench {
  background: #fff;
  border-radius: 16px;
  padding: 18px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 12px;
  min-height: calc(100vh - 200px);
  max-height: calc(100vh - 200px);
  overflow: hidden;
  box-sizing: border-box;
}

.tag {
  margin: 0;
  color: #60a5fa;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.desc {
  margin: 6px 0 0;
  color: #475569;
}

.chat-history {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  border-radius: 12px;
  background: #f8fafc;
  padding: 12px;
  min-height: 0;
  /* Firefox 滚动条样式 */
  scrollbar-width: thin;
  scrollbar-color: #cbd5e1 transparent;
}

.chat-history::-webkit-scrollbar {
  width: 6px;
}

.chat-history::-webkit-scrollbar-track {
  background: transparent;
  border-radius: 3px;
}

.chat-history::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.chat-history::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

.chat-history ul {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.chat-row {
  display: flex;
}

.chat-row.user {
  justify-content: flex-end;
}

.chat-row.ai {
  justify-content: flex-start;
}

.bubble {
  max-width: 75%;
  padding: 12px 16px;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background: #e0f2fe;
  position: relative;
}

.chat-row.user .bubble {
  background: #f1f5f9;
}

.bubble-content {
  margin: 0 0 6px;
  white-space: pre-wrap;
}

.bubble-meta {
  font-size: 12px;
  color: #94a3b8;
}

.bubble.thinking {
  opacity: 0.8;
}

.thinking-dots {
  display: inline-flex;
  gap: 4px;
}

.thinking-dots span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #60a5fa;
  animation: thinking 1.4s infinite ease-in-out;
}

.thinking-dots span:nth-child(1) {
  animation-delay: -0.32s;
}

.thinking-dots span:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes thinking {
  0%,
  80%,
  100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.chat-input {
  display: flex;
  gap: 10px;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  padding: 12px;
  background: #fff;
}

.chat-input input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 14px;
}

.chat-input button {
  background: #3b82f6;
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 8px 20px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s;
}

.chat-input button:hover:not(:disabled) {
  background: #2563eb;
}

.chat-input button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.message {
  margin: 0;
  padding: 12px 14px;
  border-radius: 12px;
  font-size: 14px;
}

.message.error {
  background: #fee2e2;
  color: #b91c1c;
}

.message.info {
  background: #e0f2fe;
  color: #1d4ed8;
}

.message.success {
  background: #d1fae5;
  color: #047857;
}

.emr-card {
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  background: #fff;
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  /* Firefox 滚动条样式 */
  scrollbar-width: thin;
  scrollbar-color: #cbd5e1 transparent;
}

.emr-card::-webkit-scrollbar {
  width: 6px;
}

.emr-card::-webkit-scrollbar-track {
  background: transparent;
  border-radius: 3px;
}

.emr-card::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.emr-card::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

.emr-header {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
  border-bottom: 2px solid #bfdbfe;
  padding-bottom: 12px;
}

.emr-header h3 {
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.header-actions button {
  border: none;
  border-radius: 8px;
  padding: 8px 16px;
  font-weight: 600;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.btn-edit {
  background: #60a5fa;
  color: #fff;
}

.btn-edit:hover {
  background: #3b82f6;
}

.btn-save {
  background: #10b981;
  color: #fff;
}

.btn-save:hover:not(:disabled) {
  background: #059669;
}

.btn-cancel {
  background: #6b7280;
  color: #fff;
}

.btn-cancel:hover {
  background: #4b5563;
}

.btn-archive {
  background: #8b5cf6;
  color: #fff;
}

.btn-archive:hover:not(:disabled) {
  background: #7c3aed;
}

.btn-print {
  background: #f59e0b;
  color: #fff;
}

.btn-print:hover {
  background: #d97706;
}

.btn-back {
  background: #60a5fa;
  color: #fff;
}

.btn-back:hover {
  background: #3b82f6;
}

.header-actions button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.emr-row {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  padding: 6px 0;
}

.emr-section {
  margin: 0 0 10px 0;
  display: flex;
  gap: 10px;
}

.emr-section dt {
  min-width: 60px;
  font-weight: 600;
  font-size: 14px;
}

.emr-section dd {
  margin: 0;
  flex: 1;
}

.edit-input,
.edit-select,
.edit-textarea {
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  padding: 6px 10px;
  font-size: 14px;
}

.edit-textarea {
  width: 100%;
  resize: vertical;
}

.edit-input.short {
  width: 70px;
}

.emr-footer {
  display: flex;
  justify-content: space-between;
  border-top: 1px dashed #cbd5f5;
  padding-top: 12px;
}

.search-panel {
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  background: #fff;
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  /* Firefox 滚动条样式 */
  scrollbar-width: thin;
  scrollbar-color: #cbd5e1 transparent;
}

.search-panel::-webkit-scrollbar {
  width: 6px;
}

.search-panel::-webkit-scrollbar-track {
  background: transparent;
  border-radius: 3px;
}

.search-panel::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.search-panel::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}

.search-title {
  margin: 0;
  font-weight: 700;
}

.search-desc {
  margin: 4px 0 0;
  color: #94a3b8;
  font-size: 13px;
}

.search-actions button {
  border: none;
  border-radius: 6px;
  padding: 6px 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.search-actions .ghost {
  background: transparent;
  color: #2563eb;
  border: 1px solid #93c5fd;
}

.search-actions button:not(.ghost) {
  background: #2563eb;
  color: #fff;
}

.search-form {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.search-form label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 13px;
  color: #475569;
}

.search-form input {
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  padding: 8px 12px;
}

.result-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.result-item {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 12px;
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: center;
  transition: background 0.2s;
}

.result-item:hover {
  background: #f8fafc;
}

.record-title {
  margin: 0;
  font-weight: 600;
}

.record-meta {
  margin: 4px 0 0;
  font-size: 13px;
  color: #94a3b8;
}

.result-item button {
  border: none;
  border-radius: 6px;
  padding: 6px 14px;
  background: #0ea5e9;
  color: #fff;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.2s;
}

.result-item button:hover {
  background: #0284c7;
}

.placeholder {
  margin: 0;
  color: #94a3b8;
  text-align: center;
}

@media (max-width: 1200px) {
  .workspace {
    grid-template-columns: 1fr;
    gap: 16px;
    padding: 12px;
  }

  .chat-panel,
  .workbench {
    height: auto;
    max-height: calc(50vh - 24px);
  }
}

@media print {
  .workspace,
  .chat-panel,
  .workbench,
  .search-panel,
  .header-actions,
  .chat-input,
  .search-actions {
    box-shadow: none;
  }
  .chat-panel,
  .search-panel {
    display: none;
  }
  .workbench {
    padding: 0;
  }
}
</style>

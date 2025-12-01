<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { saveEmr as saveEmrOperation, archiveEmr as archiveEmrOperation } from '../services/emrOperations'

const router = useRouter()

const defaultEmrRecord = {
  hospitalName: '复旦大学附属华山医院',
  deptName: '中医内科',
  visitNo: 'MZ07882405098',
  visitTime: '2024-05-09T14:21:52',
  visitType: '初诊',
  patientName: '蔡志军',
  gender: '男',
  age: 45,
  phone: '13920631008',
  address: '上海市宝山区上大路99号',
  mainComplaint: '咽喉肿痛 3 天，加重 1 天',
  presentIllness: '患者 3 天前受凉后出现咽喉不适，伴低热，精神尚可。',
  pastIllness: '既往体健，无慢性病史。',
  allergyHistory: '否认药物过敏史。',
  diagnosis: '风热感冒',
  prescription: '银翘散加减，3 剂，水煎服。',
  suggestion: '多饮水、清淡饮食，注意休息，若 3 天后未改善复诊。',
  doctorName: '贾连荣',
  signatureUrl: '',
}

const emrRecord = ref({ ...defaultEmrRecord })
const isEditing = ref(false)
const isSaving = ref(false)
const isArchiving = ref(false)
const saveMessage = ref('')
const archiveMessage = ref('')

onMounted(() => {
  const savedEmr = sessionStorage.getItem('current_emr')
  if (savedEmr) {
    try {
      emrRecord.value = JSON.parse(savedEmr)
      sessionStorage.removeItem('current_emr')
    } catch (error) {
      console.error('加载病历数据失败:', error)
    }
  }
})

const formatVisitTime = computed(() => {
  const date = new Date(emrRecord.value.visitTime)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${
    String(date.getHours()).padStart(2, '0')
  }:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
})

const toggleEdit = () => {
  isEditing.value = !isEditing.value
  if (!isEditing.value) {
    saveMessage.value = ''
  }
}

const saveEmr = async () => {
  if (isSaving.value) return

  isSaving.value = true
  saveMessage.value = ''

  try {
    const result = await saveEmrOperation()
    if (result.success) {
      saveMessage.value = '✅ ' + result.message
      if (result.id && result.data) {
        emrRecord.value.id = result.id
        Object.assign(emrRecord.value, result.data)
      }
      isEditing.value = false
      setTimeout(() => {
        saveMessage.value = ''
      }, 3000)
    } else {
      saveMessage.value = '❌ ' + result.message
    }
  } catch (error) {
    console.error('保存病历失败:', error)
    saveMessage.value = '❌ 保存失败: ' + (error instanceof Error ? error.message : '未知错误')
  } finally {
    isSaving.value = false
  }
}

const archiveEmr = async () => {
  if (isArchiving.value) return

  if (!confirm('确定要归档这份病历吗？归档后将提交到后台系统。')) {
    return
  }

  isArchiving.value = true
  archiveMessage.value = '正在归档...'

  try {
    const result = await archiveEmrOperation()
    if (result.success) {
      archiveMessage.value = `✅ ${result.message}`
      setTimeout(() => {
        archiveMessage.value = ''
      }, 5000)
    } else {
      archiveMessage.value = '❌ ' + result.message
    }
  } catch (error) {
    console.error('归档病历失败:', error)
    archiveMessage.value = '❌ 归档失败: ' + (error instanceof Error ? error.message : '未知错误')
  } finally {
    isArchiving.value = false
  }
}

const backHome = () => {
  router.push({ name: 'home' })
}

const printEmr = () => {
  window.print()
}
</script>

<template>
  <section class="emr-page">
    <header class="emr-header">
      <div>
        <h2>{{ emrRecord.hospitalName }}</h2>
        <p>互联网医院电子病历</p>
      </div>
      <div class="header-actions">
        <button v-if="!isEditing" type="button" @click="toggleEdit" class="btn-edit">编辑</button>
        <button v-if="isEditing" type="button" @click="saveEmr" :disabled="isSaving" class="btn-save">
          {{ isSaving ? '保存中...' : '保存' }}
        </button>
        <button v-if="isEditing" type="button" @click="toggleEdit" class="btn-cancel">取消</button>
        <button type="button" @click="archiveEmr" :disabled="isArchiving || isEditing" class="btn-archive">
          {{ isArchiving ? '归档中...' : '归档' }}
        </button>
        <button type="button" @click="printEmr" class="btn-print">打印</button>
        <button type="button" @click="backHome" class="btn-back">返回首页</button>
      </div>
    </header>

    <div v-if="saveMessage" class="message success">{{ saveMessage }}</div>
    <div v-if="archiveMessage" class="message" :class="{ success: archiveMessage.includes('✅'), info: archiveMessage.includes('正在') }">
      {{ archiveMessage }}
    </div>

    <article class="emr-card">
      <div class="emr-row info">
        <span v-if="!isEditing">姓名：{{ emrRecord.patientName || '信息不足' }}</span>
        <span v-else>
          姓名：<input v-model="emrRecord.patientName" class="edit-input" />
        </span>
        <span v-if="!isEditing">性别：{{ emrRecord.gender || '信息不足' }}</span>
        <span v-else>
          性别：
          <select v-model="emrRecord.gender" class="edit-select">
            <option>男</option>
            <option>女</option>
            <option>其他</option>
          </select>
        </span>
        <span v-if="!isEditing">年龄：{{ emrRecord.age && emrRecord.age > 0 ? emrRecord.age : '信息不足' }}</span>
        <span v-else>
          年龄：<input v-model.number="emrRecord.age" type="number" class="edit-input short" />
        </span>
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
        <span v-else>
          联系电话：<input v-model="emrRecord.phone" class="edit-input" />
        </span>
        <span v-if="!isEditing">家庭住址：{{ emrRecord.address || '—' }}</span>
        <span v-else>
          家庭住址：<input v-model="emrRecord.address" class="edit-input" />
        </span>
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
        <span v-else>
          医师签字：<input v-model="emrRecord.doctorName" class="edit-input" />
        </span>
        <span>手签：{{ emrRecord.signatureUrl ? '已上传' : '待上传' }}</span>
      </div>
      <p class="pagination">第 1 页，共 1 页</p>
    </article>
  </section>
</template>

<style scoped>
.emr-page {
  background: #fff;
  border-radius: 24px;
  padding: 40px;
  box-shadow: 0 30px 50px rgba(15, 23, 42, 0.12);
}

.emr-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 2px solid #60A5FA;
  padding-bottom: 20px;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 16px;
}

.emr-header h2 {
  margin: 0;
  font-size: 28px;
}

.emr-header p {
  margin: 4px 0 0;
  color: #475569;
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
  background: #60A5FA;
  color: #fff;
}

.btn-edit:hover {
  background: #3B82F6;
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
  background: #60A5FA;
  color: #fff;
}

.btn-back:hover {
  background: #3B82F6;
}

.header-actions button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.message {
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 16px;
  font-size: 14px;
}

.message.success {
  background: #d1fae5;
  color: #065f46;
  border: 1px solid #10b981;
}

.message.info {
  background: #dbeafe;
  color: #1e40af;
  border: 1px solid #3b82f6;
}

.emr-card {
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 18px;
  font-size: 15px;
}

.emr-row {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: center;
}

.emr-row span {
  color: #1e293b;
}

.emr-section {
  margin: 0;
  display: flex;
  gap: 12px;
  color: #0f172a;
  line-height: 1.8;
  align-items: flex-start;
}

.emr-section dt {
  font-weight: 600;
  min-width: 72px;
  flex-shrink: 0;
}

.emr-section dd {
  margin: 0;
  flex: 1;
}

.edit-input,
.edit-select {
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  padding: 6px 10px;
  font-size: 14px;
  background: #fff;
}

.edit-input.short {
  width: 70px;
}

.edit-input:focus,
.edit-select:focus {
  outline: none;
  border-color: #60A5FA;
  box-shadow: 0 0 0 3px rgba(96, 165, 250, 0.1);
}

.edit-textarea {
  width: 100%;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  padding: 8px 10px;
  font-size: 14px;
  font-family: inherit;
  resize: vertical;
  min-height: 60px;
}

.edit-textarea:focus {
  outline: none;
  border-color: #60A5FA;
  box-shadow: 0 0 0 3px rgba(96, 165, 250, 0.1);
}

.emr-footer {
  display: flex;
  justify-content: space-between;
  border-top: 1px dashed #cbd5f5;
  padding-top: 16px;
  color: #1e293b;
  align-items: center;
}

.pagination {
  text-align: center;
  color: #94a3b8;
  margin: 0;
}

@media (max-width: 640px) {
  .emr-page {
    padding: 24px;
  }

  .emr-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .header-actions {
    width: 100%;
  }

  .header-actions button {
    flex: 1;
    min-width: 80px;
  }

  .emr-row {
    flex-direction: column;
    align-items: flex-start;
  }

  .emr-section {
    flex-direction: column;
  }

  .emr-footer {
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
  }
}

@media print {
  .emr-header .header-actions,
  .message {
    display: none;
  }

  .emr-page {
    box-shadow: none;
    padding: 0;
  }
}
</style>

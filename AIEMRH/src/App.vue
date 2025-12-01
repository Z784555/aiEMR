<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="app-shell">
    <header class="app-header">
      <div>
        <p class="app-header-label">云脑诊疗系统 · 病历助手</p>
        <h1>病历助手</h1>
        <p class="app-header-desc">智能诊疗流程，自动生成病历，辅助医生高效诊疗。</p>
      </div>
      <nav class="app-nav">
        <RouterLink to="/">首页</RouterLink>
        <RouterLink to="/emr">诊疗记录</RouterLink>
      </nav>
    </header>

    <main class="app-main">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
:global(body) {
  margin: 0;
  font-family: 'Segoe UI', -apple-system, BlinkMacSystemFont, 'PingFang SC', sans-serif;
  background: #f4f6fb;
  color: #0f172a;
}

.app-shell {
  min-height: 100vh;
  padding: 16px 12px 20px;
  max-width: 100%;
  margin: 0;
  box-sizing: border-box;
}

.app-header {
  background: linear-gradient(120deg, #60A5FA, #93C5FD);
  border-radius: 16px;
  padding: 20px 24px;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  box-shadow: 0 2px 8px rgba(96, 165, 250, 0.25);
}

.app-header-label {
  margin: 0 0 8px;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  opacity: 0.85;
  font-size: 13px;
}

.app-header h1 {
  margin: 0 0 6px;
  font-size: 32px;
}

.app-header-desc {
  margin: 0;
  opacity: 0.9;
}

.app-nav {
  display: flex;
  gap: 16px;
}

.app-nav a {
  color: #fff;
  text-decoration: none;
  font-weight: 600;
  padding: 10px 16px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.18);
  transition: background 0.2s;
}

.app-nav a.router-link-exact-active {
  background: #fff;
  color: #3B82F6;
}

.app-main {
  margin-top: 16px;
  min-height: calc(100vh - 160px);
  overflow: visible;
}

@media (max-width: 768px) {
  .app-header {
    flex-direction: column;
    text-align: center;
  }

  .app-nav {
    flex-wrap: wrap;
    justify-content: center;
  }
}
</style>

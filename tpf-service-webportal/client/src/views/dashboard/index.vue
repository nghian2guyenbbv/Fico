<template>
  <div class="dashboard-container">
    <component :is="'editorDashboard'" />
  </div>
</template>

<script>
// import adminDashboard from './admin'
import editorDashboard from './editor'

export default {
  name: 'Dashboard',
  components: { editorDashboard },
  created() {
    this.currentRole = 'editorDashboard'
    if (this.roles) {

    }

    if (this.roles && this.roles.includes('admin')) {
      this.currentRole = 'editorDashboard'
    }
  }
}
</script>

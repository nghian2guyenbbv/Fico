<template>
  <div>
    <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
      New
    </el-button>

    <el-dialog title="Outer Dialog" :visible.sync="outerVisible">
        <el-dialog
            width="30%"
            title="Inner Dialog"
            :visible.sync="innerVisible"
            append-to-body>
        </el-dialog>
        <div slot="footer" class="dialog-footer">
        <el-button @click="outerVisible = false">Cancel</el-button>
        <el-button type="primary" @click="innerVisible = true">open the inner Dialog</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: 'dialog-create',
  data() {
    return {
      outerVisible: false,
      innerVisible: false
    };
  },

  props: {
  },
  
  methods: {
  }
}
</script>

<style scoped>
</style>

<template>
  <el-dialog :visible.sync="state.momo['Documents'].show"> 
    <tpf-image-slide :items="state.momo['Documents'].items"></tpf-image-slide>
  </el-dialog>
</template>

<script>
import { log } from "util";
export default {
  name: "tpf-dialog",
  data() {
    return {};
  },
  props:{
    test: ''
  },
  methods: {
    handleClose(done) {
      this.state.momo['Documents'].show = false;
    }
  },
};
</script>   
<style>
</style>
<template>
  <el-table
    v-loading
    :data="data"
    border
    fit
    highlight-current-row
    style="width: 100%;"
    height="70vh"
  >
    <div v-for="item in headers" :key="item.key">
      <el-table-column :label="item.title" :prop="item.key" sortable="custom" align="center">
        <template slot-scope="scope">
          <span>{{ renderCol(scope.row[item.key], item.type) }}</span>
        </template>
      </el-table-column>
    </div>
  </el-table>
</template>

<script>
export default {
  name: 'TableDefault',

  props: {
    // iconClass: {
    //   type: String,
    //   required: true
    // },
    // className: {
    //   type: String,
    //   default: ''
    // }
  },
  data() {
    return {
      data: [],
      headers: [
        { key: "createdAt", title: "createdAt", align: "center", header_align: "center", type: 'DateTime' },
        { key: "appId", title: "appId", align: "center", header_align: "center" },
        { key: "status", title: "status", align: "center", header_align: "center" },
        { key: "fullName", title: "fullName", align: "center", header_align: "center" },
        { key: "nationalId", title: "nationalId", align: "center", header_align: "center" }
      ]
    }
  },
  created() {
    this.data = require("@/store/data").data;
    // this.headers.unshift(this.headers.pop());
  },
  computed: {
  },
  methods: {
    renderCol(value, type) {
      if (type && type == 'DateTime') {
        return this.$moment(value).format('MMM DD YYYY HH:mm')
      } else {
        return value
      }
    }
  }
}
</script>

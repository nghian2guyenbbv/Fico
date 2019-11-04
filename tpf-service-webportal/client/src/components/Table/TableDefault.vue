<template>
  <div>
    <el-table
      v-loading="state.table._loading"
      :data="state.table._list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      height="70vh"
    >
      <el-table-column
        type="index"
        :index="renderIndex"
        width="50">
      </el-table-column>
      <el-table-column v-for="(item) in header" :key="item.key" 
        :label="item.title"
        :align="item.align"
        :header_align="item.header_align"
      >
        <template slot-scope="scope">{{ renderCol(scope.row[item.key], item.type) }}</template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="pagination"
      class="pagination-container"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="state.table._page"
      :page-sizes="state.table._pageSize"
      :page-size="state.table._rowsPerPage"
      layout="total, sizes, prev, pager, next, jumper"
      :total="parseInt(state.table._total)"
    ></el-pagination>
  </div>
</template>

<script>
export default {
  name: "tpf-table",

  props: {
    header: {
      type: Array,
      required: true
    },
    project: {
      type: String,
      default: ''
    },
    pagination: {
      type: Boolean,
      default: true
    },
    index: {
      type: Boolean,
      default: false
    },
  },
  data() {
    return {
      data: []
    };
  },
  created() {
    this.getListData()
    this.state.table._project = this.$props.project
  },
  computed: {},
  methods: {
    getListData() {
      if (!this.$props.pagination) {
        this.$store.dispatch("table/getDataTable", { _rowsPerPage: 99999 })
      } else {
        this.$store.dispatch("table/getDataTable")
      }
    },
    
    renderCol(value, type) {
      if (type && type == "DateTime") {
        return this.$moment(value).format("MMM DD YYYY HH:mm")
      } else {
        return value;
      }
    },

    renderIndex(index) {
      return index + 1 + (this.state.table._rowsPerPage * (this.state.table._page - 1))
    },

    handleSizeChange(a) {
      this.$store.dispatch("table/getDataTable", { _rowsPerPage: a })
    },

    handleCurrentChange(a) {
      this.$store.dispatch("table/getDataTable", { _page: a })
    },
  }
};
</script>

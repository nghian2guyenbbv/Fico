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
        v-if="index"
        type="index"
        :index="renderIndex"
        width="50">
      </el-table-column>
      <el-table-column v-for="(item) in header" :key="item.key" 
        :label="item.title"
        :align="item.align"
        :header_align="item.header_align"
      >

        <template slot="header" slot-scope="scope">
          <span>{{ scope.column.label }}</span>
        </template>
        
        <template slot-scope="scope">
          <div v-html="renderCol(scope.row[item.key], item.type)"></div>
        </template>

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
    <div v-else>
      <span>Total: {{state.table._total}}</span>
    </div>
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
    tagColor: {
      type: Object,
      default: {}
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

    renderTagColor(tag) {
      if (this.$props.tagColor) {
        return this.$props.tagColor[tag] ? this.$props.tagColor[tag] : 'unknown'
      } else {
        return 'unknown'
      }
    },
    renderCols(a){
      console.log(a)
    },
    renderCol(value, type) {
      if (type && type == "DateTime") {
        return this.$moment(value).format("MMM DD YYYY HH:mm")
      } else if (type && type == "Label") {
        return `
        <div class="tpf-tag tpf-tag--${this.renderTagColor(value)} tpf-tag--${value}" style="background-color: ${this.renderTagColor(value)}">
          ` + value + `
        </div>
        `
      } else {
        return value
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

<style lang="scss">
$colorStatus: (
  "success": #67C23A,
  "warning": #E6A23C,
  "error": #F56C6C,
  "infor": #409EFF,
  "gray": #606266
);

.tpf-tag {
  height: 24px;
  padding: 0 12px;
  line-height: 24px;
  display: inline-block;
  font-size: 12px;
  border-radius: 4px;
  box-sizing: border-box;
  white-space: nowrap;
  @each $name, $color in $colorStatus {
    &.tpf-tag--#{$name} {
      background-color: $color;
      color: #fff;
    }
  }
}
</style>

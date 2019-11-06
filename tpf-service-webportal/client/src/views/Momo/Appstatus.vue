<template>
  <div>
    <el-card :body-style="{ padding: '5px' }">
      <el-input
        placeholder="Search"
        v-model="valueSearch"
        style="width: 500px"
        @keyup.enter.native="handleSearch"
      >
        <el-select v-model="keySearch" slot="prepend" placeholder="Key" style="width: 150px">
          <el-option label="App ID" value="appId"></el-option>
        </el-select>
        <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
      </el-input>
    </el-card>
    <tpf-table-momo
      v-loading="state.momo.MomoStatus.isLoading"
      :data="state.momo.MomoStatus.list"
      :headers="headers"
      department="MomoStatus"
      :assigned="true"
    ></tpf-table-momo>
    <el-card :body-style="{ padding: '5px' }">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="params.page"
        :page-sizes="[5, 10, 20, 30, 50, 100]"
        :page-size="params.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="parseInt(state.momo.MomoStatus.total)"
      ></el-pagination>
    </el-card>
    <code style="display: none">{{track = state.momo.MomoStatus.total}}</code>
  </div>
</template>

<script>
import TpfTableMomo from "./components/MomoTable";
import axios from "axios";
export default {
  name: "Appstatus",
  components: { TpfTableMomo },
  data() {
    return {
      track: "",
      keySearch: "appId",
      valueSearch: "",
      headers: [],
      params: {
        page: 1,
        limit: 10
      }
    };
  },

  props: {},
  watch: {
    track() {
      this.handleSizeChange(this.params.limit);
      this.handleCurrentChange(this.params.page);
    }
  },
  computed: {},

  created() {
    this.state.momo.MomoStatus._page = this.params.page;
    this.state.momo.MomoStatus._rowsPerPage = this.params.limit;
    this.state.momo.MomoStatus._search = { project: "momo" };
    this.$store.dispatch("momo/fnCallListView", "MomoStatus");
    this.headers = [
      {
        text: "CREATED DATE",
        align: "left",
        value: "createdAt",
        width: "250px"
      },
      { text: "APP ID", align: "left", value: "appId", width: "200px" },
      { text: "FULL NAME", align: "left", value: "fullName", width: "350px" },
      { text: "STATUS", align: "left", value: "status", width: "220px" },
      {
        text: "SMS",
        align: "left",
        value: "optional.smsResult",
        width: "100px"
      },
      { text: "DOCUMENTS", align: "left", value: "documents", width: "120px" }
    ];
  },

  methods: {
    handleSearch() {
      this.state.momo.MomoStatus._search[this.keySearch] = this.valueSearch;
      this.$store.dispatch("momo/fnCallListView", "MomoStatus");
    },
    handleSizeChange(a) {
      this.params.limit = a;
      this.getList();
    },
    handleCurrentChange(a) {
      this.params.page = a;
      this.getList();
    },
    getList() {
      this.listLoading = true;
      this.state.momo.MomoStatus._page = this.params.page;
      this.state.momo.MomoStatus._rowsPerPage = this.params.limit;
      this.state.momo.MomoStatus._search = { project: "momo" };
      this.$store.dispatch("momo/fnCallListView", "MomoStatus");
    }
  }
};
</script>

<style>
.data-table {
  margin-top: 10px;
  margin-left: 0px;
  margin-right: 0px;
}
.boody {
  margin-top: 10px;
  height: 97%;
  margin-bottom: 10px;
  margin-left: 10px;
  margin-right: 10px;
}
.pagination-container {
  background: #fff;
  margin-top: 10px;
}
</style>

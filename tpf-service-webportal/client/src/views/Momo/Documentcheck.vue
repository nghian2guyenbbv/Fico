
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
      v-loading="this.state.momo.MomoDocumentCheckAss.isLoading"
      :data="this.state.momo.MomoDocumentCheckAss.list"
      :headers="headers.assigned"
      department="MomoDocumentCheck"
      :assigned="true"
      title="Assigned"
    ></tpf-table-momo>

    <tpf-table-momo
      v-loading="this.state.momo.MomoDocumentCheckUnAss.isLoading"
      :data="this.state.momo.MomoDocumentCheckUnAss.list"
      :headers="headers.unassigned"
      department="MomoDocumentCheck"
      :assigned="false"
      title="UnAssigned"
    ></tpf-table-momo>
  </div>
</template>

<script>
import TpfTableMomo from "./components/MomoTable";
import axios from "axios";
export default {
  name: "MomoDocumentCheck",
  components: { TpfTableMomo },
  data() {
    return {
      keySearch: "",
      valueSearch: "",
      headers: []
    };
  },

  props: {},

  computed: {},

  created() {
    this.state.momo.MomoDocumentCheckAss = {
      ...this.state.momo.MomoDocumentCheckAss,
      _search: {
        appId: "",
        project: "momo",
        department: "document_check",
        assigned: "user"
      }
    };

    this.state.momo.MomoDocumentCheckUnAss = {
      ...this.state.momo.MomoDocumentCheckUnAss,
      _search: {
        appId: "",
        project: "momo",
        department: "document_check",
        assigned: ""
      }
    };

    this.$store.dispatch("momo/fnCallListView", "MomoDocumentCheckAss");

    this.$store.dispatch("momo/fnCallListView", "MomoDocumentCheckUnAss");

    this.headers = {
      assigned: [
        {
          text: "CREATED DATE",
          align: "left",
          value: "createdAt",
          width: "180px"
        },
        { text: "APP ID", align: "left", value: "appId", width: "200px" },
        {
          text: "FULL NAME",
          align: "left",
          value: "fullName",
          width: "300px"
        },
        { text: "STATUS", align: "left", value: "status", width: "190px" },
        {
          text: "UNASSIGNED",
          align: "center",
          value: "assigned",
          sortable: false,
          width: "200px"
        },
        {
          text: "Attached",
          align: "left",
          value: "documents",
          width: "200px"
        }
      ],
      unassigned: [
        {
          text: "CREATED DATE",
          align: "left",
          value: "createdAt",
          width: "180px"
        },
        { text: "APP ID", align: "left", value: "appId", width: "200px" },
        {
          text: "FULL NAME",
          align: "left",
          value: "fullName",
          width: "300px"
        },
        { text: "STATUS", align: "left", value: "status", width: "190px" },
        {
          text: "ASSIGN TO ME",
          align: "center",
          value: "assigned",
          sortable: false,
          width: "200px"
        },
        {
          text: "Attached",
          align: "left",
          value: "documents",
          width: "200px"
        }
      ]
    };
  },

  methods: {
    handleSearch() {
      this.state.momo.MomoDocumentCheckAss._search[
        this.keySearch
      ] = this.valueSearch;
      this.$store.dispatch("momo/fnCallListView", "MomoDocumentCheckAss");
      this.state.momo.MomoDocumentCheckUnAss._search[
        this.keySearch
      ] = this.valueSearch;
      this.$store.dispatch("momo/fnCallListView", "MomoDocumentCheckUnAss");
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
</style>

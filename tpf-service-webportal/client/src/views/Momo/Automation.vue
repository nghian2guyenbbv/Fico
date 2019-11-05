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
      v-loading="this.state.momo.MomoDataentyAss.isLoading"
      :data="this.state.momo.MomoDataentyAss.list"
      :headers="headers.assigned"
      department="MomoDataenty"
      :assigned="true"
      title="Assigned"
    ></tpf-table-momo>
    <el-card :body-style="{ padding: '5px' }">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="params.page"
        :page-sizes="[5, 10, 20, 30, 50, 100]"
        :page-size="params.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="parseInt(this.state.momo.MomoDataentyAss.total)"
      ></el-pagination>
    </el-card>
    <tpf-table-momo
      v-loading="this.state.momo.MomoDataentyUnAss.isLoading"
      :data="this.state.momo.MomoDataentyUnAss.list"
      :headers="headers.unassigned"
      department="MomoDataenty"
      :assigned="false"
      title="UnAssigned"
    ></tpf-table-momo>
    <el-card :body-style="{ padding: '5px' }">
      <el-pagination
        @size-change="handleSizeChangeUnAss"
        @current-change="handleCurrentChangeUnAss"
        :current-page.sync="paramsUnAss.page"
        :page-sizes="[ 10, 20, 30, 50, 100]"
        :page-size="paramsUnAss.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="parseInt(this.state.momo.MomoDataentyUnAss.total)"
      ></el-pagination>
    </el-card>
  </div>
</template>

<script>
import TpfTableMomo from "./components/MomoTable";
import axios from "axios";
export default {
  name: "Automation",
  components: { TpfTableMomo },
  data() {
    return {
      keySearch: "",
      valueSearch: "",
      loadingNow: true,
      params: {
        page: 1,
        limit: 5
      },
      paramsUnAss: {
        page: 1,
        limit: 10
      },
      dataAss: [
        {
          createdAt: "2019-09-27T11:29:29.431+0000",
          appId: "99",
          partnerId: "12321312",
          fullName: "Nguyen Van 99",
          automationResult: "PROCESSING_FAIL",
          id: "1212",
          assigned: true,
          uuid: "momo_5d91a4c49c855835704e2b2f"
        },
        {
          createdAt: "2018-09-27T11:29:29.431+0000",
          appId: "19",
          partnerId: "02321312",
          fullName: "Nguyen Van 100",
          automationResult: "PROCESSING_PASS",
          id: "232",
          assigned: true
        }
      ],
      dataUnAss: [
        {
          createdAt: "2019-09-27T11:29:29.431+0000",
          appId: "99",
          partnerId: "12321312",
          fullName: "Nguyen Van 991",
          automationResult: "PROCESSING_FAIL",
          id: "1212",
          assigned: null,
          uuid: "momo_5d91a4c49c855835704e2b2f"
        },
        {
          createdAt: "2018-09-27T11:29:29.431+0000",
          appId: "19",
          partnerId: "02321312",
          fullName: "Nguyen Van 10022",
          automationResult: "PROCESSING_PASS",
          id: "232",
          assigned: null
        }
      ]
    };
  },

  props: {},
  methods: {
    handleSearch() {
      this.state.momo.MomoDataentyAss._search[
        this.keySearch
      ] = this.valueSearch;
      this.$store.dispatch("momo/fnCallListView", "MomoDataentyAss");
      this.state.momo.MomoDataentyUnAss._search[
        this.keySearch
      ] = this.valueSearch;
      this.$store.dispatch("momo/fnCallListView", "MomoDataentyUnAss");
    },
    handleSizeChange(a) {
      this.params.limit = a;
      this.getList();
    },
    handleCurrentChange(a) {
      this.params.page = a;
      this.getList();
    },

    handleSizeChangeUnAss(a) {
      this.paramsUnAss.limit = a;
      this.getList("UnAss");
    },
    handleCurrentChangeUnAss(a) {
      this.paramsUnAss.page = a;
      this.getList("UnAss");
    },
    getList(v) {
      if (v != "UnAss") {
        this.state.momo.MomoDataentyAss = {
          ...this.state.momo.MomoDataentyAss,
          _page: this.params.page,
          _rowsPerPage: this.params.limit,
          _search: {
            project: "momo",
            department: "data_entry",
            assigned: this.fnCookie().getInforUser().user_name
          }
        };
        this.$store.dispatch("momo/fnCallListView", "MomoDataentyAss");
      } else {
        this.state.momo.MomoDataentyUnAss = {
          ...this.state.momo.MomoDataentyUnAss,
          _page: this.paramsUnAss.page,
          _rowsPerPage: this.paramsUnAss.limit,
          _search: {
            project: "momo",
            department: "data_entry",
            assigned: ""
          }
        };
        this.$store.dispatch("momo/fnCallListView", "MomoDataentyUnAss");
      }
    }
  },

  computed: {},

  created() {
    this.headers = {
      assigned: [
        {
          text: "CREATED DATE",
          align: "left",
          value: "createdAt",
          sortable: true,
          width: "180px"
        },
        {
          text: "APP ID",
          align: "left",
          value: "appId",
          sortable: true,
          width: "180px"
        },
        {
          text: "CUSTOMER ID",
          align: "left",
          value: "partnerId",
          sortable: true,
          width: "180px"
        },
        { text: "FULL NAME", align: "left", value: "fullName", width: "300px" },
        {
          text: "STATUS",
          align: "left",
          value: "automationResult",
          sortable: true,
          width: "150px"
        },
        {
          text: "Application",
          align: "center",
          value: "id",
          sortable: false,
          width: "150px"
        },
        {
          text: "UNASSIGNED",
          align: "center",
          value: "assigned",
          sortable: false,
          width: "120px"
        },
        {
          text: "FIX MANUALY",
          align: "center",
          value: "fixmanualy",
          sortable: false,
          width: "120px"
        },
        // {
        //   text: "RETRY",
        //   align: "center",
        //   value: "retry",
        //   sortable: false,
        //   width: "120px"
        // }
      ],
      unassigned: [
        {
          text: "CREATED DATE",
          align: "left",
          value: "createdAt",
          sortable: true,
          width: "180px"
        },
        {
          text: "APP ID",
          align: "left",
          value: "appId",
          sortable: true,
          width: "180px"
        },
        {
          text: "CUSTOMER ID",
          align: "left",
          value: "partnerId",
          sortable: true,
          width: "180px"
        },
        { text: "FULL NAME", align: "left", value: "fullName", width: "300px" },
        {
          text: "STATUS",
          align: "left",
          value: "automationResult",
          sortable: true,
          width: "150px"
        },
        {
          text: "Application",
          align: "center",
          value: "id",
          sortable: false,
          width: "150px"
        },
        {
          text: "ASSIGNED",
          align: "center",
          value: "assigned",
          sortable: false,
          width: "120px"
        }
      ]
    };

    this.state.momo.MomoDataentyAss = {
      ...this.state.momo.MomoDataentyAss,
      _page: this.params.page,
      _rowsPerPage: this.params.limit,
      _search: {
        project: "momo",
        department: "data_entry",
        assigned: this.fnCookie().getInforUser().user_name
      }
    };

    this.state.momo.MomoDataentyUnAss = {
      ...this.state.momo.MomoDataentyUnAss,
      _page: this.paramsUnAss.page,
      _rowsPerPage: this.paramsUnAss.limit,
      _search: {
        project: "momo",
        department: "data_entry",
        assigned: ""
      }
    };

    this.$store.dispatch("momo/fnCallListView", "MomoDataentyAss");

    this.$store.dispatch("momo/fnCallListView", "MomoDataentyUnAss");
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
  margin-left: 10px;
  margin-right: 10px;
}
.pagination-container {
  background: #fff;
  margin-top: 10px;
}
</style>

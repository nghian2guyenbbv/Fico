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
      v-loading="state.momo.MomoLoanBookingAss.isLoading"
      :data="state.momo.MomoLoanBookingAss.list"
      :headers="headers.assigned"
      department="MomoLoanBooking"
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
        :total="parseInt(state.momo.MomoLoanBookingAss.total)"
      ></el-pagination>
    </el-card>
    <tpf-table-momo
      v-loading="state.momo.MomoLoanBookingUnAss.isLoading"
      :data="state.momo.MomoLoanBookingUnAss.list"
      :headers="headers.unAssigned"
      department="MomoLoanBooking"
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
        :total="parseInt(state.momo.MomoLoanBookingUnAss.total)"
      ></el-pagination>
    </el-card>
  </div>
</template>

<script>
import TpfTableMomo from "./components/MomoTable";
import axios from "axios";
export default {
  name: "MomoLoanBooking",
  components: { TpfTableMomo },
  data() {
    return {
      params: {
        page: 1,
        limit: 5
      },
      paramsUnAss: {
        page: 1,
        limit: 10
      },
      keySearch: "appId",
      valueSearch: "",
      headers: []
    };
  },

  props: {},
  computed: {},

  created() {
    this.state.momo.MomoLoanBookingAss = {
      ...this.state.momo.MomoLoanBookingAss,
      _page: this.params.page,
      rowsPerPage: this.params.limit,
      _search: {
        appId: "",
        project: "momo",
        department: "loan_booking",
        assigned: this.fnCookie().getInforUser().user_name
      }
    };

    this.state.momo.MomoLoanBookingUnAss = {
      ...this.state.momo.MomoLoanBookingUnAss,
      _page: this.paramsUnAss.page,
      rowsPerPage: this.paramsUnAss.limit,
      _search: {
        appId: "",
        project: "momo",
        department: "loan_booking",
        assigned: ""
      }
    };

    this.$store.dispatch("momo/fnCallListView", "MomoLoanBookingAss");

    this.$store.dispatch("momo/fnCallListView", "MomoLoanBookingUnAss");

    this.headers = {
      assigned: [
        {
          text: "CREATED DATE",
          align: "left",
          value: "createdAt",
          width: "180px"
        },
        { text: "APP ID", align: "left", value: "appId", width: "150px" },
        { text: "FULL NAME", align: "left", value: "fullName" },
        { text: "STATUS", align: "left", value: "status", width: "200px" },
        {
          text: "SMS",
          align: "left",
          value: "optional.smsResult",
          width: "100px"
        },
        {
          text: "ASSIGNED",
          align: "center",
          value: "assigned",
          sortable: false,
          width: "200px"
        }
      ],
      unAssigned: [
        {
          text: "CREATED DATE",
          align: "left",
          value: "createdAt",
          width: "180px"
        },
        { text: "APP ID", align: "left", value: "appId", width: "150px" },
        { text: "FULL NAME", align: "left", value: "fullName" },
        { text: "STATUS", align: "left", value: "status", width: "200px" },
        {
          text: "SMS",
          align: "left",
          value: "optional.smsResult",
          width: "100px"
        },
        {
          text: "UNASSIGNED",
          align: "center",
          value: "assigned",
          sortable: false,
          width: "200px"
        }
      ]
    };
  },

  methods: {
    handleSearch() {
      this.state.momo.MomoLoanBookingAss._search[
        this.keySearch
      ] = this.valueSearch;
      this.$store.dispatch("momo/fnCallListView", "MomoLoanBookingAss");
      this.state.momo.MomoLoanBookingUnAss._search[
        this.keySearch
      ] = this.valueSearch;
      this.$store.dispatch("momo/fnCallListView", "MomoLoanBookingUnAss");
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
        this.state.momo.MomoLoanBookingAss = {
          ...this.state.momo.MomoLoanBookingAss,
          _page: this.params.page,
          rowsPerPage: this.params.limit,

          _search: {
            project: "momo",
            department: "loan_booking",
            assigned: this.fnCookie().getInforUser().user_name
          }
        };
        this.$store.dispatch("momo/fnCallListView", "MomoLoanBookingAss");
      } else {
        this.state.momo.MomoLoanBookingUnAss = {
          ...this.state.momo.MomoLoanBookingUnAss,
          _page: this.paramsUnAss.page,
          rowsPerPage: this.paramsUnAss.limit,
          _search: {
            project: "momo",
            department: "loan_booking",
            assigned: ""
          }
        };
        this.$store.dispatch("momo/fnCallListView", "MomoLoanBookingUnAss");
      }
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
</style>

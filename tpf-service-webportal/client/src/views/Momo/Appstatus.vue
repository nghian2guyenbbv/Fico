<template>
  <div>
    <tpf-table-momo
      v-loading="this.state.momo.MomoStatus.isLoading"
      :data="this.state.momo.MomoStatus.list"
      :headers="headers"
      department="MomoStatus"
      :assigned="true"
    ></tpf-table-momo>
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
      headers: []
    };
  },

  props: {},

  computed: {},

  created() {
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
      { text: "Action", align: "left", value: "documents", width: "120px" }
    ];
  },

  methods: {
    searchAppID() {
      this.product_state.MomoStatus._search = {
        ...this.product_state.MomoStatus._search,
        appId: this.search
      };
      this.$store.dispatch("app_state/fnCallListView", "MomoStatus");
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


<template>
  <div>
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
      headers: [],
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
    searchAppID() {
      this.product_state.MomoDocumentCheckAss._search = {
        ...this.product_state.MomoDocumentCheckAss._search,
        appId: this.search
      };
      this.$store.dispatch("app_state/fnCallListView", "MomoDocumentCheckAss");
      this.product_state.MomoDocumentCheckUnAss._search = {
        ...this.product_state.MomoDocumentCheckUnAss._search,
        appId: this.search
      };
      this.$store.dispatch(
        "app_state/fnCallListView",
        "MomoDocumentCheckUnAss"
      );
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

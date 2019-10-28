<template>
  <div>
    <tpf-table-momo
      v-loading="this.state.momo.MomoLoanBookingAss.isLoading"
      :data="this.state.momo.MomoLoanBookingAss.list"
      :headers="headers.assigned"
      department="MomoLoanBooking"
      :assigned="true"
      title="Assigned"
    ></tpf-table-momo>

    <tpf-table-momo
      v-loading="this.state.momo.MomoLoanBookingUnAss.isLoading"
      :data="this.state.momo.MomoLoanBookingUnAss.list"
      :headers="headers.unAssigned"
      department="MomoLoanBooking"
      :assigned="false"
      title="UnAssigned"
    ></tpf-table-momo>
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
      headers: [],
    };
  },

  props: {},

  computed: {},

  created() {
        this.state.momo.MomoLoanBookingAss = {
      ...this.state.momo.MomoLoanBookingAss,
      _search: {
        appId: "",
        project: "momo",
        department: "document_check",
        assigned: "user"
      }
    };

    this.state.momo.MomoLoanBookingUnAss = {
      ...this.state.momo.MomoLoanBookingUnAss,
      _search: {
        appId: "",
        project: "momo",
        department: "document_check",
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
    searchAppID() {
      this.product_state.MomoLoanBookingAss._search = {
        ...this.product_state.MomoLoanBookingAss._search,
        appId: this.search
      };
      this.$store.dispatch("app_state/fnCallListView", "MomoLoanBookingAss");
      this.product_state.MomoLoanBookingUnAss._search = {
        ...this.product_state.MomoLoanBookingUnAss._search,
        appId: this.search
      };
      this.$store.dispatch("app_state/fnCallListView", "MomoLoanBookingUnAss");
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

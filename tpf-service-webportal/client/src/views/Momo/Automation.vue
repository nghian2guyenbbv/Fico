<template>
  <div>
    <tpf-table-momo
      v-loading="this.state.momo.MomoDataentyAss.isLoading"
      :data="this.state.momo.MomoDataentyAss.list"
      :headers="headers.assigned"
      department="MomoDataenty"
      :assigned="true"
      title="Assigned"
    ></tpf-table-momo>

    <tpf-table-momo
      v-loading="this.state.momo.MomoDataentyUnAss.isLoading"
      :data="this.state.momo.MomoDataentyUnAss.list"
      :headers="headers.unassigned"
      department="MomoDataenty"
      :assigned="false"
      title="UnAssigned"
    ></tpf-table-momo>
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
      search: "",
      model: "App",
      loadingNow: true,
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
    searchAppID() {
      // this.product_state.MomoDataentyAss._search = {
      //   ...this.product_state.MomoDataentyAss._search,
      //   appId: this.search
      // };
      // this.$store.dispatch("app_state/fnCallListView", "MomoDataentyAss");
      // this.product_state.MomoDataentyUnAss._search = {
      //   ...this.product_state.MomoDataentyUnAss._search,
      //   appId: this.search
      // };
      // this.$store.dispatch("app_state/fnCallListView", "MomoDataentyUnAss");
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
        }
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
      _search: {
        appId: "",
        project: "momo",
        department: "data_entry",
        assigned: "user"
      }
    };

    this.state.momo.MomoDataentyUnAss = {
      ...this.state.momo.MomoDataentyUnAss,
      _search: {
        appId: "",
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
</style>

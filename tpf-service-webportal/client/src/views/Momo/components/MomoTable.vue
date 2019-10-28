<template>
  <el-card class="box-card" :header="title" :body-style="{ padding: '5px' }">
    <el-table :data="data" border style="width: 100%">
      <el-table-column v-for="i in headers" :key="i.key" :label="i.text" v-slot="item">
        <template v-if="i.value == 'createdAt'">{{ item.row.createdAt|moment("Y-MM-DD h:mm a") }}</template>
        <template v-else-if="i.value == 'id'">
          <el-button type="success" plain @click="fnAppData(item.row.id)">App Data</el-button>
        </template>
        <template v-else-if="i.value == 'documents'">
          <span>{{item.row.documents.length}}</span>
        </template>

        <template v-else-if="i.value == 'optional.smsResult'">
          <font
            :color=" getColorSms(item.row.optional.smsResult).color"
          >{{ item.row.optional.smsResult }}</font>
        </template>

        <template v-else-if="i.value == 'status'">
          <div>
            <el-tag :color="getTypeStatus(item.status).bgcolor">
              <font
                :color="getTypeStatus(item.row.status).bgcolor"
              >{{getNameStatus(item.row.status)}}</font>
            </el-tag>
          </div>
        </template>
        <template v-else-if="i.value == 'assigned'">
          <el-button
            type="success"
            plain
            v-if="item.row.assigned"
            @click="fnUnassign(item.row)"
          >Unassigned</el-button>
          <el-button
            :disabled="([department + 'UnAss']).isLoading"
            type="success"
            plain
            @click="fnAssign(item.row)"
            v-else
          >Assigned</el-button>
        </template>
        <template v-else>{{ item.row[i.value]}}</template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
export default {
  name: "tpf-table-momo",

  data() {
    return {
      imagesShow: undefined,
      imagesDow: undefined,
      colorSmS: {
        W: {
          color: "#ffbf00"
        },
        Y: {
          color: "#42f57b"
        },
        N: {
          color: "#00b8ff"
        },
        F: {
          color: "#E57373"
        }
      },
      typeStatus: {
        PROCESSING_PASS: {
          img: "fa-spinner",
          color: "#7e7a7a",
          bgcolor: "rgba(184, 191, 20, 0.5)"
        },
        PROCESSING_FAIL: {
          img: "fa-spinner",
          color: "#7e7a7a",
          bgcolor: "rgba(184, 191, 20, 0.5)"
        },
        APPROVED: {
          img: "fa-thumbs-up",
          color: "#5c3382",
          bgcolor: "rgba(92, 51, 130, 0.20)"
        },
        DISBURSED: {
          img: "account_balance_wallet",
          color: "#81C784",
          bgcolor: "rgba(129, 199, 132, 0.1)"
        },
        CANCELLED: {
          img: "fa-ban",
          color: "#424242",
          bgcolor: "rgba(66, 66, 66, .2)"
        },
        REJECTED: {
          img: "fa-thumbs-down",
          color: "#E57373",
          bgcolor: "rgba(229, 115, 115, .2)"
        },
        RETURNED: {
          img: "pause_circle_filled",
          color: "#2E7D32",
          bgcolor: "rgba(46, 125, 50, 0.2)"
        },
        SUPPLEMENT: {
          img: "fa-plus-square",
          color: "#26C6DA",
          bgcolor: "rgba(38, 198, 218, .1)"
        },
        OTHER: {
          img: "fa-recycle",
          color: "#26C6DA",
          bgcolor: "rgba(38, 198, 218, .1)"
        }
      },
      clickPhoto: false,
      dow: false
    };
  },

  props: {
    data: { type: Array },
    title: "",
    headers: { type: Array },
    search: { type: String },
    height: { type: String },
    sort: { type: Object },
    loading: { type: Boolean },
    assigned: { type: Boolean },
    department: { type: String }
  },

  computed: {},

  methods: {
    getTypeStatus(item) {
      if (this.typeStatus[item]) {
        return this.typeStatus[item];
      } else {
        return {
          img: "fa-recycle",
          color: "#330000",
          bgcolor: "rgba(224, 224, 224)"
        };
      }
    },

    getNameStatus(item) {
      if (this.typeStatus[item]) {
        if (item.includes("PROCESSING")) {
          return "PROCESSING";
        }
        return item;
      } else {
        return item ? item.replace("_", " ") :  ''
      }
    },
    getColorSms(item) {
      if (this.colorSmS[item]) {
        return this.colorSmS[item];
      } else {
        return {
          color: "#000"
        };
      }
    },
    overlayOpen(images) {
      this.imagesShow = null;
      this.imagesShow = images;
      this.app_state.overlay = true;
    },
    fnAssign(app) {
      this.state.momo[this.department + "Ass"].obj.assigned = "user";
      this.state.momo[this.department + "Ass"].obj.project = app.project;
      this.state.momo[this.department + "Ass"].obj.id = app.uuid.split("_")[1];
      this.$store.dispatch("momo/fnUpdata", this.department + "Ass");
    },
    fnUnassign(app) {
      this.state.momo[this.department + "UnAss"].obj.unassigned = app.assigned;
      this.state.momo[this.department + "UnAss"].obj.project = app.project;
      this.state.momo[this.department + "UnAss"].obj.id = app.uuid.split(
        "_"
      )[1];
      this.$store.dispatch("momo/fnUpdata", this.department + "UnAss");
    },

    fnDowloadAll(items) {
      this.$store.dispatch("momo/fnDownloadDocument", items);
    },
    fnAppData(id) {
      window.open("/#/momo/appdatamomo/" + id, "_blank");
      this.state.momo.ACCA.obj.references = [];
      this.state.momo.ACCA._id = id;
      this.$store.dispatch("momo/fnACCA", "ACCA");
    }
  }
};
</script>



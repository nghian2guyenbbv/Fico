<template>
  <el-card class="box-card" :header="title" :body-style="{ padding: '5px'}" style="color: #42b983">
    <el-table :data="data" border style="width: 100%">
      <el-table-column v-for="i in headers" :key="i.key" :label="i.text" v-slot="item">
        <template v-if="i.value == 'createdAt'">{{ item.row.createdAt|moment("Y-MM-DD h:mm a") }}</template>
        <template v-else-if="i.value == 'id'">
          <el-button type="success" plain @click="fnAppData(item.row.id)">App Data</el-button>
        </template>
        <template v-else-if="i.value == 'documents'">
          <el-tooltip class="item" effect="light" content="Dowload All" placement="left">
            <el-button
              :disabled="(item.row.documents.length == 0) || (state.momo['Documents'].disabledDown)"
              @click="fnDowloadAll(item.row.documents)"
            >
              <i :class="!state.momo['Documents'].disabledDown?'el-icon-download':'el-icon-loading'"></i> 
            </el-button>
          </el-tooltip>
          <el-badge type="warning" :value="item.row.documents.length" :max="99" class="item">
            <el-tooltip class="item" effect="light" content="View All" placement="right">
              <el-button
                :disabled="item.row.documents.length == 0 "
                @click="funcShowDialog(item.row.documents)"
              >
                <i class="el-icon-document-copy"></i>
              </el-button>
            </el-tooltip>
          </el-badge>
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
          :disabled="([department + 'UnAss']).isLoading"
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
import axios from "axios";
export default {
  name: "tpf-table-momo",
  data() {
    return {
      disabled: false,
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
    funcShowDialog(images) {
      this.state.momo["Documents"].items = [];
      this.state.momo["Documents"].show = false;
      this.state.momo["Documents"].items = images;
      this.state.momo["Documents"].show = true;
    },

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
      if (item == null) return 'Null' 
      if (this.typeStatus[item]) {
        if (item.includes("PROCESSING")) {
          return "PROCESSING";
        }
        return item;
      } else {
        return item ? item.replace("_", " ") : "";
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

    fnAssign(app) {
      this.disabled = true
      this.state.momo[this.department + "Ass"].obj.assigned = this.fnCookie().getInforUser().user_name;
      this.state.momo[this.department + "Ass"].obj.project = app.project;
      this.state.momo[this.department + "Ass"].obj.id = app.uuid.split("_")[1];
      this.$store.dispatch("momo/fnUpdata", this.department + "Ass").then(e =>{
        this.disabled = false
      });
    },
    fnUnassign(app) {
      this.disabled = true
      this.state.momo[this.department + "Ass"].obj.assigned = undefined;
      this.state.momo[this.department + "UnAss"].obj.unassigned = app.assigned;
      this.state.momo[this.department + "UnAss"].obj.project = app.project;
      this.state.momo[this.department + "UnAss"].obj.id = app.uuid.split(
        "_"
      )[1];
      this.$store.dispatch("momo/fnUpdata", this.department + "UnAss").then(e =>{
        this.disabled = false
      });
    },

    fnDowloadAll(items) {
      for (const key in items) {
        this.state.momo["Documents"].disabledDown = true
        if (items.hasOwnProperty(key)) {
          const element = items[key];
          axios({
            url: element.downloadUrl,
            method: "GET",
            responseType: "blob"
          })
            .then(response => {
              var fileURL = window.URL.createObjectURL(
                new Blob([response.data])
              );
              var fileLink = document.createElement("a");
              fileLink.href = fileURL;
              fileLink.setAttribute("download", element.documentType + ".jpg");
              document.body.appendChild(fileLink);
              fileLink.click();
              this.state.momo["Documents"].disabledDown = false
              this.$notify.success({
                title: "Success",
                message: "Dowload " + element.documentType,
                offset: 100
              });
            })
            .catch(error => {
              this.state.momo["Documents"].disabledDown = false
              this.$notify.error({
                title: "Error Dowload " + element.documentType,
                message: error,
                offset: 100
              });
            });
        }
      }
    },
    fnAppData(id) {
      window.open("/#/momo/appdatamomo/" + id, "_blank");
    }
  }
};
</script>

<style>
.el-badge__content {
  margin-top: 7px;
  margin-right: 1px;
  font-size: 9px;
}
.el-button--small {
  margin-right: 5px;
  padding: 5px;
}
.el-tooltip__popper {
    padding-top: 5px;
    padding-bottom: 5px;
    color: #42b983;
    border: 1px solid #6b8bcc !important;
}
.el-tooltip__popper.popper__arrow {
    border-right-color: #6b8bcc !important;
}

</style>


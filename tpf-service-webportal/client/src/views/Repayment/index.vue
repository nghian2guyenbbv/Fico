<template>
  <div>
    <div class="app-container">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-button
            class="filter-item"
            style="margin-left: 10px; float: right;"
            type="primary"
            icon="el-icon-upload2"
            :loading="importLoading"
            @click="importExcel"
          >Import Excel</el-button>

          <el-button
            class="filter-item"
            style="margin-left: 10px; float: right;"
            type="primary"
            icon="el-icon-refresh-left"
            :loading="settelLoading"
            @click="settelTrans"
          >Settel Trans</el-button>
          <el-dropdown
            v-if="false"
            split-button
            type="primary"
            trigger="click"
            @command="fnHandlingSettelTrans"
            style="margin-left: 10px; float: right; !important"
          >
            Trans Date List
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                v-for="(item, index) in transDateList"
                v-bind:key="index+item"
                :command="item.date"
              >{{item.date}}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
      
          <el-button
            class="filter-item"
            style="margin-left: 10px; float: right;"
            type="primary"
            icon="el-icon-upload2"
            :loading="pushLoading"
            @click="fnPushData"
            v-if="statusData == 'import'"
          >Push Data</el-button>
          <el-date-picker
            v-model="value"
            :clearable="false"
            type="daterange"
            start-placeholder="Start date"
            end-placeholder="End date"
            :default-time="['00:00:00', '23:59:59']"
            :picker-options="pickerOptions"
          ></el-date-picker>
          <input
            type="file"
            id="file"
            ref="myFiles"
            class="custom-file-input"
            @change="fnChange"
            accept=".xlsx, .xls, .csv"
            style="display: none"
          />
        </el-col>
      </el-row>
       
    </div>
    <div style="margin-bottom: 20px; margin-left: 20px">
      <el-tag type="success" v-show="statusData == 'import'">Data: Excel, {{ infoExcel }}</el-tag>
      <el-tag
        type="success"
        v-show="statusData == 'server' && dataexcel.length > 0 "
      >Data: Server, Total: {{ dataexcel.length}}, From {{value[0] | moment("DD/MM/YYYY")}} to {{value[1] | moment("DD/MM/YYYY ")}}</el-tag>
    </div>

    <el-table
      v-loading="listLoading"
      :data="tempt"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      height="70vh"
      v-show="dataexcel.length>0 | show "
      v-if="isheadersImport && role.includes('admin')"
    >
      <el-table-column label="#" prop="number" width="50px" align="left">
        <template slot-scope="scope">
          <span>{{ parseInt(scope.row['number']) + 1 }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Create Date" prop="createDate" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row['createDate'] | moment("MMM DD YYYY HH:mm") }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Vendor Code" prop="vendorCode" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row['vendorCode'] | moment("MMM DD YYYY HH:mm") }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Order Code" prop="orderCode" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row['orderCode'] }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Client Code" prop="clientCode" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row['clientCode'] }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Amount" prop="amount" align="left">
        <template slot-scope="scope">
          <span>{{ currencyFormat(scope.row['amount']) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Full Name" prop="fullName" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row['fullName'] }}</span>
        </template>
      </el-table-column>
    </el-table>

    <el-table
      v-loading="listLoading"
      :data="data"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      height="70vh"
      v-show="dataexcel.length>0 | show "
      v-if="!isheadersImport"
    >
      <el-table-column label="#" prop="number" width="50px" align="left">
        <template slot-scope="scope">
          <span>{{ parseInt(scope.row['number']) + 1 }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Create Date" prop="create_date" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row['create_date'] | moment("MMM DD YYYY HH:mm") }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Amount" prop="amount" align="left">
        <template slot-scope="scope">
          <span>{{ currencyFormat(scope.row['amount']) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Transaction ID" prop="transaction_id" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row['transaction_id'] }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Loan ID" prop="loan_id" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row['loan_id'] }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Loan Account No" prop="loan_account_no" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row['loan_account_no'] }}</span>
        </template>
      </el-table-column>

      <el-table-column label="Identification Number" prop="identification_number" align="left">
        <template slot-scope="scope">
          <span>{{ scope.row['identification_number'] }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="dataexcel.length>0"
      :total="dataexcel.length"
      :page.sync="pagina.page"
      :limit.sync="pagina.limit"
      @pagination="getList"
      v-if="statusData=='import'"
    />
    <pagination
      v-show="dataexcel.length>0"
      :total="dataexcel.length"
      :page.sync="pagina.page"
      :limit.sync="pagina.limit"
      @pagination="getList"
      v-else
    />
  </div>
</template>

<script>
import XLSX from "xlsx";
import axios from "axios";
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API
  // timeout: 5000
});
import { MessageBox, Message } from "element-ui";
import Pagination from "@/components/Pagination";
import * as cookie from '@/utils/cookie'
const roles = cookie.getRoles()
const uuidv1 = require('uuid/v1');
export default {
  name: "Repayment",
  components: { Pagination },
  data() {
    return {
      role: '',
      pushLoading: false,
      show: true,
      dateRange: "",
      value: [],
      pickerOptions: {
        onPick: ({ maxDate, minDate }) => {
          if (
            this.$moment(maxDate).format("YYYY-MM-DD") ==
              this.$moment().format("YYYY-MM-DD") &&
            this.$moment(minDate).format("YYYY-MM-DD") ==
              this.$moment().format("YYYY-MM-DD")
          ) {
            this.off = true;
          }
          maxDate
            ? (this.value = [minDate, maxDate])
            : (this.value = [minDate, minDate]);
        },
        disabledDate: time => {
          let timeRange = 7 * 24 * 60 * 60 * 1000; // 7day
          const maxDate = this.$moment()
            .date(Number)
            ._d.getTime();
          const minDate = maxDate - timeRange;
          return time.getTime() < minDate || time.getTime() > maxDate;
        }
      },
      transDateList: [],
      infoExcel: "",
      off: false,
      files: null,
      listLoading: false,
      importLoading: false,
      settelLoading: false,
      settel: false,
      dataJson: null,
      fromDate: null,
      toDate: null,
      isheadersImport: false,
      data: [],
      dataexcel: [],
      statusData: "",
      tempt: [],
      pagina: {
        page: 1,
        limit: 20
      }
    };
  },

  created() {
    this.role = cookie.getRoles()
    this.setDateFirst();
  },

  watch: {
    value: function(val) {
      var moment = require("moment");
      let fromDate = moment(this.value[0]).format("YYYY-MM-DD");
      let toDate = moment(this.value[1]).format("YYYY-MM-DD");
      this.loadData([fromDate, toDate]);
    }
  },

  methods: {
    setDateFirst() {
      var moment = require("moment");
      let fromDate = this.$moment(this.$moment().format("YYYY-MM-DD"))
        .subtract(6, "days")
        .format("YYYY-MM-DD");
      let toDate = this.$moment().format("YYYY-MM-DD");
      this.value = [fromDate, toDate];
      this.loadData(this.value);
    },

    loadData(value) {
      this.isheadersImport = false;
      var moment = require("moment");
      var dateTime = moment().format("YYYY-MM-DDTHH:mm:ss.SSS");
      this.listLoading = true;
      const inputData = {
        request_id: uuidv1(),
        date_time: this.$moment().format(),
        data: {
          fromDate: value[0] + "T00:00:00.000",
          toDate: value[1] + "T23:59:59.999"
        }
      };

      service
        .post("/repayment/getListTrans", inputData, {
          headers: { Authorization: "Bearer " + this.state.user.token }
        })
        .then(success => {
          this.isheadersImport = false;
          this.statusData = "server";
          this.listLoading = false;
          this.dataexcel =
            success.data && success.data.data ? success.data.data : [];
          this.pagina.page = 1;
          let from = this.pagina.limit * (this.pagina.page - 1);
          this.data = this.fnAddSttInData(this.dataexcel).slice(
            from,
            from + this.pagina.limit
          );
          this.show = this.data.length > 0;
          if (success.data.result_code != 0) {
            Message({
              message: success.data.message,
              type: "error",
              duration: 5 * 1000
            });
          }
        })
        .catch(error => {
          this.show = false;
          this.listLoading = false;
          Message({
            message: error,
            type: "error",
            duration: 5 * 1000
          });
        });
    },

    settelTrans() {
      /// getTransDate
      this.listLoading = true;
      this.settelLoading = true;

      var moment = require("moment");
      var dateTime = moment().format("YYYY-MM-DDTHH:mm:ss.SSS");

      const inputData = {
        request_id: uuidv1(),
        date_time: this.$moment().format(),
        data: {}
      };
      service
        .post("/repayment/getTransDate", inputData, {
          headers: { Authorization: "Bearer " + this.state.user.token }
        })
        .then(success => {
          this.transDateList = success.data.data;
          if (this.transDateList.length > 0) {
            this.listLoading = true;
            this.settelLoading = true;
          } else {
            this.settelLoading = false;
            this.listLoading = false;
          }

          if (success.data.result_code != 0) {
            this.settelLoading = false;
            this.listLoading = false;
            Message({
              message: success.data.message,
              type: "error",
              duration: 5 * 1000
            });
          } else {
            if (this.transDateList.length > 0) {
              this.listLoading = true;
              this.settelLoading = true;
              Message({
                message: "GetTransDate Success",
                type: "success",
                duration: 5 * 1000
              });
              this.fnHandlingSettelTrans(this.transDateList[0].date)
            } else {
              this.settelLoading = false;
              this.listLoading = false;
              Message({
                message: "TransDate nil",
                type: "warning",
                duration: 5 * 1000
              });
            }
          }
        })
        .catch(error => {
          this.settelLoading = false;
          this.listLoading = false;
          Message({
            message: error,
            type: "error",
            duration: 5 * 1000
          });
        });
    },

    fnHandlingSettelTrans(date) {
      var moment = require("moment");
      var dateTime = moment().format("YYYY-MM-DDTHH:mm:ss.SSS");
      const inputData = {
        request_id: uuidv1(),
        date_time: this.$moment().format(),
        data: {
          transDate: date
        }
      };

      service
        .post("/repayment/settleTrans", inputData, {
          headers: { Authorization: "Bearer " + this.state.user.token }
        })
        .then(success => {
          this.listLoading = false;
          this.settelLoading = false;
          this.settel = false;
          this.data = success.data.data;
          this.transDateList = [];
          var moment = require("moment");
          let fromDate = moment(this.value[0]).format("YYYY-MM-DD");
          let toDate = moment(this.value[1]).format("YYYY-MM-DD");
          this.loadData([fromDate, toDate]);
          if (success.data.result_code != 0) {
            this.settelLoading = false;
            this.listLoading = false;
            Message({
              message: success.data.message,
              type: "error",
              duration: 5 * 1000
            });
          } else {
            this.transDateList = [];
            this.settelLoading = false;
            this.listLoading = false;
            Message({
              message: "Settel Success",
              type: "success",
              duration: 5 * 1000
            });
          }
        })
        .catch(error => {
          this.settelLoading = false;
          this.listLoading = false;
          Message({
            message: error,
            type: "error",
            duration: 5 * 1000
          });
        });
    },

    fnSubmit() {
      this.statusData = "import";
      var data = [];
      for (const key in this.dataJson) {
        var obj = {
          number: 0,
          transDate: null,
          createDate: "",
          vendorCode: "",
          orderCode: "",
          amount: 0,
          fullName: "",
          isCompleted: 0,
          clientCode: ""
        };
        if (this.dataJson.hasOwnProperty(key)) {
          const element = this.dataJson[key];
          obj.number = key;
          obj.createDate = element["__EMPTY"];
          obj.vendorCode = element["__EMPTY_1"];
          obj.orderCode = element["__EMPTY_2"];
          obj.clientCode = element["__EMPTY_3"];
          obj.amount = this.fnStrToNum(element["__EMPTY_4"]);
          obj.fullName = element["__EMPTY_5"];
          obj.isCompleted = 0;
        }
        data.push(obj);
      }
      this.importLoading = false;
      this.dataexcel = data;
      this.show = this.dataexcel.length > 0;
      let from = this.pagina.limit * (this.pagina.page - 1);
      this.tempt = this.dataexcel.slice(from, from + this.pagina.limit);
      this.isheadersImport = true;
    },

    getList() {
      if (this.statusData == "import") {
        let data = this.dataexcel;
        this.show = this.data.length > 0;
        let from = this.pagina.limit * (this.pagina.page - 1);
        this.tempt = data.slice(from, from + this.pagina.limit);
      } else {
        let data = this.dataexcel;
        this.show = this.data.length > 0;
        let from = this.pagina.limit * (this.pagina.page - 1);
        this.data = data.slice(from, from + this.pagina.limit);
      }
    },

    fnPushData() {
      this.pushLoading = true;
      var moment = require("moment");
      var dateTime = moment().format("YYYY-MM-DDTHH:mm:ss.SSS");

      const inputData = {
        request_id: uuidv1(),
        date_time: this.$moment().format(),
        data: this.dataexcel
      };

      service
        .post("/repayment/importTrans", inputData, {
          headers: { Authorization: "Bearer " + this.state.user.token }
        })
        .then(success => {
          this.pushLoading = false;
          this.dataexcel = [];
          this.tempt = [];
          this.statusData = "";
          this.settel = true;
          var moment = require("moment");
          let fromDate = moment(this.value[0]).format("YYYY-MM-DD");
          let toDate = moment(this.value[1]).format("YYYY-MM-DD");
          this.loadData([fromDate, toDate]);
          if (success.data.result_code != 0) {
            Message({
              message: success.data.message,
              type: "error",
              duration: 5 * 1000
            });
          } else {
            Message({
              message:
                "Import data success total row: " + success.data.data.totalRow,
              type: "success",
              duration: 5 * 1000
            });
          }
        })
        .catch(error => {
          this.pushLoading = false;
          Message({
            message: error,
            type: "error",
            duration: 5 * 1000
          });
        });
    },

    getCompleted(item) {
      if (item == 1) {
        return { color: "#42f57b", name: "Yes" };
      }
      return { color: "#E57373", name: "No" };
    },

    currencyFormat(num) {
      if (num) {
        return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
      }
      return "";
    },

    importExcel() {
      this.$refs.myFiles.click();
    },

    fnChange() {
      let listFile = this.$refs.myFiles.files;
      if (!listFile || listFile.length == 0) return;
      this.files = listFile[0];
      this.importLoading = true;

      var reader = new FileReader();
      reader.onload = e => {
        // pre-process data
        var binary = "";
        var bytes = new Uint8Array(e.target.result);
        var length = bytes.byteLength;
        for (var i = 0; i < length; i++) {
          binary += String.fromCharCode(bytes[i]);
        }
        /* read workbook */
        var wb = XLSX.read(binary, { type: "binary" });
        /* grab first sheet */
        var wsname = wb.SheetNames[0];
        var ws = wb.Sheets[wsname];
        // delete row A1 A2

        if (
          ws.A1 &&
          ws.A2 &&
          (ws.A1.h && ws.A2.h) &&
          (ws.A1.h.includes("Tổng tiền:") &&
            ws.A2.h.includes("Tổng số lượng giao dịch")) &&
          (ws.A3.h.includes("Thời điểm thanh toán") &&
            ws.B3.h.includes("Nhà cung cấp") &&
            ws.C3.h.includes("Mã đơn hàng") &&
            ws.D3.h.includes("Mã khách hàng") &&
            ws.E3.h.includes("Số tiền(VND)") &&
            ws.F3.h.includes("Họ tên"))
        ) {
          this.infoExcel = ws.A1.h + " VND" + " - " + ws.A2.h;
          delete ws.A1;
          delete ws.A2;
          // result Json
          var resultJson = XLSX.utils.sheet_to_json(ws);
          this.dataJson = resultJson.slice(1);
          this.show = this.dataJson.length > 0;
        } else {
          this.importLoading = false;
          Message({
            message: "File format error",
            type: "error",
            duration: 5 * 1000
          });
        }
      };
      reader.onloadend = e => {
        if (this.dataJson && this.dataJson.length > 0) {
          this.fnSubmit();
        }
      };
      reader.readAsArrayBuffer(this.files);
    },

    fnStrToNum(str) {
      var result = "";
      if (typeof str == "number") {
        return parseInt(str);
      } else {
        for (const key in str.split(",")) {
          result += str.split(",")[key];
        }
        return parseInt(result);
      }
    },

    fnAddSttInData(data) {
      for (const key in data) {
        data[key].number = key;
      }
      return data;
    }
  }
};
</script>

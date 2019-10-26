<template>
  <div>
    <div class="app-container">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-input placeholder="Search" style="min-width: 150px; max-width: 300px;" class="filter-item" />
          <el-button class="filter-item" style="margin-left: 10px; float: right;" type="primary" icon="el-icon-upload2" :loading="importLoading" @click="importExcel">
            Import Excel
          </el-button>
          <!-- <el-button class="filter-item" style="margin-left: 10px; float: right;" type="primary" icon="el-icon-download" @click="loadData">
            Load Data
          </el-button> -->
          <el-button class="filter-item" style="margin-left: 10px; float: right;" type="primary" icon="el-icon-refresh-left" @click="settelTrans">
            Settel Trans
          </el-button>
          <el-button class="filter-item" style="margin-left: 10px; float: right;" type="primary" icon="el-icon-upload2" :loading="pushLoading" @click="fnPushData" v-if="statusData == 'import'">
            Push Data
          </el-button>
          <!-- <el-date-picker
            style="margin-left: 10px; float: right;"
            v-model="dateRange"
            type="daterange"
            range-separator="To"
            start-placeholder="Start date"
            end-placeholder="End date">
          </el-date-picker> -->
          <el-date-picker
            v-model="value"
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
        <el-col :span="12">
          
        </el-col>
      </el-row>
    </div>
    <el-table
      v-loading="listLoading"
      :data="statusData=='import' ? tempt : data"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      height="70vh"
    >
      <div v-for="item in headers" :key="item.key">
        <el-table-column 
          :label="item.title" 
          :prop="item.key" 
          sortable="custom" 
          align="center"
          v-if="item.key == 'transDate' && statusData != 'import'"
        >
        <template slot-scope="scope">
            <span>{{ scope.row[item.key] | moment("MMM DD YYYY HH:mm") }}</span>
        </template>
        </el-table-column>
        <el-table-column 
          :label="item.title" 
          :prop="item.key" 
          sortable="custom" 
          align="center"
          v-else-if="item.key=='isCompleted'"
        >
        <template slot-scope="scope">
            <span :style="'color: '+getCompleted(scope.row[item.key]).color">{{ getCompleted(scope.row[item.key]).name }}</span>
        </template>
        </el-table-column>
        <el-table-column 
          :label="item.title" 
          :prop="item.key" 
          sortable="custom" 
          align="center"
          v-else-if="item.key=='amount'"
        >
        <template slot-scope="scope">
            <span>{{ currencyFormat(scope.row[item.key]) }}</span>
        </template>
        </el-table-column>
        <el-table-column 
            :label="item.title" 
            :prop="item.key" 
            sortable="custom" 
            align="center"
            v-else
        >
        <template slot-scope="scope">
            <span>{{ scope.row[item.key] }}</span>
        </template>
        </el-table-column>
      </div>
    </el-table>
    <pagination v-show="dataexcel.length>0" :total="dataexcel.length" :page.sync="pagina.page" :limit.sync="pagina.limit" @pagination="getList" v-if="statusData=='import'"/>
    <pagination v-show="dataexcel.length>0" :total="dataexcel.length" :page.sync="pagina.page" :limit.sync="pagina.limit" @pagination="getList" v-else/>
  </div>
</template>

<script>
import XLSX from 'xlsx'
import axios from 'axios'
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API_REPAYMENT,
  // timeout: 5000
})
import { MessageBox, Message } from 'element-ui'
import Pagination from '@/components/Pagination'

export default {
  name: 'Repayment',
  components: { Pagination },
  data() {
    return {
      dateRange: '',
      value: [],
      pickerOptions: {
        onPick: ({maxDate, minDate}) => {
          maxDate ? this.value = [minDate,maxDate] : this.value = [minDate,minDate]
        }, disabledDate: (time) => {
        let timeRange = 7*24*60*60*1000 // 7day
        const maxDate1 = this.$moment().date(Number)._d.getTime() 
        const minDate1 = maxDate1 - timeRange
        return time.getTime() < minDate1 || time.getTime() >   maxDate1
        }
      },
      files: null,
      listLoading: false,
      importLoading: false,
      dataJson: null,
      fromDate: null,
      toDate: null,
      headers: [
        { key: 'transDate', title: 'Trans Date', align: 'center', header_align: 'center' },
        { key: 'vendorCode', title: 'Vendor Code', align: 'center', header_align: 'center' },
        { key: 'orderCode', title: 'Order Code', align: 'center', header_align: 'center' },
        { key: 'clientCode', title: 'Client Code', align: 'center', header_align: 'center' },
        { key: 'amount', title: 'Amount', align: 'center', header_align: 'center' },
        { key: 'fullName', title: 'Full Name', align: 'center', header_align: 'center' },
        { key: 'isCompleted', title: 'Completed', align: 'center', header_align: 'center' }
      ],
      headersImport: [
        { key: 'vendorCode', title: 'Vendor Code', align: 'center', header_align: 'center' },
        { key: 'orderCode', title: 'Order Code', align: 'center', header_align: 'center' },
        { key: 'clientCode', title: 'Client Code', align: 'center', header_align: 'center' },
        { key: 'amount', title: 'Amount', align: 'center', header_align: 'center' },
        { key: 'fullName', title: 'Full Name', align: 'center', header_align: 'center' },
        { key: 'isCompleted', title: 'Completed', align: 'center', header_align: 'center' }
      ],
      data: [],
      dataexcel: [],
      statusData: '',
      tempt: [],
      pagina: {
        page: 1,
        limit: 20
      }
    }
  },

  created() {
    this.setDateFirst()
  },

  watch: {
    value: function(val) {
      var moment = require('moment')
      let fromDate = moment(this.value[0]).format("YYYY-MM-DD")
      let toDate = moment(this.value[1]).format("YYYY-MM-DD")
      this.loadData([fromDate, toDate])
    }
  },

  methods: {
    setDateFirst() {
      var moment = require('moment')
      let fromDate = this.$moment(this.$moment().format("YYYY-MM-DD")).subtract(7, "days").format("YYYY-MM-DD")
      let toDate = this.$moment().format("YYYY-MM-DD")
      this.value = [fromDate, toDate]
      
      this.loadData(this.value)
    },

    loadData (value) {
      var moment = require('moment')
      var dateTime = moment().format("YYYY-MM-DDTHH:mm:ss.SSS")
      this.listLoading = true
      const inputData = {
        "request_id": "",
        "date_time": dateTime,
        "data": {
          "fromDate": value[0] + 'T00:00:00',
          "toDate": value[1] + 'T00:00:00'
        }
      }

      service.post(
          '/repayment/getListTrans',
          inputData,
          { headers: { Authorization: 'Bearer ' + this.state.user.token }
        })
        .then(success => {
          this.listLoading = false
          this.statusData=null
          this.dataexcel = success.data && success.data.data ? success.data.data : []
          this.pagina.page = 1
          let from = this.pagina.limit*(this.pagina.page - 1)
          this.data = this.dataexcel.slice(from, from + this.pagina.limit)
        })
        .catch(error => {
          this.listLoading = false
        })
    },

    settelTrans() {
      this.listLoading = true
      var moment = require('moment');
      var dateTime = moment().format("YYYY-MM-DDTHH:mm:ss.SSS");

      const inputData = {
        "request_id": "",
        "date_time": dateTime,
        "data": {
          "transDate": dateTime
        }
      }

      service.post(
        '/repayment/settleTrans',
        inputData,
        { headers: { Authorization: 'Bearer ' + this.state.user.token }
      })
      .then(success => {
        this.listLoading = false
        this.data = success.data.data
        Message({
            message: 'Settel Success',
            type: 'success',
            duration: 5 * 1000
          })
      })
      .catch(error => {
        this.listLoading = false
        Message({
          message: 'error.message',
          type: 'error',
          duration: 5 * 1000
        })
      })
    },

    fnSubmit() {
      this.headers = this.headersImport
      this.statusData = 'import'
      var data = []
      for (const key in this.dataJson) {
        var obj = {
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
      this.importLoading = false
      this.dataexcel = data
      let from = this.pagina.limit*(this.pagina.page - 1)
      this.tempt = this.dataexcel.slice(from, from + this.pagina.limit)
    },

    getList() {
      if (this.statusData == 'import') {
        let data = this.dataexcel
        let from = this.pagina.limit*(this.pagina.page - 1)
        this.tempt = data.slice(from, from + this.pagina.limit)
      } else {
        let data = this.dataexcel
        let from = this.pagina.limit*(this.pagina.page - 1)
        this.data = data.slice(from, from + this.pagina.limit)
      }
    },

    fnPushData() {
      this.pushLoading = true
      var moment = require('moment');
      var dateTime = moment().format("YYYY-MM-DDTHH:mm:ss.SSS");
      
      const inputData = {
        "request_id": "",
        "date_time": dateTime,
        "data": this.dataexcel
      }

      service.post(
        '/repayment/importTrans',
        inputData,
        { headers: { Authorization: 'Bearer ' + this.state.user.token }
      })
      .then(success => {
        this.pushLoading = false
        this.statusData = ''
        this.dataexcel = []
        this.tempt = []
        var moment = require('moment')
        let fromDate = moment(this.value[0]).format("YYYY-MM-DD")
        let toDate = moment(this.value[1]).format("YYYY-MM-DD")
        this.loadData([fromDate, toDate])
        Message({
          message: 'Import data success total row: ' + success.data.data.totalRow,
          type: 'success',
          duration: 5 * 1000
        })
      })
      .catch(error => {
        this.pushLoading = false
        Message({
          message: error.message,
          type: 'error',
          duration: 5 * 1000
        })
      })
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
      let listFile = this.$refs.myFiles.files
      if (!listFile || listFile.length == 0) return
      this.files = listFile[0]
      this.importLoading = true

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
        delete ws.A1;
        delete ws.A2;
        // result Json
        var resultJson = XLSX.utils.sheet_to_json(ws);
        this.dataJson = resultJson.slice(1);
      };
      reader.onloadend = e => {
        this.fnSubmit()
      }
      reader.readAsArrayBuffer(this.files);
    },

    fnStrToNum(str) {
      var result = "";
      if (typeof str == 'number') {
        return parseInt(str)
      } else {
        for (const key in str.split(",")) {
          result += str.split(",")[key];
        }
        return parseInt(result);
      }
    },

  }
}
</script>

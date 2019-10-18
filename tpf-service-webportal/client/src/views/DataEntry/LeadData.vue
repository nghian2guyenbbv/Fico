<template>
  <div class="app-container">
    <div class="filter-container">
      <!-- <el-input v-model="listQuery.title" placeholder="Title" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" /> -->
      <!-- <el-select v-model="listQuery.importance" placeholder="Imp" clearable style="width: 90px" class="filter-item">
        <el-option v-for="item in importanceOptions" :key="item" :label="item" :value="item" />
      </el-select> -->
      <!-- <el-select v-model="listQuery.sort" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        Search
      </el-button> -->
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        New
      </el-button>
      <!-- <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" >
        Export
      </el-button> -->
      <!-- <el-checkbox v-model="showReviewer" class="filter-item" style="margin-left:15px;" @change="tableKey=tableKey+1">
        reviewer
      </el-checkbox> -->
    </div>
    
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @row-click="handleUpdate"
      height="60vh"
    >
        <div v-for="item in headers" :key="item.key">
            <el-table-column 
                :label="item.title" 
                :prop="item.key" 
                sortable="custom" 
                align="center" 
                :class-name="getSortClass('id')"
                v-if="item.key=='createdAt'"
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
                :class-name="getSortClass('id')"
                v-if="item.key=='updatedAt'"
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
                :class-name="getSortClass('id')"
                v-else
            >
            <template slot-scope="scope">
                <span>{{ scope.row[item.key] | moment("MMM DD YYYY HH:mm") }}</span>
            </template>
            </el-table-column>
        </div>
    </el-table>

    <pagination v-show="list.length>0" :total="list.length" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogDetail" width="95%">
      <el-row>
        <el-col :span="12">
          <el-row>
            <el-col :span="12">
                <p><span>APP ID: </span> <span>{{ temp.appId }}</span></p>
                <p><span>FULL NAME: </span> <span>{{ temp.fullName }}</span></p>
                <p><span>NATIONAL ID: </span> <span>{{ temp.nationalId }}</span></p>
            </el-col>
            <el-col :span="12">
                <p><span>SCHEME: </span> <span>{{ temp.scheme }}</span></p>
                <p><span>STATUS: </span> <span>{{ temp.status }}</span></p>
                <p><span>CREATED AT: </span> <span>{{ temp.createdAt | moment("MMM DD YYYY HH:mm") }}</span></p>
            </el-col>
          </el-row>
          <el-row>
            <p>Comments: </p>
          </el-row>
          <el-row>
            <el-table :data="temp.comments" style="width: 100%" @row-click="handleComment">
              <el-table-column prop="createdAt" label="Created At">
                <template slot-scope="scope">
                  <span>{{ scope.row['createdAt'] | moment("MMM DD YYYY HH:mm") }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="commentId" label="CommentId">
              </el-table-column>
              <el-table-column prop="comment" label="Comment">
              </el-table-column>
              <el-table-column prop="recommnet" label="Recommnet">
              </el-table-column>
            </el-table>
          </el-row>
        </el-col>
        <el-col :span="12">
          <el-row>
            <p>Comment Detail: {{ tempCmt.comment }}</p>
            <input
              type="file"
              id="file"
              ref="myFiles"
              class="custom-file-input"
              @change="previewFiles"
              style="display: none"
            />
          </el-row>
           <el-button v-waves class="filter-item" type="primary" icon="el-icon-download" @click="select" >
            Export
          </el-button>
          <el-row>
            <el-col :span="12">
              <div>TPF_ID Card:</div>
              <div class="sheme-doc-name">
                <p class="sheme-doc-name--filename">
                  {{ files ? files.name.substring(0, files.name.length - 4) : 'Unknow' }}
                </p>
                <p class="sheme-doc-name--filetype">
                  {{ files && files.name.substring(files.name.length - 4) }}
                </p>
              </div>
              <el-input placeholder="Comment" v-model="input"></el-input>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetail = false">
          Cancel
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          Save
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="outerVisible" width="95%">
      <el-alert title="Check Customer Information" type="success" effect="dark" :closable="false"></el-alert>
      <el-row :gutter="20" style="margin-top: 10px; margin-bottom: 10px">
        <el-col :span="6">
          <el-input placeholder="Full Name (*)" v-model="state.dataentry.newCustomer.customerName" :disabled="checkPass"></el-input>
        </el-col>
        <el-col :span="6">
          <el-input placeholder="National Id (*)" v-model="state.dataentry.newCustomer.customerId" type="number" :disabled="checkPass"></el-input>
        </el-col>
        <el-col :span="6">
          <el-input placeholder="New Bank Card No (*)" v-model="state.dataentry.newCustomer.bankCardNumber" type="number" :disabled="checkPass"></el-input>
        </el-col>
        <el-col :span="6">
          <el-input placeholder="DSA Code (*)" v-model="state.dataentry.newCustomer.dsaCode" type="number" :disabled="checkPass"></el-input>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 10px; margin-bottom: 10px">
        <el-col :span="6">
          <el-select v-model="value" placeholder="Branch" class="el-fullwidth-custom">
            <el-option v-for="item in [1,2,3]" :key="item" :label="item" :value="item" :disabled="checkPass"></el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="value" placeholder="City" class="el-fullwidth-custom">
            <el-option v-for="item in [1,2,3]" :key="item" :label="item" :value="item" :disabled="checkPass">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="value" placeholder="District" class="el-fullwidth-custom">
            <el-option v-for="item in [1,2,3]" :key="item" :label="item" :value="item" :disabled="checkPass">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input placeholder="Address (*)" v-model="state.dataentry.newCustomer.currentAddress" :disabled="checkPass"></el-input>
        </el-col>
      </el-row>
      <div v-if="checked">
      <el-alert title="Upload ACCA" type="success" effect="dark" :closable="false"></el-alert>
      <el-row :gutter="20" style="margin-top: 10px; margin-bottom: 10px">
        <el-col :span="12">
          <el-select v-model="value" placeholder="Product" class="el-fullwidth-custom" disabled>
            <el-option v-for="item in [1,2,3]" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-col>
        <el-col :span="12">
          <el-select v-model="value" placeholder="Scheme" class="el-fullwidth-custom">
            <el-option v-for="item in [1,2,3]" :key="item" :label="item" :value="item">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 10px; margin-bottom: 10px">
        <el-col :span="24">
        <el-upload action="#" list-type="picture-card" :auto-upload="false" multiple>
          <i slot="default" class="el-icon-plus"></i>
          <div slot="file" slot-scope="{file}">
            <div class="type-card-docs">Notarization of ID card</div>
            <img class="el-upload-list__item-thumbnail" :src="file.url" :alt="file.name" style="height: 90px">
            <div style="display: block; margin: auto; width: 125px; height: 20px;">
              <p class="sheme-doc-name--filename--create">
                {{ file ? file.name.substring(0, file.name.length - 4) : 'Unknow' }}
              </p>
              <p class="sheme-doc-name--filetype--create">
                {{ file && file.name.substring(file.name.length - 4) }}
              </p>
            </div>
            <span class="el-upload-list__item-actions">
              <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)"><i class="el-icon-zoom-in"></i></span>
              <span class="el-upload-list__item-delete" @click="handleRemove(file)"><i class="el-icon-delete"></i></span>
            </span>
          </div>
        </el-upload>
        <el-dialog :visible.sync="innerVisible" append-to-body>
          <img width="100%" :src="dialogImageUrl" alt="">
        </el-dialog>
        </el-col>
      </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="outerVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" v-if="checked" @click="createData()">
          Save
        </el-button>
        <el-button type="primary" v-else  @click="checkData()" :disabled="checkPass">
          Check
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from './pagination' // secondary package based on el-pagination
import DialogCreate from './dialogCreate'
import axios from 'axios'

export default {
  name: 'LeadDE',
  components: { Pagination, DialogCreate },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    return {
      listSchemeDoc: [],
      dialogImageUrl: '',
      innerVisible: false,
      disabled: false,
      checked: false,
      checkPass: false,
      checkLoading: false,
      newCustomer: {
        requestID: '123456',
        customerName: '',
        customerId: '',
        dsaCode: '',
        bankCardNumber: '',
        currentAddress: '',
        areaId: '100001'
      },
        files: null,
        list: [],
        total: 0,
        listLoading: true,
        listQuery: {
            page: 1,
            limit: 20,
            importance: undefined,
            title: undefined,
            type: undefined,
            sort: '+id'
        },
        headers: [
            {
                key: 'createdAt', //key columns with data key
                title: 'Created At', //title columns will display
                align: 'center', //position of column body
                // width: '230',
                // expand: false,
                // checkbox: false,
                header_align: 'center' //position of column header
            },
            {
                key: 'updatedAt', //key columns with data key
                title: 'Updated At', //title columns will display
                align: 'center', //position of column body
                // width: '230',
                // expand: false,
                // checkbox: false,
                header_align: 'center' //position of column header
            },
            {
                key: 'appId', //key columns with data key
                title: 'App ID', //title columns will display
                align: 'center', //position of column body
                // width: '230',
                // expand: false,
                // checkbox: false,
                header_align: 'center' //position of column header
            },
            {
                key: 'status', //key columns with data key
                title: 'Status', //title columns will display
                align: 'center', //position of column body
                // width: '230',
                // expand: false,
                // checkbox: false,
                header_align: 'center' //position of column header
            },
            {
                key: 'fullName', //key columns with data key
                title: 'Full Name', //title columns will display
                align: 'center', //position of column body
                // width: '230',
                // expand: false,
                // checkbox: false,
                header_align: 'center' //position of column header
            }
        ],
        importanceOptions: [1, 2, 3],
        sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
        statusOptions: ['published', 'draft', 'deleted'],
        showReviewer: false,
        temp: {
          createdAt: '',
          updatedAt: '',
          appId: '',
          status: '',
          fullName: '',
          nationalId: '',
          scheme: ''
        },
        tempCmt: {
          createdAt: '',
          commentId: '',
          comment: '',
          recommnet: ''
        },
        dialogDetail: false,
        outerVisible: false,
        dialogStatus: '',
        textMap: {
            update: 'Detail App',
            create: 'Create App'
        },
        dialogPvVisible: false,
        pvData: [],
        // rules: {
        //     type: [{ required: true, message: 'type is required', trigger: 'change' }],
        //     timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        //     title: [{ required: true, message: 'title is required', trigger: 'blur' }]
        // },
        params: {
          page: 1,
          limit: 13,
          project: 'dataentry'
        }
        
    }
  },
  created() {
    this.getList()
    this.getListScheme()
  },
  methods: {
    getList() {
      this.listLoading = true
      this.$store.dispatch('dataentry/getQuickList', this.params)
        .then((data) => {
          this.list = data.data
          this.total = data.total
          this.listLoading = false
        })
        .catch(() => {
          this.listLoading = false
        })
    },
    getListScheme() {
      this.$store.dispatch('dataentry/getDocsCheme')
        .then((data) => {
          this.listSchemeDoc = data.data
        })
        .catch(() => {
        })
    },
    checkData() {
      this.checkPass = true
      this.$store.dispatch('dataentry/postFirstCheck', this.newCustomer)
        .then((data) => {
          this.checkPass = false
          if (data.data.data.first_check_result == 'pass') {
            this.checked = true
            this.checkPass = true
          }
        })
        .catch(() => {
        })
    },
    handleRemove(file) {
      console.log(file);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.innerVisible = true;
    },
    select() {
      this.$refs.myFiles.click();
    },
    previewFiles() {
      let listFile = this.$refs.myFiles.files
      this.files = listFile[0]
      console.log(this.files)
    },
    rowClicked(a) {
        console.log(a)
    },
    
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        status: 'published',
        type: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.outerVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          this.temp.author = 'vue-element-admin'
          createArticle(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogDetail = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleComment(cmt) {
      this.tempCmt = Object.assign({}, cmt)
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          updateArticle(tempData).then(() => {
            for (const v of this.list) {
              if (v.id === this.temp.id) {
                const index = this.list.indexOf(v)
                this.list.splice(index, 1, this.temp)
                break
              }
            }
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row) {
      this.$notify({
        title: 'Success',
        message: 'Delete Successfully',
        type: 'success',
        duration: 2000
      })
      const index = this.list.indexOf(row)
      this.list.splice(index, 1)
    },
    handleFetchPv(pv) {
      fetchPv(pv).then(response => {
        this.pvData = response.data.pvData
        this.dialogPvVisible = true
      })
    },
    // handleDownload() {
    //   this.downloadLoading = true
    //   import('@/vendor/Export2Excel').then(excel => {
    //     const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
    //     const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
    //     const data = this.formatJson(filterVal, this.list)
    //     excel.export_json_to_excel({
    //       header: tHeader,
    //       data,
    //       filename: 'table-list'
    //     })
    //     this.downloadLoading = false
    //   })
    // },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}`
        ? 'ascending'
        : sort === `-${key}`
          ? 'descending'
          : ''
    }
  }
}
</script>

<style lang="scss" scoped>
.sheme-doc-name {
  background-color: black;
  padding: 5px 15px;
  margin: 10px 0;
  border-radius: 15px;
  display: inline-block;
  &--filename {
    color: white;
    margin: 0 !important;
    max-width: 150px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    float: left;
    &--create {
      color: black;
      font-size: 12px;
      margin: 0 !important;
      max-width: 100px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      float: left;
    }
  }
  &--filetype {
    color: white;
    margin: 0 !important;
    display: inline-block;
    &--create {
      font-size: 12px;
      color: black;
      margin: 0 !important;
      float: left;
    }
  }
}
.el-fullwidth-custom {
  width: 100%
}
.type-card-docs {
  background-color: #13ce66;
  color: white;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input placeholder="Search" v-model="valueSearch" style="width: 500px" @keyup.enter.native="handleSearch">
        <el-select v-model="keySearch" slot="prepend" placeholder="Key" style="width: 150px">
          <el-option label="App ID" value="appId"></el-option>
          <el-option label="Full Name" value="fullName"></el-option>
          <el-option label="National ID" value="identificationNumber"></el-option>
        </el-select>
        <el-button slot="append" icon="el-icon-search" @click="handleSearch"></el-button>
      </el-input>
      <!-- <el-button v-waves class="filter-item" type="primary" icon="el-icon-search"></el-button> -->
      <el-button class="filter-item" style="margin-left: 10px; float: right" type="primary" icon="el-icon-edit" @click="handleCreate">
        New
      </el-button>
    </div>
    
    <el-table
      v-loading="state.dataentry.isLoading"
      :data="state.dataentry.list"
      border fit highlight-current-row
      style="width: 100%"
      @row-click="handleUpdate"
      height="70vh"
    >
        <div v-for="item in headers" :key="item.key">
            <el-table-column 
                :label="item.title" 
                :prop="item.key" 
                align="center"
                v-if="item.key=='createdAt'"
            >
            <template slot-scope="scope">
                <span>{{ scope.row[item.key] | moment("MMM DD YYYY HH:mm") }}</span>
            </template>
            </el-table-column>
            <el-table-column 
                :label="item.title" 
                :prop="item.key" 
                align="center"
                v-else-if="item.key=='updatedAt'"
            >
            <template slot-scope="scope">
                <span>{{ scope.row[item.key] | moment("MMM DD YYYY HH:mm") }}</span>
            </template>
            </el-table-column>
            <el-table-column 
                :label="item.title" 
                :prop="item.key" 
                align="center"
                v-else-if="item.key=='lastComment'"
            >
            <template slot-scope="scope">
                <span>{{ scope.row['comments'] && scope.row['comments'].length != 0 ? scope.row['comments'][(scope.row['comments'].length) - 1].request : '' }}</span>
            </template>
            </el-table-column>
            <el-table-column 
                :label="item.title" 
                :prop="item.key" 
                align="center"
                v-else-if="item.key=='action'"
            >
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" content="Retry Quicklead" placement="top" 
                v-if="scope.row['status'] == 'AUTO_QL_FAIL'">
                <el-button type="primary" size="mini" icon="el-icon-refresh-left" circle @click="retryQuickLead(scope.row)"></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="Fix Manually" placement="top" 
                v-if="scope.row['status'] == 'CANCEL' && scope.row['appId']">
                <el-button type="primary" size="mini" icon="el-icon-edit" circle @click="updateStatusManualy(scope.row)"></el-button>
              </el-tooltip>
              
            </template>
            </el-table-column>
            <el-table-column 
                :label="item.title" 
                :prop="item.key" 
                align="center"
                v-else
            >
            <template slot-scope="scope">
                <span>{{ scope.row[item.key] }}</span>
            </template>
            </el-table-column>
        </div>
    </el-table>

    <el-pagination class="pagination-container"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="state.dataentry.pagination.page"
      :page-sizes="[10, 15, 20, 100]"
      :page-size="state.dataentry.pagination.limit"
      layout="total, sizes, prev, pager, next, jumper"
      :total="parseInt(state.dataentry.total)">
    </el-pagination>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogDetail" width="95%" top="3vh">
      <el-row>
        <el-col :span="12" style="padding: 5px;">
          <el-alert title="Information Customer" type="success" effect="dark" :closable="false"></el-alert>
          <el-row style="border: 1px solid; border-radius: 5px;">
            <el-col :span="12" style="border-right: 1px solid; padding: 5px 15px;">
                <p><span style="font-weight: 600;">APP ID: </span> <span style="font-style: italic;">{{ temp.appId || 'Unknown' }}</span></p>
                <p><span style="font-weight: 600;">FULL NAME: </span> <span style="font-style: italic;">{{ temp.fullName }}</span></p>
                <p><span style="font-weight: 600;">NATIONAL ID: </span> <span style="font-style: italic;">{{ temp.optional && temp.optional.identificationNumber }}</span></p>
            </el-col>
            <el-col :span="12" style="padding: 5px 15px;">
                <p><span style="font-weight: 600;">SCHEME: </span> <span style="font-style: italic;">{{ temp.optional && temp.optional.schemeCode }}</span></p>
                <p><span style="font-weight: 600;">STATUS: </span> <span style="font-style: italic;">{{ temp.status || 'Processing' }}</span></p>
                <p><span style="font-weight: 600;">CREATED AT: </span> <span style="font-style: italic;">{{ temp.createdAt | moment("MMM DD YYYY HH:mm") }}</span></p>
            </el-col>
          </el-row>
          <el-row>
            <el-alert title="Comments" type="success" effect="dark" :closable="false"></el-alert>
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
        <el-col :span="12" style="padding: 5px;">
          <el-row>
            <el-alert :title="'Comment Detail: ' + tempCmt.comment" type="success" effect="dark" :closable="false"></el-alert>
            <input
              type="file"
              id="file"
              ref="myFiles"
              class="custom-file-input"
              @change="previewFiles"
              style="display: none"
            />
          </el-row>
          <el-row style="">
            <el-collapse v-model="activeNames">
              <el-col :span="12" v-for="(i, j) in listSchemeComments" :key="i+j" style="padding: 3px 5px;">
                <el-collapse-item :name="j">
                  <template slot="title">
                    <p style="overflow: hidden; text-overflow: ellipsis; max-width: 200px; white-space: nowrap;"> {{ j }} </p>
                  </template>
                  <div class="sheme-doc-name">
                    <p class="sheme-doc-name--filename">
                      {{ files ? files.name.substring(0, files.name.length - 4) : j }}
                    </p>
                    <p class="sheme-doc-name--filetype">
                      {{ files ? files.name.substring(files.name.length - 4) : '.pdf' }}
                    </p>
                  </div>
                  <div style="float: right; padding: 5px 5px; margin: 4px 0;">
                    <el-button icon="el-icon-view" circle size="mini" @click="handlePictureCardPreview('')"></el-button>
                    <el-button icon="el-icon-upload" circle size="mini" @click="select(i, j)"></el-button>
                  </div>
                  <el-input placeholder="Comment" v-model="i.comment"></el-input>
                </el-collapse-item>
                
              </el-col>
            </el-collapse>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-input placeholder="Comment" v-model="commentsAll"></el-input>
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

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="outerVisible" width="95%" top="5vh" :destroy-on-close="true">
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
          <el-select v-model="state.dataentry.newCustomer.branch" placeholder="Branch" class="el-fullwidth-custom">
            <el-option v-for="item in listBranch.data" :key="item.branchName" :label="item.branchName" :value="item.branchName" :disabled="checkPass"></el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="cityStatus" placeholder="City" class="el-fullwidth-custom" ref="citySelect">
            <el-option v-for="item in listAriaCode.data" :key="item._id" :label="item.cityName" :value="item.data" :disabled="checkPass">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select v-model="state.dataentry.newCustomer.areaId" placeholder="District" class="el-fullwidth-custom">
            <el-option v-for="item in ariaCodeSelect" :key="item.areaCode" :label="item.areaName" :value="item.areaCode" :disabled="checkPass">
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
          <el-select v-model="state.dataentry.newCustomer.product" placeholder="Product" class="el-fullwidth-custom" disabled>
            <el-option v-for="item in [1,2,3]" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-col>
        <el-col :span="12">
          <el-select v-model="listScheme" placeholder="Scheme" class="el-fullwidth-custom" @change="onChangeScheme" ref="schemeSelect">
            <el-option v-for="item in listSchemeDoc.data" :key="item.productName" :label="item.productName" :value="item.documentName">
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 10px; margin-bottom: 10px" v-if="listScheme">
        <el-col :span="24">
            <el-badge :value="countFile" class="item-badge">
              <el-upload
                action="#"
                list-type="picture-card" 
                :auto-upload="false"
                multiple 
                ref="upload" 
                name="files"
                :on-change="onChangeScheme"
                :disabled="uploadFileLoading"
              >
              <i slot="default" class="el-icon-plus"></i>
            
              <div slot="file" slot-scope="{file}">
                  <div v-if="renderComponent">
                    <div v-if="file.scheme" class="type-card-docs">{{ file.scheme }}</div>
                    <div v-else-if="handleTypeScheme(file).file" class="type-card-docs">{{ handleTypeScheme(file).file }}</div>
                    <div v-else class="type-card-docs Unknown" style="text-align: center;">Warning
                        <i class="el-icon-warning" style="float: right; margin: 5px;"></i>
                    </div>
                      
                    <embed :src="file.url" :alt="file.name" style="height: 90px"/>

                    <div style="display: block; margin: auto; width: 125px; height: 20px;">
                      <p class="sheme-doc-name--filename--create">
                        {{ file ? file.name.substring(0, file.name.length - 4) : 'Unknow' }}
                      </p>
                      <p class="sheme-doc-name--filetype--create">
                        {{ file && file.name.substring(file.name.length - 4) }}
                      </p>
                    </div>
                  
                    <el-tooltip class="item" effect="dark" :content="handleTypeScheme(file).result" placement="right-start">
                      <span class="el-upload-list__item-actions">
                        <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)"><i class="el-icon-zoom-in"></i></span>
                        <el-dropdown trigger="click" @command="handleCommand">
                          <span class="el-upload-list__item-edit">
                            <i class="el-icon-edit" style="font-size: 20px; color: white;"></i>
                          </span>
                          <el-dropdown-menu slot="dropdown" >
                            <el-dropdown-item v-for="(i, j) in listObj_scheme" :key="j" :command="{type: j, file: file}">
                              <span style="display: inline-block; width: 20px;"><i v-if="i" class="el-icon-check"></i></span><span>{{ j }}</span>
                            </el-dropdown-item>
                          </el-dropdown-menu>
                        </el-dropdown>
                        
                        <span class="el-upload-list__item-delete" @click="handleRemove(file)"><i class="el-icon-delete"></i></span>
                      </span>
                    </el-tooltip>
                  </div>
              </div>
              
            </el-upload>
            </el-badge>
        </el-col>
      </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="outerVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" v-if="checked && uploadDone" @click="createData()">
          Save
        </el-button>
        <el-button type="primary" v-else-if="checked && !uploadDone" @click="submitUpload()" :disabled="!$refs.upload || !$refs.upload.uploadFiles || !$refs.upload.uploadFiles.length">
          UploadFiles
        </el-button>
        <el-button type="primary" v-else  @click="checkData()" :disabled="checkPass">
          Check
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="dialogImageName" :visible.sync="innerVisible" append-to-body width="80%" top="9vh">
      <embed :src="dialogImageUrl" style="width: 100%; height: 70vh;"/>
    </el-dialog>

    <el-dialog :title="dialogConfirm.title" :visible.sync="dialogConfirm.show" width="50%">
      <span>{{ dialogConfirm.content }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogConfirm.show = false">Cancel</el-button>
        <el-button type="primary" @click="dialogConfirm.action">{{ dialogConfirm.confirm }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import DialogCreate from './dialogCreate'
import { MessageBox, Message } from 'element-ui'

export default {
  name: 'LeadDE',
  components: { DialogCreate },
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
      renderComponent: true,
      fileList: [], 
      valueSearch: '',
      keySearch: '',
      listSchemeDoc: [],
      listObj_scheme: {},
      countFile: 0,
      listAriaCode: [],
      listBranch: [],
      cityStatus: [],
      listScheme: [],
      dialogImageUrl: '',
      dialogImageName: '',
      innerVisible: false,
      disabled: false,
      checked: false,
      checkPass: false,
      checkLoading: false,
      uploadDone: false,
      uploadFileLoading: false,
      
      listSchemeComments: {},
      commentsAll: '',
      activeNames: [],
        files: null,
        list: [],
        total: 0,
        listLoading: true,
        headers: [
          { key: 'action', title: 'Action', align: 'center', header_align: 'center' },
          { key: 'appId', title: 'App ID', align: 'center', header_align: 'center' },
          { key: 'status', title: 'Status', align: 'center', header_align: 'center' },
          { key: 'fullName', title: 'Full Name', align: 'center', header_align: 'center' },
          { key: 'identificationNumber', title: 'National ID', align: 'center', header_align: 'center' },
          { key: 'lastComment', title: 'Last Comment', align: 'center', header_align: 'center' },
          { key: 'assigned', title: 'Create By', align: 'center', header_align: 'center' },
          { key: 'createdAt', title: 'Created At', align: 'center', header_align: 'center' },
          { key: 'updatedAt', title: 'Updated At', align: 'center', header_align: 'center' },
          
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
        dialogConfirm: {
          show: false,
          title: '',
          content: '',
          confirm: '',
          action: ''
        },
        dialogDetail: false,
        outerVisible: false,
        dialogStatus: '',
        textMap: {
            update: 'Detail App',
            create: 'Create App'
        },
    }
  },
  computed: {
    ariaCodeSelect: function () {
      return this.cityStatus
    },
    listSchemeSelect: function () {
      this.countFile = this.listScheme.length
      this.listObj_scheme = {}
      for (let i = 0; i < this.listScheme.length; i++) {
        this.listObj_scheme[this.listScheme[i]] = false
      }
      return this.listScheme
    }
  },
  created() {
    this.getList()
    this.getListScheme()
    this.getListAreaCode()
    this.getBranch()
  },
  methods: {
    // get all data first time
    getList() {
      this.$store.dispatch('dataentry/getQuickList')
    },
    getListScheme() {
      this.$store.dispatch('dataentry/getDocsCheme')
        .then((data) => {
          this.listSchemeDoc = data.data
        })
        .catch(() => {
        })
    },
    getListAreaCode() {
      this.$store.dispatch('dataentry/getAriaCode')
        .then((data) => {
          this.listAriaCode = data.data
        })
        .catch(() => {
        })
    },
    getBranch() {
      this.$store.dispatch('dataentry/getBranch')
        .then((data) => {
          this.listBranch = data.data
        })
        .catch(() => {
        })
    },
    // for list app
    handleSizeChange(a) {
      this.state.dataentry.pagination.limit = a
      this.getList()
    },
    handleCurrentChange(a) {
      this.state.dataentry.pagination.page = a
      this.getList()
    },
    handleSearch() {
      this.state.dataentry.pagination.page = 1
      this.state.dataentry.pagination[this.keySearch] = this.valueSearch
      this.getList()
    },
    // action in 1 of apps
    retryQuickLead(data) {
      this.dialogConfirm.show = true
      this.dialogConfirm.title = 'Retry QuickLead'
      this.dialogConfirm.content = 'Are you sure want to retry quiclead: ' + data.fullName
      this.dialogConfirm.confirm = 'Retry'
      let that = this
      this.dialogConfirm.action = function() {
        if (data && data.optional && data.optional.quickLeadId) {
          that.$store.dispatch('dataentry/retryQuickLead', data.optional.quickLeadId)
            .then((res) => {
              if (res.data.result_code == "0") {
                Message({
                  message:'Automation did receive, wait a few minutes for upgrade',
                  type: 'success',
                  duration: 5 * 1000
                })
                that.dialogConfirm.show = false
              }
            })
            .catch(() => {
            })
        } else {
          Message({
            message:'Some error',
            type: 'error',
            duration: 5 * 1000
          })
        }
      }
    },
    updateStatusManualy(data) {
      this.dialogConfirm.show = true
      this.dialogConfirm.title = 'Update Status Manually QuickLead'
      this.dialogConfirm.content = 'Are you sure want to update manually app: ' + data.appId
      this.dialogConfirm.confirm = 'Update'
      let that = this
      this.dialogConfirm.action = function() {
       if (data && data.appId) {
        that.$store.dispatch('dataentry/updateStatusManualy', data.appId)
          .then((res) => {
            if (res.data.result_code == "0") {
              Message({
                message:'Automation did receive, wait a little for upgrade',
                type: 'success',
                duration: 5 * 1000
              })
              that.dialogConfirm.show = false
            }
          })
          .catch(() => {
          })
        }
      }
    },
    // for create new quicklead
    checkData() {
      this.checkPass = true
      this.$store.dispatch('dataentry/postFirstCheck', this.state.dataentry.newCustomer)
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
    handleTypeScheme(file) {
      const isPDF = file.raw.type === 'application/pdf'
      const isLt2M = file.raw.size / 1024 / 1024 < 2;
      let mess = undefined
      let typeScheme = false
      if (!isPDF) {
        mess = 'Docs must be PDF format!'
      }
      if (!isLt2M) {
        mess = 'Docs size can not exceed 2MB!'
      }

      if (isPDF && isLt2M) {
        let a = this.checkFiles(file.name)
        if (!a.checked) {
          mess = 'Can not read Type_Scheme this file, get 1 Type_Scheme for it!'
        } else {
          typeScheme = a.type
        }
      }

      return { result: mess, file: typeScheme }
    },
    onChangeScheme () {
      if (this.$refs && this.$refs.upload && this.$refs.upload.uploadFiles) {
        for (let j in this.$refs.upload.uploadFiles) {
          let a = this.checkFiles(this.$refs.upload.uploadFiles[j].name)
          if (a.checked) {
            this.listObj_scheme[a.type] = this.$refs.upload.uploadFiles[j]
          }
        }
      }
      this.countFile = 0
      for (let i in this.listObj_scheme) {
        if (!this.listObj_scheme[i]) {
          this.countFile++
        }
      }
    },
    handleCommand(command) {
      this.renderComponent = false;
      for (let j in this.$refs.upload.uploadFiles) {
        if (this.$refs.upload.uploadFiles[j].name == command.file.name) {
          this.$refs.upload.uploadFiles[j].scheme = command.type
        }
      }
      this.listObj_scheme[command.type] = command.file
      this.$set(this.listObj_scheme, command.type, command.file)
      this.countFile = 0
      for (let i in this.listObj_scheme) {
        if (!this.listObj_scheme[i]) {
          this.countFile++
        }
      }
      this.$nextTick(() => {
        this.renderComponent = true;
      });
    },
    checkFiles(fileName) {
      let type = ''
      return {
        checked: this.listSchemeSelect.some(scheme => {
          let keyCheck = scheme
          let regex = new RegExp(keyCheck)
          if (fileName.search(regex, 'i') != -1) {
            type = keyCheck
            return true
          } else {
            return false
          }
        }),
        type: type
      } 
    },
    handleRemove(file) {
      let listFile = this.$refs.upload.uploadFiles
      for (let i in listFile) {
        if (listFile[i].name == file.name) {
          this.$refs.upload.uploadFiles.splice(i, 1)
        }
      }
      for (let j in this.listObj_scheme) {
        if (this.listObj_scheme[j] && this.listObj_scheme[j].name == file.name) {
          this.listObj_scheme[j] = false
        }
      }
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url || '';
      this.dialogImageName = file.name || 'Unknown';
      let that = this 
      setTimeout(function(){ that.innerVisible = true; }, 100);
      
    },
    submitUpload() {
      let formData = new FormData();
      this.uploadFileLoading = true
      for (let i in this.$refs.upload.uploadFiles) {
        let file = this.$refs.upload.uploadFiles[i].raw
        formData.append('files', file)
      }
      
      this.$store.dispatch('dataentry/uploadFiles', formData)
        .then((data) => {
          if (data.data.result_code == '0') {
            let res = data.data.data
            let revertDoc = res.documents
            for (let i in revertDoc) {
              for (let j in this.listObj_scheme) {
                if (this.listObj_scheme[j]) {
                  if (revertDoc[i].originalname == this.listObj_scheme[j].name) {
                    revertDoc[i].type = j
                  }
                }
              }
            }
            this.state.dataentry.quickLeadData.documents = revertDoc
            this.state.dataentry.quickLeadData.quickLeadId = res.quickLeadId
            let fullName = this.state.dataentry.newCustomer.customerName.split(' ')
            this.state.dataentry.quickLeadData.firstName = fullName[0]
            this.state.dataentry.quickLeadData.lastName = fullName[fullName.length - 1]
            this.state.dataentry.quickLeadData.city = this.$refs.citySelect.selectedLabel
            this.state.dataentry.quickLeadData.sourcingBranch = this.state.dataentry.newCustomer.branch
            this.state.dataentry.quickLeadData.schemeCode = this.$refs.schemeSelect.selectedLabel
            this.state.dataentry.quickLeadData.identificationNumber = this.state.dataentry.newCustomer.customerId
            
            this.uploadDone = true
            Message({
              message:'Upload Files success',
              type: 'success',
              duration: 5 * 1000
            })
          }
        })
        .catch(() => {
          this.uploadDone = true
          Message({
            message:'Upload Files failed',
            type: 'error',
            duration: 5 * 1000
          })
        })
    },
    createData() {
      this.$store.dispatch('dataentry/createQuicklead')
        .then((data) => {
          if (data.data.result_code == '0') {
            Message({
              message:'Create Quick Lead success, waiting for automation process!',
              type: 'success',
              duration: 5 * 1000
            })
            this.clearFormCreate()
            this.$store.dispatch('dataentry/clearDataState')
          } else {
            Message({
              message:'Create Quick Lead failed',
              type: 'error',
              duration: 5 * 1000
            })
          }
        })
        .catch(() => {
          Message({
            message:'Create Quick Lead failed',
            type: 'error',
            duration: 5 * 1000
          })
        })
    },
    clearFormCreate() {
      // this.listObj_scheme = {}
      // this.countFile = 0
      // this.cityStatus = []
      // this.listScheme = []
      // this.dialogImageUrl = ''
      // this.dialogImageName = ''
      this.innerVisible = false
      this.outerVisible = false
      // this.disabled = false
      // this.checked = false
      // this.checkPass = false
      // this.checkLoading = false
      // this.uploadDone = false
      // this.uploadFileLoading = false
    },
    // for comment detail 1 app
    handleUpdate(row, column) {
      if (column.property != 'action') {
        this.temp = Object.assign({}, row)
        // console.log(this.listSchemeDoc)
        console.log(row)
        if (row.optional.schemeCode) {
          try {
            this.listSchemeDoc.data && this.listSchemeDoc.data.forEach(scheme => {
              console.log(scheme)
              if (scheme.productName == row.optional.schemeCode) {
                scheme.documentName.forEach(docs => {
                  this.listSchemeComments[docs] = { file: '', comment: '' }
                })
              }
            })
          }
          catch(err) {
            console.log('sdf')
          }
        } else {

        }
        

      //   listSchemeComments: {
      //   'ID Card': { file: '', comment: '' },
      //   'Notarization of ID card': { file: '', comment: '' },
      //   'Family Book': { file: '', comment: '' },
      //   'Notarization of Family Book': { file: '', comment: '' },
      //   'Health Insurance Card': { file: '', comment: '' },
      //   'Banking Statement': { file: '', comment: '' },
      //   'Employer Confirmation': { file: '', comment: '' },
      //   'Labor Contract': { file: '', comment: '' },
      //   'Salary slip': { file: '', comment: '' },
      //   'Customer Photograph': { file: '', comment: '' },
      //   'Map to Customer House': { file: '', comment: '' },
      //   'Customer Signature': { file: '', comment: '' }
      // },
        this.dialogStatus = 'update'
        this.dialogDetail = true
      }
    },
    select(scheme) {
      console.log(scheme)
      this.$refs.myFiles.click();
    },
    previewFiles() {
      let listFile = this.$refs.myFiles.files
      this.files = listFile[0]
    },
    // rowClicked(a) {
    //     console.log(a)
    // },
    
    // handleFilter() {
    //   this.listQuery.page = 1
    //   this.getList()
    // },
    // handleModifyStatus(row, status) {
    //   this.$message({
    //     message: '操作Success',
    //     type: 'success'
    //   })
    //   row.status = status
    // },
    // sortChange(data) {
    //   const { prop, order } = data
    //   if (prop === 'id') {
    //     this.sortByID(order)
    //   }
    // },
    // sortByID(order) {
    //   if (order === 'ascending') {
    //     this.listQuery.sort = '+id'
    //   } else {
    //     this.listQuery.sort = '-id'
    //   }
    //   this.handleFilter()
    // },
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
    },
    
    
    handleComment(cmt) {
      this.tempCmt = Object.assign({}, cmt)
    },
    // updateData() {
    //   this.$refs['dataForm'].validate((valid) => {
    //     if (valid) {
    //       const tempData = Object.assign({}, this.temp)
    //       tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
    //       updateArticle(tempData).then(() => {
    //         for (const v of this.list) {
    //           if (v.id === this.temp.id) {
    //             const index = this.list.indexOf(v)
    //             this.list.splice(index, 1, this.temp)
    //             break
    //           }
    //         }
    //         this.dialogFormVisible = false
    //         this.$notify({
    //           title: 'Success',
    //           message: 'Update Successfully',
    //           type: 'success',
    //           duration: 2000
    //         })
    //       })
    //     }
    //   })
    // },
    // handleDelete(row) {
    //   this.$notify({
    //     title: 'Success',
    //     message: 'Delete Successfully',
    //     type: 'success',
    //     duration: 2000
    //   })
    //   const index = this.list.indexOf(row)
    //   this.list.splice(index, 1)
    // },
    // handleFetchPv(pv) {
    //   fetchPv(pv).then(response => {
    //     this.pvData = response.data.pvData
    //     this.dialogPvVisible = true
    //   })
    // },
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
    // formatJson(filterVal, jsonData) {
    //   return jsonData.map(v => filterVal.map(j => {
    //     if (j === 'timestamp') {
    //       return parseTime(v[j])
    //     } else {
    //       return v[j]
    //     }
    //   }))
    // },
    // getSortClass: function(key) {
    //   const sort = this.listQuery.sort
    //   return sort === `+${key}`
    //     ? 'ascending'
    //     : sort === `-${key}`
    //       ? 'descending'
    //       : ''
    // }
  }
}
</script>

<style lang="scss" scoped>
.el-button + .el-button {
  margin-left: 3px;
}
.el-select .el-input {
  width: 110px;
}
.pagination-container {
  background: #fff;
  margin-top: 10px;
}
.sheme-doc-name {
  background-color: #b3d8ff;
  padding: 5px 15px;
  margin: 10px 0;
  border-radius: 15px;
  display: inline-block;
  &--filename {
    color: black;
    margin: 0 !important;
    max-width: 130px;
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
    color: black;
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
.type-card-docs {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.Unknown {
  background-color: #ce1313;
  color: white;
}
</style>
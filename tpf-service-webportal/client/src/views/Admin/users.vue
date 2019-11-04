<template>
  <div class="app-container">
    <el-button class="filter-item" style="margin-left: 10px; float: right" type="primary" icon="el-icon-edit" @click="dialogCreate()">
      Create User
    </el-button>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="outerVisible" width="60%" :destroy-on-close="true">
      <el-row>
        <el-input
          type="textarea"
          :rows="dialogStatus=='create'? 15 : 25"
          placeholder="Please input"
          v-model="userNew">
        </el-input>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button @click="outerVisible = false">
          Cancel
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData() ">
          {{ dialogStatus=='create'? 'Create' : 'Save' }}
        </el-button>
      </div>
    </el-dialog>
    <el-table
      v-loading
      :data="users"
      border
      fit
      highlight-current-row
      @row-click="handleUpdate"
      style="width: 100%;"
      height="70vh"
    >
      <el-table-column v-for="(item) in headers" :key="item.key" :label="item.title">
        <template slot-scope="scope">
          {{ renderCol(scope.row[item.key], item.type) }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { MessageBox, Message } from 'element-ui'
export default {
  name: "Users",
  data() {
    return {
      textMap: {
        update: 'Update User',
        create: 'Create User'
      },
      dialogStatus: '',
      outerVisible: false,
      userNew: `
        {
          "username": "momotest",
          "email": "momotest@tpb.com.vn",
          "password": "momotest",
          "authorities": "role_user",
          "departments": [ "document_check", "loan_booking", "data_entry" ],
          "projects": ["momo"],
          "branches": ["HN"],
          "optional": {
              "roles": [ "momo_view", "momo_lb_view", "momo_dc_view", "momo_de_view" ]
            }
        }
      `,
      userId: '',
      users: [],
      headers: [
        { key: "createdAt", title: "Created", align: "center", header_align: "center", type: 'DateTime' },
        { key: "username", title: "User Name", align: "center", header_align: "center" },
        { key: "email", title: "Email", align: "center", header_align: "center" },
        { key: "authorities", title: "Authorities", align: "center", header_align: "center" },
        { key: "branches", title: "Branches", align: "center", header_align: "center", type: 'Array' },
        { key: "departments", title: "Departments", align: "center", header_align: "center", type: 'Array' },
        { key: "optional", title: "Roles", align: "center", header_align: "center", type: 'Array' },
        { key: "projects", title: "Projects", align: "center", header_align: "center", type: 'Array' }
      ]
    };
  },

  created() {
    this.getUsers()
  },

  watch: {},

  methods: {
    getUsers () {
      this.$store.dispatch('user/getAllUser')
        .then((data) => {
          this.users = data
        })
        .catch(() => {
        })
    },
    renderCol (value, type) {
      if (type && type == 'DateTime') {
        return this.$moment(value).format('MMM DD YYYY HH:mm')
      } else {
        return value
      }
    },
    dialogCreate() {
      this.dialogStatus = 'create'
      this.outerVisible = true
      this.userNew = `
        {
          "username": "momotest",
          "email": "momotest@tpb.com.vn",
          "password": "momotest",
          "authorities": "role_user",
          "departments": [ "document_check", "loan_booking", "data_entry" ],
          "projects": ["momo"],
          "branches": ["HN"],
          "optional": {
              "roles": [ "momo_view", "momo_lb_view", "momo_dc_view", "momo_de_view" ]
            }
        }
      `
    },
    createData () {
      let cloneObj = Object.assign({}, JSON.parse(this.userNew))
      this.$store.dispatch('user/createUser', cloneObj)
        .then((data) => {
          this.outerVisible = false
          this.getUsers()
          Message({
            message:'Create user ' + cloneObj.username + ' success',
            type: 'success',
            duration: 5 * 1000
          })
        })
        .catch(() => {
        })
    },
    handleUpdate (row) {
      this.dialogStatus = 'update'
      this.userId = row.id
      let {username, email, authorities, optional, departments, projects, branches} = row
      this.userNew = JSON.stringify({username, email, authorities, optional, departments, projects, branches},null,'\t')
      this.outerVisible = true
    },
    updateData () {
      let cloneObj = Object.assign({}, JSON.parse(this.userNew))
      this.$store.dispatch('user/updateUser', { userId: this.userId, data: cloneObj})
        .then((data) => {
          this.outerVisible = false
          this.getUsers()
          Message({
            message:'Update user ' + cloneObj.username + ' success',
            type: 'success',
            duration: 5 * 1000
          })
        })
        .catch(() => {
        })
      
    }
  }
};
</script>

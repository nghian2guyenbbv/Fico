<template>
  <div>
    <div class="app-container">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-input
            placeholder="Search"
            style="min-width: 150px; max-width: 300px;"
            class="filter-item"
          />
          <el-button
            class="filter-item"
            style="margin-left: 10px; float: right;"
            type="primary"
            icon="el-icon-plus"
            @click="createUser"
          >Create User</el-button>
        </el-col>
      </el-row>
    </div>
    <el-table
      v-loading
      :data="data"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      height="70vh"
    >
      <div v-for="item in headers" :key="item.key">
        <el-table-column :label="item.title" :prop="item.key" sortable="custom" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row[item.key] }}</span>
          </template>
        </el-table-column>
      </div>
    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="outerVisible" width="60%">
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="120px"
        class="demo-ruleForm"
      >
        <el-form-item label="Username" prop="username">
          <el-input v-model="ruleForm.username"></el-input>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="ruleForm.email"></el-input>
        </el-form-item>
        <el-form-item label="Password" prop="password">
          <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Department" prop="departments">
          <el-select v-model="ruleForm.departments" placeholder="Department">
            <el-option v-for="item in departmentOp" :key="item.value" :label="item.text" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Project" prop="projects">
          <el-select v-model="ruleForm.projects" placeholder="Project">
            <el-option v-for="item in projectsOp" :key="item.value" :label="item.text" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Authorities" prop="authorities">
          <el-select v-model="ruleForm.authorities" placeholder="Authorities">
            <el-option v-for="item in authoritiesOp" :key="item.value" :label="item.text" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Roles" prop="roles">
          <el-cascader v-model="value" :options="options" :props="{ multiple: true }" clearable></el-cascader>
        </el-form-item>
        <el-form-item label="Notes" prop="desc">
          <el-input type="textarea" v-model="ruleForm.desc"></el-input>
        </el-form-item>
        
        <!-- <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">Create</el-button>
          <el-button @click="resetForm('ruleForm')">Reset</el-button>
        </el-form-item>-->
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="outerVisible = false">Cancel</el-button>

        <el-button type="primary" @click="submitForm('ruleForm')">Create</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Users",
  data() {
    return {
      departmentOp: [
        { value: "data_entry", text: "Data Entry" },
        { value: "document_check", text: "Document Check" },
        { value: "loan_booking", text: "Loan Booking" }
      ],
      projectsOp: [
        { value: "fpt", text: "Fpt" },
        { value: "momo", text: "Momo" },
        { value: "vinid", text: "Vin ID" },
        { value: "trustingsocial", text: "Trusting Social" }
      ],
      rolesOp: [
        {
          value: "de",
          label: "Data Entry",
          children: [{ label: "View", value: "view" }]
        }
      ],
      authoritiesOp: [
        { value: "role_root", text: "role_root" },
        { value: "role_user", text: "role_root" }
      ],
      data: [],
      headers: [
        {
          key: "transDate",
          title: "Trans Date",
          align: "center",
          header_align: "center"
        },
        {
          key: "createdAt",
          title: "createdAt",
          align: "center",
          header_align: "center"
        },
        {
          key: "appId",
          title: "appId",
          align: "center",
          header_align: "center"
        },
        {
          key: "status",
          title: "status",
          align: "center",
          header_align: "center"
        },
        {
          key: "fullName",
          title: "fullName",
          align: "center",
          header_align: "center"
        },
        {
          key: "nationalId",
          title: "nationalId",
          align: "center",
          header_align: "center"
        }
      ],
      dialogStatus: null,
      outerVisible: false,
      textMap: {
        update: "Update User",
        create: "Create User"
      },
      ruleForm: {
        username: "",
        email: "",
        password: "",
        departments: [],
        projects: [],
        authorities: [],
        roles: [],
        enabled: false,
        desc: ""
      },
      rules: {
        username: [
          {
            required: true,
            message: "Please input Activity name",
            trigger: "blur"
          },
          {
            min: 3,
            max: 5,
            message: "Length should be 3 to 5",
            trigger: "blur"
          }
        ],
        email: [
          {
            required: true,
            message: "Please select Activity zone",
            trigger: "change"
          }
        ],
        date1: [
          {
            type: "date",
            required: true,
            message: "Please pick a date",
            trigger: "change"
          }
        ],
        date2: [
          {
            type: "date",
            required: true,
            message: "Please pick a time",
            trigger: "change"
          }
        ],
        type: [
          {
            type: "array",
            required: true,
            message: "Please select at least one activity type",
            trigger: "change"
          }
        ],
        resource: [
          {
            required: true,
            message: "Please select activity resource",
            trigger: "change"
          }
        ],
        desc: [
          {
            required: true,
            message: "Please input activity form",
            trigger: "blur"
          }
        ]
      }
    };
  },

  created() {
    this.data = require("@/store/data").data;
  },

  watch: {},

  methods: {
    createUser() {
      this.dialogStatus = "create";
      this.outerVisible = true;
    }
  }
};
</script>

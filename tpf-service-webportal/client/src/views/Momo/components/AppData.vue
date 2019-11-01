<template>
  <div style="padding: 10px;" v-loading="state.momo.ACCA.isLoading">
    <el-card style=" background-color: rgb(92, 51, 130);">
      <label>
        <font color="white">THÔNG TIN CÁ NHÂN</font>
      </label>
    </el-card>
    <div>
      <el-card style="margin: 5px;">
        <el-row :gutter="20" style="margin-top: 5px; margin-bottom: 5px">
          <el-col :span="12">
            <el-input
              :value="(state.momo.ACCA.obj.firstName || '') + ' ' + (state.momo.ACCA.obj.middleName||'') +' '+ (state.momo.ACCA.obj.lastName||'')"
              @focus="copied(state && ((state.momo.ACCA.obj.firstName|| '') + ' ' + (state.momo.ACCA.obj.middleName || '') + ' ' + (state.momo.ACCA.obj.lastName || '')))"
            >
              <template slot="prepend">Họ và tên:</template>
            </el-input>
          </el-col>

          <el-col :span="6">
            <el-input
              :value="state.momo.ACCA.obj.dateOfBirth|| '' "
              @focus="copied(state.momo.ACCA.obj.dateOfBirth || '')"
            >
              <template slot="prepend">Ngày sinh:</template>
            </el-input>
          </el-col>

          <el-col :span="6" style=" height: 32px; padding-top: 8px;">
            <el-checkbox
              @change="copied(state.momo.ACCA.obj.gender)"
              :disabled=" state.momo.ACCA.obj.gender ? state.momo.ACCA.obj.gender !== 'Male' : true "
              :value="state.momo.ACCA.obj.gender === 'Male'"
              :label="`Nam`"
            ></el-checkbox>

            <el-checkbox
              @change="copied(state.momo.ACCA.obj.gender)"
              :disabled="state.momo.ACCA.obj.gender ? state.momo.ACCA.obj.gender !== 'Female' : true"
              :value="(state.momo.ACCA.obj.gender === 'Female')"
              :label="`Nữ`"
            ></el-checkbox>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 5px; margin-bottom: 5px">
          <el-col :span="6">
            <el-input
              :value="state.momo.ACCA.obj.personalId || ''"
              @focus="copied(state.momo.ACCA.obj.personalId || '')"
            >
              <template slot="prepend">CMND/CCCD Số:</template>
            </el-input>
          </el-col>

          <el-col :span="6">
            <el-input
              :value="state.momo.ACCA.obj.issueDate || ''"
              @focus="copied(state.momo.ACCA.obj.issueDate || '')"
            >
              <template slot="prepend">Ngày cấp:</template>
            </el-input>
          </el-col>

          <el-col :span="6">
            <el-input
              :value="state.momo.ACCA.obj.issuePlace || ''"
              @focus="copied(state.momo.ACCA.obj.issuePlace || '')"
            >
              <template slot="prepend">Nơi cấp:</template>
            </el-input>
          </el-col>

          <el-col :span="6">
            <el-input :value="null" @focus="copied(null)">
              <template slot="prepend">Ngày hết hạn:</template>
            </el-input>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 5px; margin-bottom: 5px">
          <el-col :span="12">
            <el-input
              :value="state.momo.ACCA.obj.employeeCard || ''"
              @focus="copied(state.momo.ACCA.obj.employeeCard || '')"
            >
              <template slot="prepend">Số thẻ bảo hiểm y tế/Số thẻ nhân viên:</template>
            </el-input>
          </el-col>

          <el-col :span="12">
            <el-input
              :value="currencyFormat(state.momo.ACCA.obj.salary || '')+ ' VNĐ'"
              @focus="copied(state.momo.ACCA.obj.salary || '')"
            >
              <template slot="prepend">Salary:</template>
            </el-input>
          </el-col>
        </el-row>

        <el-row :gutter="20" style="margin-top: 5px; margin-bottom: 5px">
          <el-col :span="3.5">
            <div
              class="el-input-group__prepend"
              style=" height: 32px; font-size:12px;"
            >Tình trạng hôn nhân:</div>
          </el-col>
          <el-col :span="12" style=" height: 32px; padding-top: 8px;">
            <el-checkbox
              @change="copied(state.momo.ACCA.obj.maritalStatus)"
              :disabled="state.momo.ACCA.obj.maritalStatus  ? state.momo.ACCA.obj.maritalStatus !== 'Married' : true"
              :value="state.momo.ACCA.obj.maritalStatus ? state.momo.ACCA.obj.maritalStatus === 'Married' : false"
              :label="`Đã kết hôn`"
            ></el-checkbox>

            <el-checkbox
              @change="copied(state.momo.ACCA.obj.maritalStatus)"
              :disabled="state.momo.ACCA.obj.maritalStatus  ? state.momo.ACCA.obj.maritalStatus !== 'Single' : true"
              :value="state.momo.ACCA.obj.maritalStatus ? state.momo.ACCA.obj.maritalStatus === 'Single' : false"
              :label="`Độc thân`"
            ></el-checkbox>

            <el-checkbox
              @change="copied(state.momo.ACCA.obj.maritalStatus)"
              :disabled="state.momo.ACCA.obj.maritalStatus  ? state.momo.ACCA.obj.maritalStatus !== 'Divorced/Widow' : true"
              :value="state.momo.ACCA.obj.maritalStatus ? state.momo.ACCA.obj.maritalStatus === 'Divorced/Widow' : false"
              :label="`Đã ly dị hoặc Góa (vợ/chồng)`"
            ></el-checkbox>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <el-card style=" margin-top:18px; background-color: rgb(92, 51, 130);">
      <label>
        <label>
          <font color="white">ĐỊA CHỈ</font>
        </label>
      </label>
    </el-card>
    <el-card style="margin: 5px;">
      <el-row :gutter="20" style="margin-top: 5px; margin-bottom: 5px">
        <el-col :span="12">
          <el-input
            :value="(state.momo.ACCA.obj.address2 || ' ' +  ' ' + state.momo.ACCA.obj.ward || '')"
            @focus="copied(state.momo.ACCA.obj.address2 || ' ' +  ' ' + state.momo.ACCA.obj.ward || '')"
          >
            <template slot="prepend">Số, Đường, Phường/Xã/Thị Trấn:</template>
          </el-input>
        </el-col>
        <el-col :span="12">
          <el-input
            :value="(state.momo.ACCA.obj.district ||'' )"
            @focus="copied(state.momo.ACCA.obj.district ||'' )"
          >
            <template slot="prepend">Quận/Huyện/Thị Xã/Thành Phố:</template>
          </el-input>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 5px; margin-bottom: 5px">
        <el-col :span="12">
          <el-input
            :value="(state.momo.ACCA.obj.province || '')"
            @focus="copied(state.momo.ACCA.obj.province || '')"
          >
            <template slot="prepend">Tỉnh/Thành Phố:</template>
          </el-input>
        </el-col>
        <el-col :span="12">
          <el-input
            :value="(state.momo.ACCA.obj.addressType)"
            @focus="copied(state.momo.ACCA.obj.addressType)"
          >
            <template slot="prepend">Type:</template>
          </el-input>
        </el-col>
      </el-row>

      <!--  -->
      <el-row :gutter="20" style="margin-top: 5px; margin-bottom: 5px">
        <el-col :span="4">
          <div
            class="el-input-group__prepend"
            style=" height: 32px; font-size:12px;"
          >Địa chỉ liên lạc:</div>
        </el-col>
        <el-col :span="12" style=" height: 32px; padding-top: 8px;">
          <el-checkbox
            @change="copied('Địa chỉ hiện tại')"
            :disabled="false"
            :value="true"
            :label="`Địa chỉ hiện tại`"
          ></el-checkbox>
          <el-checkbox
            @change="copied('Địa chỉ làm việc')"
            :disabled="true"
            :value="false"
            :label="`Địa chỉ làm việc`"
          ></el-checkbox>
          <el-checkbox
            @change="copied('Địa chỉ hộ khẩu')"
            :disabled="true"
            :value="false"
            :label="` Địa chỉ hộ khẩu`"
          ></el-checkbox>
        </el-col>
        <el-col :span="8">
          <el-input
            :value="(state.momo.ACCA.obj.mobilePhone || '')"
            @focus="copied(state.momo.ACCA.obj.mobilePhone || '')"
          >
            <template slot="prepend">ĐTDĐ:</template>
          </el-input>
        </el-col>
      </el-row>
      <!--  -->
      <el-row
        :gutter="20"
        style="margin-top: 5px; margin-bottom: 5px"
        v-if="state.momo.ACCA.obj.region === 'Current Address'"
      >
        <el-col :span="6">
          <el-input
            :value="((state.momo.ACCA.obj.durationYear || '') + ' năm '+ (state.momo.ACCA.obj.durationMonth ||'') +' tháng')"
            @focus="copied((state.momo.ACCA.obj.durationYear || '') + ' năm '+ (state.momo.ACCA.obj.durationMonth ||'') +' tháng')"
          >
            <template slot="prepend">Thời gian cư trú tại địa chỉ hiện tại:</template>
          </el-input>
        </el-col>
      </el-row>
    </el-card>
    <!--        -->
    <el-card
      style=" margin-top:18px; background-color: rgb(92, 51, 130);"
      v-show="state.momo.ACCA.obj.maritalStatus === 'Married'"
    >
      <label>
        <label>
          <font color="white">THÔNG TIN NGƯỜI HÔN PHỐI</font>
        </label>
      </label>
    </el-card>
    <el-row
      :gutter="20"
      style="margin-top: 5px; margin-bottom: 5px"
      v-for="(item, index) in state.momo.ACCA.obj.references"
      :key="item.phoneNumber+ index"
      v-show="item.relation === 'Married' "
    >
      <el-col :span="8">
        <el-input :value="item.fullName || '' " @focus="copied(item.fullName || '')">
          <template slot="prepend">Họ và tên:</template>
        </el-input>
      </el-col>
      <el-col :span="5">
        <el-input value="Vợ/Chồng" @focus="copied(item.relation || '')">
          <template slot="prepend">Relation</template>
        </el-input>
      </el-col>

      <el-col :span="6">
        <el-input :value="item.personalId" @focus="copied(item.personalId || '')">
          <template slot="prepend">CMND/CCCD Số:</template>
        </el-input>
      </el-col>

      <el-col :span="5">
        <el-input :value="item.phoneNumber" @focus="copied(item.phoneNumber || '')">
          <template slot="prepend">DTDĐ:</template>
        </el-input>
      </el-col>
    </el-row>
    <!--  -->

    <el-card style=" margin-top:18px; background-color: rgb(92, 51, 130);">
      <label>
        <label>
          <font color="white">THÔNG TIN NGƯỜI THAM CHIẾU</font>
        </label>
      </label>
    </el-card>
    <el-card style="margin: 5px;">
      <el-row
        :gutter="20"
        style="margin-top: 5px; margin-bottom: 5px"
        v-for="(item, index) in state.momo.ACCA.obj.references"
        :key="item.phoneNumber+ index"
        v-show="item.relation !== 'Spouse' "
      >
        <el-col :span="8">
          <el-input :value="item.fullName || ''" @focus="copied(item.fullName || '')">
            <template slot="prepend">Họ và tên:</template>
          </el-input>
        </el-col>
        <el-col :span="5">
          <el-input :value="item.relation|| ''" @focus="copied(item.relation|| '')">
            <template slot="prepend">Relation:</template>
          </el-input>
        </el-col>

        <el-col :span="6">
          <el-input :value="item.personalId || ''" @focus="copied(item.personalId|| '')">
            <template slot="prepend">CMND/CCCD Số:</template>
          </el-input>
        </el-col>

        <el-col :span="5">
          <el-input :value="item.phoneNumber || ''" @focus="copied(item.phoneNumber || '')">
            <template slot="prepend">DTDĐ:</template>
          </el-input>
        </el-col>
      </el-row>
    </el-card>
    <el-card style=" margin-top:18px; background-color: rgb(92, 51, 130) ;">
      <label>
        <font color="white">THÔNG TIN ĐỀ NGHỊ VAY</font>
      </label>
    </el-card>
    <el-card style="margin: 5px;">
      <el-row :gutter="20" style="margin-top: 5px; margin-bottom: 5px">
        <el-col :span="12">
          <el-input
            :value="state.momo.ACCA.obj.loanDetail ? state.momo.ACCA.obj.loanDetail.product || '' : '' "
            @focus="copied(state.momo.ACCA.obj.loanDetail ? state.momo.ACCA.obj.loanDetail.product || '' : '' )"
          >
            <template slot="prepend">Mã sản phẩm:</template>
          </el-input>
        </el-col>
        <el-col :span="12">
          <el-input
            :value="currencyFormat(state.momo.ACCA.obj.loanDetail ? state.momo.ACCA.obj.loanDetail.loanAmount || '' : '') + 'VNĐ'"
            @focus="copied(state.momo.ACCA.obj.loanDetail ? state.momo.ACCA.obj.loanDetail.loanAmount || '' : '')"
          >
            <template slot="prepend">Số tiền đè nghị vay:</template>
          </el-input>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 5px; margin-bottom: 5px">
        <el-col :span="12">
          <el-input
            :value="currencyFormat(state.momo.ACCA.obj.loanDetail ? state.momo.ACCA.obj.loanDetail.tenor || '' : '') + ' VNĐ'"
            @focus="copied(state.momo.ACCA.obj.loanDetail ? state.momo.ACCA.obj.loanDetail.tenor || '' : '')"
          >
            <template slot="prepend">Kì hạn vay:</template>
          </el-input>
        </el-col>

        <el-col :span="12">
          <el-input
            :value="currencyFormat(state.momo.ACCA.obj.loanDetail ? state.momo.ACCA.obj.loanDetail.annualr || '' : '' )"
            @focus="copied(state.momo.ACCA.obj.loanDetail ? state.momo.ACCA.obj.loanDetail.annualr || '' : '' )"
          >
            <template slot="prepend">Lãi suất:</template>
          </el-input>
        </el-col>
      </el-row>
      <el-row
        :gutter="20"
        style="margin-top: 5px; margin-bottom: 5px"
        v-if="state.momo.ACCA.obj.loanDetail"
      >
        <el-col :span="12">
          <el-input
            :value="currencyFormat(state.momo.ACCA.obj.loanDetail ? state.momo.ACCA.obj.loanDetail.downPayment || '' : '' ) + ' VNĐ'"
            @focus="copied(state.momo.ACCA.obj.loanDetail ? state.momo.ACCA.obj.loanDetail.downPayment || '' : '' )"
          >
            <template slot="prepend">Số tiền trả trước:</template>
          </el-input>
        </el-col>
        <el-col :span="12">
          <el-input
            :value="state.momo.ACCA.obj.loanDetail ? state.momo.ACCA.obj.loanDetail.dsaCode || '' : '' "
            @focus="copied(state.momo.ACCA.obj.loanDetail ? state.momo.ACCA.obj.loanDetail.dsaCode || '' : '')"
          >
            <template slot="prepend">Mã nhân viên:</template>
          </el-input>
        </el-col>
      </el-row>

      <el-row
        :gutter="20"
        style="margin-top: 5px; margin-bottom: 5px"
        v-for="item in state.momo.ACCA.obj.productDetails"
        :key="item + index"
      >
        <el-col :span="12">
          <el-input :value="item.model || ''" @focus="copied(item.model || '')">
            <template slot="prepend">Mã hàng hóa:</template>
          </el-input>
        </el-col>
        <el-col :span="12">
          <el-input :value="item.goodCode || ''" @focus="copied(item.goodCode|| '')">
            <template slot="prepend">Good Code:</template>
          </el-input>
        </el-col>

        <el-col :span="12">
          <el-input :value="item.goodPrice || ''" @focus="copied(item.goodPrice || '')">
            <template slot="prepend">Good Price:</template>
          </el-input>
        </el-col>
        <el-col :span="12">
          <el-input :value="item.goodType || ''" @focus="copied(item.goodType || '')">
            <template slot="prepend">Good Type:</template>
          </el-input>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      obj: null,
      typeAlert: null,
      color: "",
      title: "",
      show: false,
      text: ""
    };
  },
  props: ["data"],

  computed: {},
  created() {
    this.state.momo.ACCA._id = this.$route.params.appid;
    this.$store.dispatch("momo/fnACCA", "ACCA");
  },
  methods: {
    currencyFormat(num) {
      if (num) {
        return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
      }
      return "";
    },
    copied(e) {
      this.$clipboard(e);
      this.color = "#1E88E5";
      this.title = "fa-info-circle";
      this.$message({
        message: "Copied: " + e,
        type: "success"
      });
    }
  }
};
</script>

<style scoped>
</style>

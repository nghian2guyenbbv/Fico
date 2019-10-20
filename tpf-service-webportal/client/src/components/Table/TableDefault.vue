<template>
  <el-table
    :data="data"
    style="width: 100%">
    <div v-for="item in headers" :key="item.key">
      <el-table-column 
          :label="item.title" 
          :prop="item.key" 
          sortable="custom" 
          align="center"
          
      >
      <template slot-scope="scope">
          <span v-if="item.handle">{{ item.handle }}</span>
          <span v-else>{{ scope.row[item.key] }}</span>
      </template>
      </el-table-column>
    </div>
  </el-table>
</template>

<script>

export default {
  name: 'TableDefault',
  props: {
    // iconClass: {
    //   type: String,
    //   required: true
    // },
    // className: {
    //   type: String,
    //   default: ''
    // }
  },
  data() {
    return {
      data: [],
      headers: [
        { key: 'nationalId', title: 'nationalId', align: 'center', header_align: 'center' },
        { key: 'createdAt', title: 'createdAt', align: 'center', header_align: 'center', handle: `scope.row[item.key] | moment("MMM DD YYYY HH:mm")` },
        { key: 'updatedAt', title: 'updatedAt', align: 'center', header_align: 'center' },
        { key: 'appId', title: 'appId', align: 'center', header_align: 'center' },
        { key: 'status', title: 'status', align: 'center', header_align: 'center' },
        { key: 'comments', title: 'comments', align: 'center', header_align: 'center' },
        { key: 'fullName', title: 'Full Name', align: 'center', header_align: 'center' },
      ],
    }
  },
  created() {
    let data = require('@/store/data') 
    this.data = data.data
  },
  computed: {
  }
}
</script>

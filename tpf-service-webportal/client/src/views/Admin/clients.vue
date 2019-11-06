<template>
  <tpf-table
    project="dataentry"
    :header="headers"
    :pagination="false"
    :index="true"
    :tagColor="tagColor"
  ></tpf-table>
</template>

<script>

export default {
  name: 'Clients',
  props: {
  },
  data() {
    return {
      headers: [
        { key: "createdAt", title: "createdAt", align: "center", header_align: "center", type: 'DateTime' },
        { key: "appId", title: "appId", align: "center", header_align: "center" },
        { key: "status", title: "status", align: "left", header_align: "center", type: 'Label' },
        { key: "fullName", title: "fullName", align: "center", header_align: "center" },
        { key: "nationalId", title: "nationalId", align: "center", header_align: "center" }
      ],
      tagColor: {
        PROCESSING_FAIL: "#303133",
        PROCESSING_PASS: "success",
        OK: "success",
        PROCESSING: "warning",
        APPROVED_FINNONE: "infor",
        AUTO_QL_FAIL: "error",
        FAIL: "error",
        COMPLETED: "success",
      }
    }
  },
  created() {
    
  },
  computed: {
    
  }
}
</script>



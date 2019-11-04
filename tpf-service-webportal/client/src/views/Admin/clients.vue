<template>
  <tpf-table
    project="dataentry"
    :header="headers"
    :pagination="true"
    :index="true"
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
        { key: "status", title: "status", align: "center", header_align: "center", type: 'Label' },
        { key: "fullName", title: "fullName", align: "center", header_align: "center" },
        { key: "nationalId", title: "nationalId", align: "center", header_align: "center" }
      ]
    }
  },
  created() {
    
  },
  computed: {
    
  }
}
</script>

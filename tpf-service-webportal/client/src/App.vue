<template>
  <div id="app">
    <router-view />
    <tpf-dialog></tpf-dialog>
  </div>
</template>

<script>
export default {
  name: 'App',
  filters: {
    momentParse: function (date) {
      return moment(date).format('MMMM Do YYYY, h:mm:ss a');
    }
  }
}
</script>

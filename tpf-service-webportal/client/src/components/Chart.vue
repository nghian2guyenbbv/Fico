<template>
  <canvas ref="chart" :width="width" :height="height || 300"></canvas>
  <!-- <v-chart type='line' :data='data'></v-chart> -->
</template>

<script>
import Chart from 'chart.js'

export default {
  props: {
    type: { required: true,type: String },
    data: { required: true, type: Object },
    options: Object, width: Number, height: Number
  },
  data: () => { return { chart: null } },
  watch: {
    'data.labels'() { this.chart.update() },
    'data.datasets'() { this.chart.update() }
  },
  mounted() {
    this.chart = new Chart(this.$refs.chart, {
      type: this.type, data: this.data,
      options: { maintainAspectRatio: false, ...this.options }
    })
  },
  beforeDestroy() { this.chart.destroy() }
}
</script>
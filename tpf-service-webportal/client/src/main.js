import Vue from 'vue'
import VueMoment from 'vue-moment'
import { mapState } from 'vuex'
import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import './styles/element-variables.scss'
import locale from 'element-ui/lib/locale/lang/en'
import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import './icons' // icon
import './permission' // permission control
import './utils/error-log' // error log
import TableDefault from '@/components/Table/TableDefault'
import TpfImageSlide from "@/views/Momo/components/Carousel";
import TpfDialog from "@/views/Momo/components/Dialog";
Vue.component('table-default', TableDefault)
Vue.component('tpf-dialog', TpfDialog)
Vue.component('tpf-image-slide', TpfImageSlide)
import * as filters from './filters' // global filters
import { pagination, opt } from './utils/const-config'
import './utils/socket'
import checkPermission from '@/utils/permission'
import * as cookie from '@/utils/cookie'

Vue.use(Element, {
  locale,
  size: 'small' // set element-ui default size
})
Vue.use(VueMoment)
// register global utility filters
// Object.keys(filters).forEach(key => {
//   Vue.filter(key, filters[key])
// })

Vue.config.productionTip = false

Vue.mixin({
  computed: mapState({ state: state => state }),
  methods: {
    checkPermission,
    fnCookie: () => cookie
	}
})

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})

import Vue from 'vue'
import Vuetify from 'vuetify'
import VueMoment from 'vue-moment'
import { VueEditor } from 'vue2-editor'
import { mapState, mapActions } from 'vuex'
import { fnCopy, fnIsAuth } from './utils'
import App from './App.vue'
import router from './router'
import store from './store'
import Chart from './components/Chart'
import colors from 'vuetify/es5/util/colors'
import 'vuetify/dist/vuetify.min.css'
import 'material-design-icons-iconfont/dist/material-design-icons.css'

Vue.use(Vuetify, { theme: { primary: colors.purple } })
Vue.use(VueMoment)
Vue.component('v-chart', Chart)
Vue.component('v-editor', VueEditor)
Vue.filter('fDateTime', v => Vue.moment(v).format('HH:mm DD/MM/YYYY'))

Vue.mixin({
  computed: mapState(['s']),
  methods: {
    fnCopy, fnIsAuth, ...mapActions([
      'fnToastr', 'fnLogout', 'fnLogin', 'fnPagination', 'fnRead', 'fnCreate', 'fnUpdate', 'fnDelete',
      'fnCallResetView', 'fnCallListView', 'fnCallCreateView', 'fnCallUpdateView', 'fnCallDeleteView'
    ])
  }
})

router.beforeEach(async (to, from, next) => {
  const user = await store.dispatch('fnGetUserSession')

  if (to.matched[0].props.default) {
    const { auth, opt } = to.matched[0].props.default
    if (auth && !fnIsAuth(user, opt)) return next('/login')
  }

  next()
})

new Vue({ router, store, render: h => h(App) }).$mount('#app')

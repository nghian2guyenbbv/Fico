import Layout from '@/layout'

const momoRouter = {
  path: '/momo',
  component: Layout,
  redirect: 'noRedirect',
  name: 'Momo',
  meta: {
    title: 'MoMo',
    iconSub: 'el-icon-eleme',
  },
  children: [
    {
      path: 'automation',
      component: () => import('@/views/Momo/Automation'),
      name: 'AutomationMomo',
      meta: { title: 'Automation', roles: ['momo_de_view'], projects: ['momo'], iconSub: 'el-icon-loading' }
    },
    {
      path: 'documentcheck',
      component: () => import('@/views/Momo/Documentcheck'),
      name: 'DocumentcheckMomo',
      meta: { title: 'Document Check', roles: ['momo_dc_view'], projects: ['momo'], iconSub: 'el-icon-finished' }
    },
    {
      path: 'loanbooking',
      component: () => import('@/views/Momo/Loanbooking'),
      name: 'LoanbookingMomo',
      meta: { title: 'Loan Booking', projects: ['momo'], roles: ['momo_lb_view'], iconSub: 'el-icon-news' }
    },
    {
      path: 'appstatus',
      component: () => import('@/views/Momo/Appstatus'),
      name: 'AppstatusMomo',
      meta: { title: 'App Status', projects: ['momo'], roles: ['momo_as_view'], iconSub: 'el-icon-pie-chart' }
    }
  ]
}

export default momoRouter

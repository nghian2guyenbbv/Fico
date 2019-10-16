import Layout from '@/layout'

const momoRouter = {
  path: '/momo',
  component: Layout,
  redirect: 'noRedirect',
  name: 'Momo',
  meta: {
    title: 'MoMo',
    icon: 'fas fa-spinner',
    roles: ['momo_view', ]
  },
  children: [
    {
      path: 'automation',
      component: () => import('@/views/Momo/Automation'),
      name: 'AutomationMomo',
      meta: { title: 'Automation', projects: ['momo']}
    },
    {
      path: 'documentcheck',
      component: () => import('@/views/Momo/Documentcheck'),
      name: 'DocumentcheckMomo',
      meta: { title: 'Documentcheck',roles: ['momo_view'],projects: ['momo']}
    },
    {
        path: 'loanbooking',
        component: () => import('@/views/Momo/Loanbooking'),
        name: 'LoanbookingMomo',
        meta: { title: 'Loanbooking', projects: ['momo']}
      },
      {
        path: 'appstatus',
        component: () => import('@/views/Momo/Appstatus'),
        name: 'AppstatusMomo',
        meta: { title: 'Appstatus', projects: ['momo']}
      }
  ]
}

export default momoRouter

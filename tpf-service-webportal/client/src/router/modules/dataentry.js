import Layout from '@/layout'

const dataentryRouter = {
  path: '/dataentry',
  component: Layout,
  redirect: 'noRedirect',
  name: 'DataEntry',
  meta: {
    title: 'Data Entry',
    icon: 'chart',
    roles: ['de_lead_view', 'de_return_view']
  },
  children: [
    {
      path: 'lead',
      component: () => import('@/views/DataEntry/LeadData'),
      name: 'LeadDE',
      meta: { title: 'Lead Data', roles: ['de_lead_view'] }
    },
    {
      path: 'return',
      component: () => import('@/views/DataEntry/ReturnDE'),
      name: 'ReturnDE',
      meta: { title: 'Return', roles: ['de_return_view'] }
    }
  ]
}

export default dataentryRouter

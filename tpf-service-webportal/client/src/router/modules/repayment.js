import Layout from '@/layout'

const repaymentRouter = {
  path: '/repayment',
  component: Layout,
  redirect: '/repayment',
  meta: {
    title: 'Repayment',
    icon: 'icon',
  },
  children: [
    {
      path: '',
      component: () => import('@/views/Repayment/index'),
      name: 'Repayment',
      meta: { title: 'Repayment',  roles: ['repayment_view'], icon: 'icon' }
    }
  ]
}

export default repaymentRouter

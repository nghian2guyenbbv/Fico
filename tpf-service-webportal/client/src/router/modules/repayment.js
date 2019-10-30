import Layout from '@/layout'

const repaymentRouter = {
  path: '/repayment',
  component: Layout,
  redirect: '/repayment',
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

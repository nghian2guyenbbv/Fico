import Layout from '@/layout'

const repaymentRouter = {
  path: '/repayment',
  component: Layout,
  redirect: '/repayment',
  name: 'Repayment',
  meta: {
    title: 'Repayment',
    iconSub: 'el-icon-coin',
  },
  children: [
    {
      path: '',
      component: () => import('@/views/Repayment/index'),
      name: 'Repayment',
      meta: { title: 'Repayment',  roles: ['repayment_view'], iconSub: 'el-icon-money' }
    }
  ]
}

export default repaymentRouter

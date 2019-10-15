import Layout from '@/layout'

const componentsRouter = {
  path: '/components',
  component: Layout,
  redirect: 'noRedirect',
  name: 'Components',
  meta: {
    title: 'Components',
    icon: 'component'
  },
  children: [
    {
      path: 'icon',
      component: () => import('@/views/Components/icons'),
      name: 'Icon',
      meta: { title: 'Icon' }
    }
  ]
}

export default componentsRouter

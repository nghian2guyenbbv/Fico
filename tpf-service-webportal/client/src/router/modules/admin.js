import Layout from '@/layout'

const adminRouter = {
    path: '/admin',
    component: Layout,
    redirect: 'noRedirect',
    name: 'Admin',
    meta: {
        title: 'Admin',
        icon: 'user'
    },
    children: [
        {
            path: 'users',
            component: () => import('@/views/Admin/users'),
            name: 'Users',
            meta: { title: 'Users', roles: ['admin_user'] }
        },
        {
            path: 'clients',
            component: () => import('@/views/Admin/clients'),
            name: 'Clients',
            meta: { title: 'Clients', roles: ['admin_client'] }
        }
    ]
}

export default adminRouter

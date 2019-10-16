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
            path: 'roles',
            component: () => import('@/views/Admin/users'),
            name: 'Roles',
            meta: { title: 'Roles', roles: ['admin_role'] }
        },
        {
            path: 'clientid',
            component: () => import('@/views/Admin/users'),
            name: 'ClientID',
            meta: { title: 'ClientID', roles: ['admin_client'] }
        }
    ]
}

export default adminRouter

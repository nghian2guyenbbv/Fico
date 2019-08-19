import Vue from 'vue'
import Router from 'vue-router'

import DashBoard from './components/DashBoard'
import Account from './components/Account'
import Client from './components/Client'
import Login from './components/Login'

Vue.use(Router)

export default new Router({
	routes: [
		{
			path: '/',
			name: 'dashboard',
			component: DashBoard,
			props: { auth: true }
		},
		{
			path: '/account',
			name: 'account',
			component: Account,
			props: { auth: true, opt: { authorities: ['role_root', 'role_admin'] } }
		},
		{
			path: '/client',
			name: 'client',
			component: Client,
			props: { auth: true, opt: { authorities: ['role_root'] } }
		},
		{
			path: '/login',
			name: 'login',
			component: Login
		},
		{
			path: '*',
			redirect: '/'
		}
	]
})

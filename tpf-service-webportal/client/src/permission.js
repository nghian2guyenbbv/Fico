import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import * as cookie from '@/utils/cookie'
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login']

router.beforeEach(async (to, from, next) => {
  document.title = getPageTitle(to.meta.title)
  NProgress.start()

  const hasToken = cookie.getToken()
  if (hasToken) {
    const roles = cookie.getRoles()
    if (roles && roles.length > 0) {
      store.dispatch('app/fnSocket')
      if (to.path === '/login') {
        next({ path: '/' })
      } else {
        
        if (to.meta && to.meta.roles) {
          if (roles.includes("admin")) {
            next()
          } else {
            if (roles.some(role => to.meta.roles.includes(role))) {
              next()
            } else {
              next({ path: '/' })
            }
          }
        } else {
          next()
        }
      }
    } else {
      await store.dispatch('user/resetToken')
      Message.error('Token has been expires, login again!')
      next(`/login?redirect=${to.path}`)
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
  }

  NProgress.done()
})

router.afterEach(() => {
  NProgress.done()
})
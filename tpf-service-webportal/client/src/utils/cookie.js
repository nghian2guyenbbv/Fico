import Cookies from 'js-cookie'

export function getToken() {
  return Cookies.get('token')
}

export function setToken(token, expires) {
  return Cookies.set('token', token, { expires: expires })
}

export function removeToken() {
  return Cookies.remove('token')
}

export function getRoles() {
  return Cookies.getJSON('roles')
}

export function setRoles(roles) {
  return Cookies.set('roles', roles)
}

export function removeRoles() {
  return Cookies.remove('roles')
}

export function setInforUser(infor) {
  return localStorage.setItem('INFOR_USER', JSON.stringify(infor))
}

export function getInforUser() {
  return JSON.parse(localStorage.getItem('INFOR_USER'))
}

export function clearCookie() {
  Cookies.remove('roles')
  Cookies.remove('token')
  localStorage.clear()
  return
}

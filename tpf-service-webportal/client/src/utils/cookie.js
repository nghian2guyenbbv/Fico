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

export function clearCookie() {
  Cookies.remove('roles')
  Cookies.remove('token')
  return
}

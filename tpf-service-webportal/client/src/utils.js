export const fnCopy = data => JSON.parse(JSON.stringify(data))

export const fnIsAuth = (user, option) => {
	if (user && option && option.authorities && option.authorities.indexOf(user.authorities[0]) === -1) {
		return false
	}

	return user || false
}
require('dotenv').config({ path: __dirname + `/${process.env.NODE_ENV || 'local'}.env` })
const axios = require('axios')
const express = require('express')
APP = express()

// INIT
require('./redis')
require('./rabbitmq')
require('./eureka')

// INIT SOCKET.IO
IO = require('socket.io')(require('http').createServer(APP).listen(Number(process.env.PORT)))
IO.adapter(require('socket.io-redis')({ host: process.env.REDIS_HOST, port: Number(process.env.REDIS_PORT) }))
IO.of('/').adapter.on('error', () => { })

// MIDDLEWARE
APP.use(require('cors')())
APP.use(require('helmet')())
APP.use(require('compression')())
APP.use(express.json({ limit: '1mb' }))
APP.use(express.urlencoded({ limit: '1mb', extended: true }))
APP.use(express.static(`${__dirname}/../public/`))
APP.use((req, res, next) => next(APP.get('ERROR_REDIS') && { message: 'Redis Disconnected' }))
APP.use((req, res, next) => next({ status: 404, message: 'Api Not Found' }))
APP.use((err, req, res, next) => {
	const { status, code, message, error } = err
	res.status(status || 500).send({ code, message, error })
})

// SOCKET.IO
IO.use(async (socket, next) => {
	try {
		if (APP.get('ERROR_REDIS')) return next(new Error('Redis Disconnected'))

		const token = socket.handshake.query.token
		if (!token) return next(new Error('Forbidden'))

		const success = await axios.get(process.env.USER_INFO_URI, { headers: { Authorization: 'Bearer ' + token } })
		socket.user = success.data
		socket.id = socket.user.user_name

		next()
	} catch (error) {
		next(error)
	}
})

IO.on('connection', (socket) => {
	if (!socket.user.departments) socket.join('default')
	else socket.user.departments.forEach(d => socket.user.projects.forEach(p => socket.join(d + p)))
	socket.on('disconnect', () => { })
})
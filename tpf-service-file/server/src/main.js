require('dotenv').config({ path: __dirname + `/${process.env.NODE_ENV || 'local'}.env` })
const express = require('express')

// INIT APP
APP = express()
require('http').createServer(APP).listen(Number(process.env.PORT))

// MIDDLEWARE
APP.use(require('cors')())
APP.use(require('helmet')())
APP.use(require('compression')())
APP.use(express.json({ limit: '1mb' }))
APP.use(express.urlencoded({ limit: '1mb', extended: true }))
APP.use(express.static(`${__dirname}/../public/`))
APP.use((req, res, next) => {
	const message = []
	APP.get('ERROR_MONGO') && message.push('Mongo')
	next(message.length && { message: `${message} Disconnected` })
})

// ROUTERS
APP.use(require('./routers'))
APP.use((req, res, next) => next({ status: 404, message: 'Api Not Found' }))

// HANDLE ERROR
APP.use((err, req, res, next) => {
	const { status, code, message, error } = err
	res.status(status || 500).send({ code, message, error })
})
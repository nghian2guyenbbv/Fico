const mongoose = require('mongoose')

class Mongo {

	constructor() {
		mongoose.Promise = global.Promise

		mongoose.connect(process.env.MONGO_URI, {
			useNewUrlParser: true,
			useCreateIndex: true,
			useFindAndModify: false,
			reconnectTries: Number.MAX_VALUE,
			reconnectInterval: 1000
		})

		mongoose.connection.on('error', () => { })

		mongoose.connection.on('connected', () => {
			console.log('Mongo connected')
			APP.set('ERROR_MONGO', false)
		})

		mongoose.connection.on('disconnected', () => {
			console.error('Mongo disconnected')
			!APP.get('ERROR_MONGO') && APP.set('ERROR_MONGO', true)
		})
	}

}

module.exports = new Mongo()
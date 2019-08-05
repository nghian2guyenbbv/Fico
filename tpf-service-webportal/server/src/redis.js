const IORedis = require('ioredis')

class Redis {

	constructor() {
		this.client = new IORedis({
			port: Number(process.env.REDIS_PORT),
			host: process.env.REDIS_HOST,
			retryStrategy: () => 1000
		})

		this.client.on('ready', () => {
			console.info('Redis Connected')
			APP.set('ERROR_REDIS', false)
		})

		this.client.on('error', () => {
			console.error('Redis Disconnected')
			!APP.get('ERROR_REDIS') && APP.set('ERROR_REDIS', true)
		})
	}

}

module.exports = new Redis()
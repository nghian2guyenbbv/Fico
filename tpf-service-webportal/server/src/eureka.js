const eureka = require('eureka-js-client').Eureka

class Eureka {

	constructor() {
		this.client = new eureka({
			instance: {
				app: process.env.APP_ID,
				hostName: `${process.env.APP_ID}:${process.env.PORT}`,
				ipAddr: '127.0.0.1',
				port: { '$': 3000, '@enabled': true },
				vipAddress: process.env.APP_ID,
				statusPageUrl: `http://${process.env.APP_ID}:${process.env.PORT}`,
				dataCenterInfo: {
					'@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
					name: 'MyOwn'
				}
			},
			eureka: {
				host: process.env.EUREKA_HOST,
				port: 8761,
				servicePath: '/eureka/apps/'
			}
		})
		this.client.start()
	}

}

module.exports = new Eureka()
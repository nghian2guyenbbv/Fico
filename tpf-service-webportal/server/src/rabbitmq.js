const rabbitmq = require('amqplib')

class Rabbit {

	constructor() {
		const { RABBITMQ_USERNAME, RABBITMQ_PASSWORD, RABBITMQ_HOST, RABBITMQ_PORT, RABBITMQ_APP_ID } = process.env
		const connectionString = `amqp://${RABBITMQ_USERNAME}:${RABBITMQ_PASSWORD}@${RABBITMQ_HOST}:${RABBITMQ_PORT}`

		rabbitmq.connect(connectionString).then(conn => conn.createChannel()).then(ch => {
			ch.assertQueue(RABBITMQ_APP_ID)
			ch.consume(RABBITMQ_APP_ID, msg => {
				try {
					const payload = JSON.parse(msg.content.toString())
					const { token, body } = payload
					const { from, to, project, data } = body
					const roomFrom = ['default', from + project]
					const roomTo = ['default', to + project]
					if (from !== to) {
						roomFrom.forEach(r => IO.to(r).send({ action: 'DELETE', from, to, project, data }))
						roomTo.forEach(r => IO.to(r).send({ action: 'CREATE', from, to, project, data }))
					} else {
						if (!from) IO.send({ action: 'UPDATE', from, to, project, data })
						else roomFrom.forEach(r => IO.to(r).send({ action: 'UPDATE', from, to, project, data }))
					}
				} catch (error) {
					console.error(error)
				}
				ch.ack(msg)
			})
		}).catch(console.error)
	}

}

module.exports = new Rabbit()
const rabbitmq = require('amqplib')

class Rabbit {

  constructor() {
    const { RABBITMQ_USERNAME, RABBITMQ_PASSWORD, RABBITMQ_HOST, RABBITMQ_PORT, RABBITMQ_APP_ID } = process.env
    const connectionString = `amqp://${RABBITMQ_USERNAME}:${RABBITMQ_PASSWORD}@${RABBITMQ_HOST}:${RABBITMQ_PORT}`

    rabbitmq.connect(connectionString).then(conn => conn.createChannel()).then(ch => {
      ch.assertQueue(RABBITMQ_APP_ID)
      ch.consume(RABBITMQ_APP_ID, msg => ch.ack(msg))
    }).catch(console.error)
  }

}

module.exports = new Rabbit()
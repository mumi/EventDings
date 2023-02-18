# Event Delivery

Listens on AMQP Queues and forwards the events to the configured endpoints.

## Environment variables
* `SUBSCRIBING_URL`: (required) URL of the subscribing API
* `RABBITMQ_HOST`: (required)
* `RABBITMQ_USER`: (required)
* `RABBITMQ_PASSWORD`: (required)
* `RABBITMQ_VHOST`: (default: `/`)
* `RABBITMQ_AMQP_PORT`: (default: `5672`)
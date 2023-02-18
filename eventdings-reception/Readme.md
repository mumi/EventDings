# Event Reception

Provides an addressable POST endpoint expecting cloudevents and forwards them to an RabbitMQ Stream.

## Environment variables

* `RABBITMQ_HOST`: (required)
* `RABBITMQ_STREAM`: (required) Name of the Stream Queue to forward to
* `RABBITMQ_USER`: (required)
* `RABBITMQ_PASSWORD`: (required)
* `RABBITMQ_VHOST`: (default: `/`)
* `RABBITMQ_STREAM_PORT`: (default: `5552`)
# EventDings

EventDings is a simple event routing system. It is designed to be used in a microservice architecture, where each service is responsible for a single task. EventDings allows you to route events from one service to another, and to define a set of rules that determine which events are routed to which services.


### Architecture

EventDings consists of four components: A receptionist, a subscribing API, a dispatcher and a delivery service. The receptionist is a webhook that receives events and forwards them into a RabbitMQ stream. The subscribing API is a REST API that allows you to subscribe to events (with filters). The dispatcher is a service that listens to the RabbitMQ stream and forwards events to their queue(s) (One queue per subscription). The delivery service listens to the RabbitMQ queues and delivers events to the destination of the subscriptions.
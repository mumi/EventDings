# Maverick.EventDispatcher

The Event Dispatcher is a simple event routing system. It is designed to be used in a microservice architecture, where each service is responsible for a single task. The dispatcher allows you to route events from one service to another, and to define a set of rules that determine which events are routed to which services.

This a stop-gap solution for developers planning to implement software on top of a event-driven architecture targeting the K8S stack (using KNative Eventing) or the Cloud (using something like the Azure Event Grid). Setting up KNative can be tricky and is often (at least in our case) rejected by customers with a vanilla Kubernetes setup. Public clouds are regularly forbidden by internal regulation. 


### Features
The event dispatcher consists of multiple side cars augmenting a RabbitMQ instance and its Streams module. All its features are exposed as adressable HTTP endpoints to simplify the integration. 


### Architecture

The Event Dispatcher consists of four components: 

* an adressable receiver, which accepts any kind of CloudEvent through its webhook and forwards them to the event stream managed by RabbitMQ
* the internal broker, which routes the events to the different queues as configured in the subscriptions
* the distributor, which monitors the queues and delivers the events to the addressable destinations configured in the subscriptions
* a subscription service, which provides an API for external systems to create and manage their subscriptions

# rabbitmq-client (Microservice)

This PoC show how to listen/consume messages from RabbitMQ queues dynamically and how to know from which queue the the message is comming.

## Features:
- Swagger UI Console (http://localhost:8080/swagger-ui.html#/rabbitmq-client-controller)
- Subscribe queues to listen dynamically
- Un-subscribe queues to stop listening
- Get a list of which queues are we listen
- Send messages to an specific queue
- Expose metrics (http://localhost:8080/metrics)

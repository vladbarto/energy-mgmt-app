# consume.py
import pika, os

# Access the CLODUAMQP_URL environment variable and parse it (fallback to localhost)
url = os.environ.get('CLOUDAMQP_URL', 'amqps://ivpbhbrx:BB9NSs5i3qYczwRO-ofc-a9WkXEh04r7@sparrow.rmq.cloudamqp.com/ivpbhbrx')
params = pika.URLParameters(url)
connection = pika.BlockingConnection(params)
channel = connection.channel() # start a channel
channel.queue_declare(queue='test_queue') # Declare a queue
def callback(ch, method, properties, body):
  print(" [x] Received " + str(body))

channel.basic_consume('test_queue',
                      callback,
                      auto_ack=True)

print(' [*] Waiting for messages:')
channel.start_consuming()
connection.close()
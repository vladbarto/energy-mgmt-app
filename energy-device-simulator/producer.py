# threading inspiration source: https://www.geeksforgeeks.org/multithreading-python-set-1/

import json
from datetime import datetime
from time import sleep

import pika, os, yaml
import pandas as pd

import threading

simulator_1_config = yaml.full_load(open('simulator1.config.yaml'))
simulator_2_config = yaml.full_load(open('simulator2.config.yaml'))
rabbitExchange = simulator_1_config.get('rabbitMQ', {}).get('exchange')
rabbitQueue = simulator_1_config.get('rabbitMQ', {}).get('queue')
rabbitUrl = simulator_1_config.get('rabbitMQ', {}).get('url')

def connect_to_rabbit():
    url = os.environ.get('CLOUDAMQP_URL', rabbitUrl)
    params = pika.URLParameters(url)
    connection = pika.BlockingConnection(params)  # it waits for all requests to complete

    return connection


def do_channeling(connection):
    channel = connection.channel()  # start a channel

    channel.exchange_declare(exchange=rabbitExchange)
    channel.queue_declare(queue=rabbitQueue)
    channel.queue_bind(rabbitQueue, rabbitExchange, 'tests')  # create binding between queue and exchange

    return channel


def produce_message(channel, contor_reading: float, config_file):

    # create message as json format
    message = {
        "deviceId": config_file.get('simulator', {}).get('device_id'),
        "timestamp": datetime.now().isoformat(),
        "readValue": contor_reading
    }

    # Convert message to JSON string
    message_json = json.dumps(message)

    # publish message
    channel.basic_publish(
        body=message_json,
        exchange=rabbitExchange,
        routing_key='tests',
    )

    print(f' Message {message_json} sent.')


def finish(connection, channel):
    channel.close()
    connection.close()


def citeste_contor(channel, config_file):

    idx = 0

    with open(
            config_file.get('simulator', {})
                    .get('readings', {})
                    .get('file')
            , 'r') as file:

        data = pd.read_csv(file)

    while idx < len(data):
        produce_message(channel, data.iloc[idx, 0], config_file)
        idx += 1
        sleep(1)


if __name__ == '__main__':
    connection = connect_to_rabbit()
    channel = do_channeling(connection)

    # Message producing section, two simulators
    t1 = threading.Thread(target=citeste_contor, args=(channel, simulator_1_config))
    t2 = threading.Thread(target=citeste_contor, args=(channel, simulator_2_config))

    t1.start()
    sleep(5)
    t2.start()

    t1.join()
    t2.join()
    print("Done!")

    # End connection
    finish(connection, channel)

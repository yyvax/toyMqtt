#### What is toy-mqtt?
This is a mqtt-broker written by a tyro in Mqtt.
I borrowed a lot of codes from [iot-mqtt-server](https://gitee.com/recallcode/iot-mqtt-server/paas/huaweicloud_cse).

#### Introduction
1. The project is based on maven
2. Implement most mqtt stuffs via Java netty
3. Use springboot for injections and configs

#### Quick start
# Run the broker
`cd broker`<br>
`mvn spring-boot:run`<br>
  Server is running on port 1883.
  <br>
# Test using a mqtt client
We need a mqtt client for our tests. A recommended tool is [mqtt-spy](https://github.com/eclipse/paho.mqtt-spy/releases/download/1.0.1-beta18/mqtt-spy-1.0.1-beta-b18-jar-with-dependencies.jar).<br>
Go to the directory mqtt-spy locates.<br>
`java -jar mqtt-spy-1.0.1-beta-b18-jar-with-dependencies.jar`
The configuration file should be like this:

Go to the connection tag, and do some publications and subscriptions:


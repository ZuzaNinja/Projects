package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class SmartMeterSimulator {

    private static final String QUEUE_NAME = "consumptionQueue";

    public static void main(String[] args) {
        String sensorFilePath = "src/main/resources/sensor.csv";
        String configFilePath = "src/main/resources/deviceIdFile";

        String deviceId = getDeviceIdFromConfig(configFilePath);
        if (deviceId == null || deviceId.isEmpty()) {
            System.err.println("Device ID not found in configuration file. Please check the file.");
            return;
        }

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("localhost");

        //pe docker
        factory.setUsername("admin");
        factory.setPassword("admin");

        //factory.setUsername("guest");
        //factory.setPassword("guest");

        Queue<Double> slidingWindow = new LinkedList<>();
        List<Double> hourlyConsumptions = new ArrayList<>();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
             BufferedReader br = new BufferedReader(new FileReader(sensorFilePath))) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            String line;
            while ((line = br.readLine()) != null) {
                double measurementValue = Double.parseDouble(line);
                long timestamp = Instant.now().toEpochMilli();

                slidingWindow.add(measurementValue);
                if (slidingWindow.size() > 6) {
                    slidingWindow.poll();
                }

                if (slidingWindow.size() == 6) {
                    double firstValue = slidingWindow.peek();
                    double lastValue = getLastValue(slidingWindow);

                    double hourlyConsumption = lastValue - firstValue;

                    if (hourlyConsumption < 0) {
                        System.err.println("Invalid consumption detected, ignoring...");
                        continue;
                    }

                    hourlyConsumptions.add(hourlyConsumption);

                    double maxHourlyConsumption = Collections.max(hourlyConsumptions);

                    String message = new ObjectMapper().writeValueAsString(
                            new SensorData(deviceId, timestamp, maxHourlyConsumption));

                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                    System.out.println("Sent (Max Hourly Consumption): " + message);

                    slidingWindow.clear();
                    hourlyConsumptions.clear();
                }

                Thread.sleep(400);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double getLastValue(Queue<Double> window) {
        double last = 0.0;
        for (double value : window) {
            last = value;
        }
        return last;
    }

    private static String getDeviceIdFromConfig(String filePath) {
        Properties properties = new Properties();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            properties.load(br);
            return properties.getProperty("device_id_1");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class SensorData {
        private String device_id;
        private long timestamp;
        private double measurement_value;

        public SensorData(String device_id, long timestamp, double measurement_value) {
            this.device_id = device_id;
            this.timestamp = timestamp;
            this.measurement_value = measurement_value;
        }

        public String getDevice_id() {
            return device_id;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public double getMeasurement_value() {
            return measurement_value;
        }
    }
}

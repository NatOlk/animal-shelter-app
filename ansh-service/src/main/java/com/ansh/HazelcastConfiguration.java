package com.ansh;

import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.app.serializer.AnimalSerializer;
import com.ansh.app.serializer.VaccinationSerializer;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.client.HazelcastClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfiguration {
  @Value("${hazelcast.host}")
  private String hazelcastHost;

  @Value("${hazelcast.port}")
  private int hazelcastPort;

  @Value("${hazelcast.cluster.name}")
  private String hazelcastClusterName;

  @Bean
  public ClientConfig hazelcastClientConfig() {
    ClientConfig clientConfig = new ClientConfig();
    clientConfig.setClusterName(hazelcastClusterName);

    clientConfig.getNetworkConfig()
        .addAddress(STR."\{hazelcastHost}:\{hazelcastPort}")
        .setSmartRouting(true);

    clientConfig.getSerializationConfig()
        .addSerializerConfig(new SerializerConfig()
            .setTypeClass(Animal.class)
            .setImplementation(new AnimalSerializer()))
        .addSerializerConfig(new SerializerConfig()
            .setTypeClass(Vaccination.class)
            .setImplementation(new VaccinationSerializer()));

    return clientConfig;
  }

  @Bean
  public HazelcastInstance hazelcastInstance(ClientConfig clientConfig) {
    return HazelcastClient.newHazelcastClient(clientConfig);
  }
}

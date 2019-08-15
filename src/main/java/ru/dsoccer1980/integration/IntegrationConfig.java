package ru.dsoccer1980.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

@IntegrationComponentScan
@Configuration
public class IntegrationConfig {
    @Bean
    public QueueChannel userChannel() {
        return MessageChannels.queue(100).get();
    }

    @Bean
    public PublishSubscribeChannel messageChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(100).get();
    }

    @Bean
    public IntegrationFlow packagingFlow() {
        return IntegrationFlows.from("userChannel")
                .split()
                .handle("emailService", "sendMessage")
                .aggregate()
                .channel("messageChannel")
                .get();
    }
}

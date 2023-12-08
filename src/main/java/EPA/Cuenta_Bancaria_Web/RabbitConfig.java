package EPA.Cuenta_Bancaria_Web;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class RabbitConfig {

    public static final String QUEUE_NAME = "transactions-queue";
    public static final String QUEUE_CLOUD_WATCH_NAME = "transactions-queue-cloud-watch";
    public static final String QUEUE_ERROR = "transactions-error";
    public static final String EXCHANGE_NAME = "transactions-exchange";

    public static final String ROUTING_KEY_NAME = "transactions.routing.key";
    public static final String ROUTING_CLOUD_WATCH_KEY_NAME = "transactions.routing.cloud_watch.key";
    public static final String ROUTING_ERROR_KEY_NAME = "transactions.routing.error.key";
    //public  final String URI_NAME = "amqp://rabbit_user:b5x26z4p@192.168.65.3:5672";

   // private final String URI_NAME;

    @Value("${rabbit.uri}")
    public static String URI_NAME;

    @Bean
    public static URI uri(@Value("${rabbit.uri}") String uri) {
        URI_NAME = uri;
        return URI.create(uri);
    }
    @Bean
    public AmqpAdmin amqpAdmin(URI uri) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(uri);
        var amqpAdmin =  new RabbitAdmin(connectionFactory);

        var exchange = new TopicExchange(EXCHANGE_NAME);


        var queue = new Queue(QUEUE_NAME, true, false, false);
        var queue_cloud_watch = new Queue(QUEUE_CLOUD_WATCH_NAME, true, false, false);
        var queueError = new Queue(QUEUE_ERROR, true, false, false);


        amqpAdmin.declareExchange(exchange);


        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareQueue(queue_cloud_watch);
        amqpAdmin.declareQueue(queueError);


        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_NAME));
        amqpAdmin.declareBinding(BindingBuilder.bind(queue_cloud_watch).to(exchange).with(ROUTING_CLOUD_WATCH_KEY_NAME));
        amqpAdmin.declareBinding(BindingBuilder.bind(queueError).to(exchange).with(ROUTING_ERROR_KEY_NAME));

        return amqpAdmin;
    }


    @Bean
    public ConnectionFactory connectionFactory(URI uri) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.useNio();
        connectionFactory.setUri(uri);
        return connectionFactory;
    }

    @Bean
    public Mono<Connection> connectionMono(@Value("spring.application.name") String name, ConnectionFactory connectionFactory)  {
        return Mono.fromCallable(() -> connectionFactory.newConnection(name)).cache();
    }

    @Bean
    public SenderOptions senderOptions(Mono<Connection> connectionMono) {
        return new SenderOptions()
                .connectionMono(connectionMono)
                .resourceManagementScheduler(Schedulers.boundedElastic());
    }

    @Bean
    public Sender sender(SenderOptions senderOptions) {
        return RabbitFlux.createSender(senderOptions);
    }


    @Bean
    public ReceiverOptions receiverOptions(Mono<Connection> connectionMono) {
        return new ReceiverOptions()
                .connectionMono(connectionMono);
    }

    @Bean
    public Receiver receiver(ReceiverOptions receiverOptions) {
        return RabbitFlux.createReceiver(receiverOptions);
    }
}

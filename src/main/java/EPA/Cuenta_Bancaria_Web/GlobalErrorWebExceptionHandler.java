package EPA.Cuenta_Bancaria_Web;


import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
public class GlobalErrorWebExceptionHandler implements WebExceptionHandler {

    private final RabbitMqPublisher eventBus;

    public GlobalErrorWebExceptionHandler(RabbitMqPublisher eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        String errorMessage = "Ocurrió un error inesperado en la solicitud";
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);


        String errorJson = "{\"error\": \"" + errorMessage + "\"}";

        eventBus.publishMessage(errorMessage, ex.getMessage());

        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(errorJson.getBytes())));
    }
}

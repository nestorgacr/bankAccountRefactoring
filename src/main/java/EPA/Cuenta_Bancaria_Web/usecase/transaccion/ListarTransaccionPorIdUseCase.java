package EPA.Cuenta_Bancaria_Web.usecase.transaccion;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioTransaccionMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.TransaccionResponseDto;
import EPA.Cuenta_Bancaria_Web.utils.TransaccionUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;


@Service
public class ListarTransaccionPorIdUseCase implements Function<String, Mono<TransaccionResponseDto>> {

    private final I_RepositorioTransaccionMongo repositorio;


    private final RabbitMqPublisher eventBus;

    public ListarTransaccionPorIdUseCase(I_RepositorioTransaccionMongo repositorio, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<TransaccionResponseDto> apply(String id) {
        eventBus.publishCloudWatchMessage("Inicia busca transaccion", id);
        return repositorio.findById(id).map(TransaccionUtil::entityToDto).doOnTerminate(() -> {
            eventBus.publishCloudWatchMessage("Finaliza busca transaccion", id);
        });
    }
}

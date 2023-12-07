package EPA.Cuenta_Bancaria_Web.usecase.transaccion;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_Repositorio_TransaccionMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.TransaccionResponseDto;
import EPA.Cuenta_Bancaria_Web.utils.TransaccionUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;


@Service
public class ListarTransaccionPorIdUseCase implements Function<String, Mono<TransaccionResponseDto>> {

    private final I_Repositorio_TransaccionMongo repositorio;


    private final RabbitMqPublisher eventBus;

    public ListarTransaccionPorIdUseCase(I_Repositorio_TransaccionMongo repositorio, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<TransaccionResponseDto> apply(String id) {
        return repositorio.findById(id).map(TransaccionUtil::entityToDto);
    }
}

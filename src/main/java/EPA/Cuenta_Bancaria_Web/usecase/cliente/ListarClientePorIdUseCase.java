package EPA.Cuenta_Bancaria_Web.usecase.cliente;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioClienteMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.utils.ClienteUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
public class ListarClientePorIdUseCase implements Function<String, Mono<M_Cliente_DTO>> {
    private final I_RepositorioClienteMongo repositorio;


    private final RabbitMqPublisher eventBus;


    public ListarClientePorIdUseCase(I_RepositorioClienteMongo repositorio, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<M_Cliente_DTO> apply(String id) {
        eventBus.publishCloudWatchMessage("se busca el cliente con el id:" + id);
        return repositorio.findById(id).map(ClienteUtil::entityToDto);
    }
}

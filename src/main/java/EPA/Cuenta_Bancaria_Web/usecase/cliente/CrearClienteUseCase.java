package EPA.Cuenta_Bancaria_Web.usecase.cliente;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioClienteMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_ClienteMongo;
import EPA.Cuenta_Bancaria_Web.utils.ClienteUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
public class CrearClienteUseCase implements Function<M_Cliente_DTO, Mono<M_Cliente_DTO>> {
    private final I_RepositorioClienteMongo repositorio;


    private final RabbitMqPublisher eventBus;


    public CrearClienteUseCase(I_RepositorioClienteMongo repositorio, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<M_Cliente_DTO> apply(M_Cliente_DTO mClienteDto) {
        eventBus.publishCloudWatchMessage("Inicia la creacion del cliente", mClienteDto);
        M_ClienteMongo cliente = ClienteUtil.dtoToEntity(mClienteDto);
        return repositorio.save(cliente).map(ClienteUtil::entityToDto);
    }
}

package EPA.Cuenta_Bancaria_Web.usecase.cliente;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioClienteMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.utils.ClienteUtil;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

public class ListarClientesUseCase implements Supplier<Flux<M_Cliente_DTO>> {

    private final I_RepositorioClienteMongo repositorio;


    private final RabbitMqPublisher eventBus;


    public ListarClientesUseCase(I_RepositorioClienteMongo repositorio, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.eventBus = eventBus;
    }

    @Override
    public Flux<M_Cliente_DTO> get() {
        eventBus.publishCloudWatchMessage("Inicia creación de findAll clientes");
        return repositorio.findAll()
                .map(ClienteUtil::entityToDto);
    }
}

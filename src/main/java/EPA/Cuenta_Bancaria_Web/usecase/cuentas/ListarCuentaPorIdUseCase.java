package EPA.Cuenta_Bancaria_Web.usecase.cuentas;


import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
public class ListarCuentaPorIdUseCase implements Function<String, Mono<M_Cuenta_DTO>> {

    private final I_RepositorioCuentaMongo repositorio_Cuenta;


    private final RabbitMqPublisher eventBus;

    public ListarCuentaPorIdUseCase(I_RepositorioCuentaMongo repositorioCuenta, RabbitMqPublisher eventBus) {
        repositorio_Cuenta = repositorioCuenta;
        this.eventBus = eventBus;
    }
    @Override
    public Mono<M_Cuenta_DTO> apply(String id) {
        eventBus.publishCloudWatchMessage("Inicia creación de findById cuenta");
        return repositorio_Cuenta.findById(id)
                .map(cuentaModel -> new M_Cuenta_DTO(cuentaModel.getId(),
                        new M_Cliente_DTO(cuentaModel.getCliente().getId(),
                                cuentaModel.getCliente().getNombre()),
                        cuentaModel.getSaldo_Global()));
    }
}

package EPA.Cuenta_Bancaria_Web.usecase.cuentas;


import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Service
public class ListarCuentasUseCase implements Supplier<Flux<M_Cuenta_DTO>> {

    private final I_RepositorioCuentaMongo repositorio_Cuenta;


    private final RabbitMqPublisher eventBus;

    public ListarCuentasUseCase(I_RepositorioCuentaMongo repositorioCuenta, RabbitMqPublisher eventBus) {
        repositorio_Cuenta = repositorioCuenta;
        this.eventBus = eventBus;
    }


    @Override
    public Flux<M_Cuenta_DTO> get() {
        eventBus.publishCloudWatchMessage("Inicia creación de findAll cuentas");
        return repositorio_Cuenta.findAll()
                .map(cuentaModel -> new M_Cuenta_DTO(cuentaModel.getId(),
                        new M_Cliente_DTO(cuentaModel.getCliente().getId(),
                                cuentaModel.getCliente().getNombre()),
                        cuentaModel.getSaldo_Global()));
    }
}

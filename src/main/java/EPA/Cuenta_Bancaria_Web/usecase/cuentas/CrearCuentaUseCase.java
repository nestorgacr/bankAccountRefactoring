package EPA.Cuenta_Bancaria_Web.usecase.cuentas;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_ClienteMongo;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_CuentaMongo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;


@Service
public class CrearCuentaUseCase implements Function<M_Cuenta_DTO, Mono<M_Cuenta_DTO>> {
    private final I_RepositorioCuentaMongo repositorio_Cuenta;


    private final RabbitMqPublisher eventBus;

    public CrearCuentaUseCase(I_RepositorioCuentaMongo repositorio_Cuenta, RabbitMqPublisher eventBus) {
        this.repositorio_Cuenta = repositorio_Cuenta;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<M_Cuenta_DTO> apply(M_Cuenta_DTO p_Cuenta_DTO) {
        eventBus.publishCloudWatchMessage("Inicia creación de cuenta", p_Cuenta_DTO);
        M_CuentaMongo cuenta = new M_CuentaMongo(p_Cuenta_DTO.getId(),
                new M_ClienteMongo(p_Cuenta_DTO.getCliente().getId(),
                        p_Cuenta_DTO.getCliente().getNombre()),
                p_Cuenta_DTO.getSaldo_Global());

        return repositorio_Cuenta.save(cuenta)
                .map(cuentaModel-> {
                    //eventBus.publishCloudWatchMessage("Finaliza creación de cuenta", cuentaModel);
                    return new M_Cuenta_DTO(cuentaModel.getId(),
                            new M_Cliente_DTO(cuentaModel.getCliente().getId(),
                                    cuentaModel.getCliente().getNombre()),
                            cuentaModel.getSaldo_Global());
                });
    }
}

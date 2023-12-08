package EPA.Cuenta_Bancaria_Web.usecase.transaccion;


import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_Repositorio_TransaccionMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Transaccion_DTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Service
public class ListarTransaccionesUseCase implements Supplier<Flux<M_Transaccion_DTO>> {

    private final I_Repositorio_TransaccionMongo repositorio;


    private final RabbitMqPublisher eventBus;

    public ListarTransaccionesUseCase(I_Repositorio_TransaccionMongo repositorio, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.eventBus = eventBus;
    }


    @Override
    public Flux<M_Transaccion_DTO> get() {
        eventBus.publishCloudWatchMessage("Inicia transacción findAll", "");
        return repositorio.findAll()
                .map(transaccion -> {
                    return new M_Transaccion_DTO(transaccion.getId(),
                            new M_Cuenta_DTO(transaccion.getCuenta().getId(),
                                    new M_Cliente_DTO(transaccion.getCuenta().getCliente().getId(),
                                            transaccion.getCuenta().getCliente().getNombre()
                                    ),
                                    transaccion.getCuenta().getSaldo_Global()
                            ),
                            transaccion.getMonto_transaccion(),
                            transaccion.getSaldo_inicial(),
                            transaccion.getSaldo_final(),
                            transaccion.getCosto_tansaccion(),
                            transaccion.getTipo(),
                            transaccion.getIdProceso()
                    );
                });
    }
}

package EPA.Cuenta_Bancaria_Web.usecase.transaccion;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioTransaccionMongo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
public class BorrarTransaccionPorIdProcesoUseCase implements Function<String, Mono<Void>> {

    private final I_RepositorioTransaccionMongo repositorio;


    private final RabbitMqPublisher eventBus;

    public BorrarTransaccionPorIdProcesoUseCase(I_RepositorioTransaccionMongo repositorio, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<Void> apply(String idProceso) {
        return repositorio.deleteByIdProceso(idProceso).doOnTerminate(() -> {
            eventBus.publishCloudWatchMessage("Finaliza el borrado de la transaccion", idProceso);
        });
    }
}

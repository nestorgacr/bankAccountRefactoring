package EPA.Cuenta_Bancaria_Web.usecase.cuentas;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.ActualizarSaldoDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;


@Service
public class ActualizarSaldoCuentaUseCase implements Function<ActualizarSaldoDto, Mono<Void>>  {

    private final I_RepositorioCuentaMongo repositorio_Cuenta;


    private final RabbitMqPublisher eventBus;

    public ActualizarSaldoCuentaUseCase(I_RepositorioCuentaMongo repositorioCuenta, RabbitMqPublisher eventBus) {
        repositorio_Cuenta = repositorioCuenta;
        this.eventBus = eventBus;
    }


    @Override
    public Mono<Void> apply(ActualizarSaldoDto actualizarSaldoDto) {
        eventBus.publishCloudWatchMessage("Se actualiza saldo de cuenta", actualizarSaldoDto);

        return repositorio_Cuenta.findById(actualizarSaldoDto.getId_Cuenta())
                .flatMap(cuenta -> {
                    cuenta.setSaldo_Global(actualizarSaldoDto.getMonto());
                    return repositorio_Cuenta.save(cuenta);
                })
                .then().doOnTerminate(() -> {
                    eventBus.publishCloudWatchMessage("Finaliza actualizacion de cuenta", actualizarSaldoDto);
                });
    }
}

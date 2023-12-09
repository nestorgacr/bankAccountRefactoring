package EPA.Cuenta_Bancaria_Web.handlers.bus;


import EPA.Cuenta_Bancaria_Web.RabbitConfig;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.models.DTO.ActualizarSaldoDto;
import EPA.Cuenta_Bancaria_Web.models.DTO.ErrorTransaccion;
import EPA.Cuenta_Bancaria_Web.models.DTO.EventosDto;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ActualizarSaldoCuentaUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.BorrarTransaccionPorIdProcesoUseCase;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.Receiver;


@Component
public class RabbitMqMessageConsumer implements CommandLineRunner {

    @Autowired
    private Receiver receiver;

    @Autowired
    private Gson gson;

    private final BorrarTransaccionPorIdProcesoUseCase borrarTransaccionPorIdProcesoUseCase;
    private final ActualizarSaldoCuentaUseCase actualizarSaldoCuentaUseCase;

    private final RabbitMqPublisher eventBus;

    public RabbitMqMessageConsumer(BorrarTransaccionPorIdProcesoUseCase borrarTransaccionPorIdProcesoUseCase, ActualizarSaldoCuentaUseCase actualizarSaldoCuentaUseCase, RabbitMqPublisher eventBus) {
        this.borrarTransaccionPorIdProcesoUseCase = borrarTransaccionPorIdProcesoUseCase;
        this.actualizarSaldoCuentaUseCase = actualizarSaldoCuentaUseCase;
        this.eventBus = eventBus;
    }


    @Override
    public void run(String... args) throws Exception {


        receiver.consumeAutoAck(RabbitConfig.QUEUE_ERROR)
                .map(message -> {

                    ErrorTransaccion transaction = gson.fromJson(new String(message.getBody()), ErrorTransaccion.class);

                    eventBus.publishMessage("Inicio rollback de la transaccion", transaction);
                    borrarTransaccionPorIdProcesoUseCase.apply(transaction.getTransaccion().getIdProceso()).subscribe();

                    //actualizar cuenta
                    ActualizarSaldoDto saldo = new ActualizarSaldoDto();
                    saldo.setMonto(transaction.getTransaccion().getSaldo_inicial());
                    saldo.setId_Cuenta(transaction.getTransaccion().getCuenta().getId());
                    actualizarSaldoCuentaUseCase.apply(saldo).subscribe();
                    eventBus.publishMessage("Finaliza rollback", "");

                    return transaction;
                }).subscribe();
   }
}

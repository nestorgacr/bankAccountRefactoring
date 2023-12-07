package EPA.Cuenta_Bancaria_Web.handlers.bus;


import EPA.Cuenta_Bancaria_Web.RabbitConfig;
import EPA.Cuenta_Bancaria_Web.models.DTO.ErrorTransaccion;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.services.Cuenta.I_Cuenta;
import EPA.Cuenta_Bancaria_Web.services.Transaccion.I_Transaccion;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.Receiver;

import java.util.Arrays;

@Component
public class RabbitMqMessageConsumer implements CommandLineRunner {

    @Autowired
    private Receiver receiver;

    @Autowired
    private Gson gson;

    @Autowired
    private I_Transaccion transaccionServ;

    @Autowired
    private I_Cuenta cuentaServ;


    @Override
    public void run(String... args) throws Exception {
        receiver.consumeAutoAck(RabbitConfig.QUEUE_NAME)
                .map(message -> {
                   M_Cuenta_DTO transaction = gson
                           .fromJson(new String(message.getBody()),
                                   M_Cuenta_DTO.class);

                    System.out.println("La cuenta creada fue:  " + transaction);
                    return transaction;
                }).subscribe();



        receiver.consumeAutoAck(RabbitConfig.QUEUE_CLOUD_WATCH_NAME)
                .map(message -> {
                    String mensaje = new String(message.getBody());
                    System.out.println("CloudWatch:" + mensaje);
                    return mensaje;
                })
                .subscribe();




        receiver.consumeAutoAck(RabbitConfig.QUEUE_ERROR)
                .map(message -> {
                    System.out.println("Mensaje recibido: " + new String(message.getBody()));

                    ErrorTransaccion transaction = gson.fromJson(new String(message.getBody()), ErrorTransaccion.class);

                    //se borra la transaccion

                    transaccionServ.BorrarTransaccion(transaction.getTransaccion().getIdProceso()).subscribe();


                    //actualizar cuenta
                    cuentaServ.Actualizar_Saldo(transaction.getTransaccion().getCuenta().getId(),
                            transaction.getTransaccion().getSaldo_inicial()).subscribe();


                    System.out.println("La cuenta creada fue:  " + transaction);
                    return transaction;
                }).subscribe();
   }
}

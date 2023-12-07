package EPA.Cuenta_Bancaria_Web.services.Cuenta;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_ClienteMongo;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_CuentaMongo;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


@Service()
@Qualifier("MONGO")
public class Cuenta_ImpMongo implements I_Cuenta
{
    @Autowired
    I_RepositorioCuentaMongo repositorio_Cuenta;

    @Autowired
    private RabbitMqPublisher eventBus;



    @Override
    public Mono<M_Cuenta_DTO> crear_Cuenta(M_Cuenta_DTO p_Cuenta_DTO)
    {
        eventBus.publishCloudWatchMessage("Inicia creación de cuenta");
        M_CuentaMongo cuenta = new M_CuentaMongo(p_Cuenta_DTO.getId(),
                new M_ClienteMongo(p_Cuenta_DTO.getCliente().getId(),
                        p_Cuenta_DTO.getCliente().getNombre()),
                p_Cuenta_DTO.getSaldo_Global());


        eventBus.publishMessage(cuenta);

        return repositorio_Cuenta.save(cuenta)
                .map(cuentaModel-> {
                    eventBus.publishCloudWatchMessage("Finaliza creación de cuenta");
                    return new M_Cuenta_DTO(cuentaModel.getId(),
                            new M_Cliente_DTO(cuentaModel.getCliente().getId(),
                                    cuentaModel.getCliente().getNombre()),
                            cuentaModel.getSaldo_Global());
                });
    }

    @Override
    public Flux<M_Cuenta_DTO> findAll()
    {
        eventBus.publishCloudWatchMessage("Inicia creación de findAll cuentas");
        return repositorio_Cuenta.findAll()
                .map(cuentaModel -> new M_Cuenta_DTO(cuentaModel.getId(),
                        new M_Cliente_DTO(cuentaModel.getCliente().getId(),
                                cuentaModel.getCliente().getNombre()),
                        cuentaModel.getSaldo_Global()));
    }


    @Override
    public Mono<Void> Actualizar_Saldo(String id_Cuenta, BigDecimal monto) {
        eventBus.publishCloudWatchMessage("Se actualiza saldo de cuenta:" + id_Cuenta.toString() + ", monto:" + monto.toString());

        return repositorio_Cuenta.findById(id_Cuenta)
                .flatMap(cuenta -> {
                    cuenta.setSaldo_Global(monto);
                    return repositorio_Cuenta.save(cuenta);
                })
                .then();
    }

}
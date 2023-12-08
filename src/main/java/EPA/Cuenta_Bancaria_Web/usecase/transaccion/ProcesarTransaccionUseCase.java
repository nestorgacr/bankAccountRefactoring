package EPA.Cuenta_Bancaria_Web.usecase.transaccion;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_Repositorio_TransaccionMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.*;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_TransaccionMongo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Function;


@Service
public class ProcesarTransaccionUseCase implements Function<ProcesarTransaccionDto, Mono<M_Transaccion_DTO>> {

    private final I_Repositorio_TransaccionMongo repositorio;
    private final I_RepositorioCuentaMongo repositorioCuenta;
    private final Environment environment;

    @Value("${EPA.Deposito.Cajero}")
    String costoCajeroStr;

    @Value("${EPA.Deposito.Sucursal}")
    String costoSucursalStr;

    @Value("${EPA.Deposito.OtraCuenta}")
    String costoOtraCuentaStr;

    @Value("${EPA.Compra.Fisica}")

    String costoCompraFisicaStr;

    @Value("${EPA.Compra.Web}")
    String costoCompraWebStr;

    @Value("${EPA.Retiro}")

    String costoRetiroStr;


    private final RabbitMqPublisher eventBus;

    public ProcesarTransaccionUseCase(I_Repositorio_TransaccionMongo repositorio,
                                      I_RepositorioCuentaMongo repositorioCuenta,
                                      Environment environment, RabbitMqPublisher eventBus) {
        this.repositorio = repositorio;
        this.repositorioCuenta = repositorioCuenta;
        this.environment = environment;
        this.eventBus = eventBus;
    }

    @Override
    public Mono<M_Transaccion_DTO> apply(ProcesarTransaccionDto procesarTransaccionDto) {




        double costoCajero = Double.parseDouble(this.costoCajeroStr);
        double costoSucursal = Double.parseDouble(costoSucursalStr);
        double costoOtraCuenta = Double.parseDouble(costoOtraCuentaStr);

        double costoCompraFisica = Double.parseDouble(costoCompraFisicaStr);
        double costoCompraWeb = Double.parseDouble(costoCompraWebStr);

        double costoRetiro = Double.parseDouble(costoRetiroStr);


        eventBus.publishCloudWatchMessage("Inicia creación de transaccion", procesarTransaccionDto);

        return repositorioCuenta.findById(procesarTransaccionDto.getId_Cuenta())
                .flatMap(cuenta -> {
                    BigDecimal costo = switch (procesarTransaccionDto.getTipo()) {
                        case CAJERO -> BigDecimal.valueOf(costoCajero);
                        case SUCURSAL -> BigDecimal.valueOf(costoSucursal);
                        case OTRA_CUENTA -> BigDecimal.valueOf(costoOtraCuenta);
                    };
                    BigDecimal bdSaldoActual = cuenta.getSaldo_Global();
                    BigDecimal bdSaldoNuevo  = cuenta.getSaldo_Global().add(procesarTransaccionDto.getMonto().subtract(costo));
                    cuenta.setSaldo_Global(bdSaldoNuevo);
                    ErrorTransaccion errorSend = new ErrorTransaccion();
                    UUID uuid = UUID.randomUUID();


                    M_TransaccionMongo transaccion = new M_TransaccionMongo(
                            cuenta,
                            procesarTransaccionDto.getMonto(),
                            bdSaldoActual,
                            bdSaldoNuevo,
                            costo,
                            procesarTransaccionDto.getTipo().toString()
                    );
                    transaccion.setIdProceso(uuid.toString());
                    repositorioCuenta.save(cuenta)
                            //.flatMap(cuentaCreada -> Mono.error(new RuntimeException("Mensaje de pueba")))
                            .onErrorResume(error -> {
                                System.out.println("El error fue: " + error.getMessage());
                                /**
                                 * Crear objeto de resiliencia que contenga la cuenta y la
                                 * transacción para enviar a una nueva cola de Rabbit con un
                                 * nuevo routing key.
                                 *
                                 * Crear un handler que consuma esa cola y borrar los registros ya guardados
                                 * */
                                //BigDecimal monto_transaccion, BigDecimal saldo_inicial, BigDecimal saldo_final, BigDecimal costo_tansaccion, String tipo, String idProceso
                                M_Transaccion_DTO mTran = new M_Transaccion_DTO(
                                        transaccion.getId(),
                                        new M_Cuenta_DTO(transaccion.getCuenta().getId(),null, null),
                                        transaccion.getMonto_transaccion(),
                                        transaccion.getSaldo_inicial(),
                                        transaccion.getSaldo_final(),
                                        transaccion.getCosto_tansaccion(),
                                        transaccion.getTipo(),
                                        transaccion.getIdProceso());

                                errorSend.setTransaccion(mTran);

                                eventBus.publishErrorMessage("Error al procesar transaccion",errorSend);


                                return Mono.empty();
                            })
                            .subscribe();
                    return repositorio.save(transaccion);
                }).map(transactionModel -> {
                    eventBus.publishCloudWatchMessage("Finaliza creación de transaccion", transactionModel);
                    return new M_Transaccion_DTO(transactionModel.getId(),
                            new M_Cuenta_DTO(transactionModel.getCuenta().getId(),
                                    new M_Cliente_DTO(transactionModel.getCuenta().getCliente().getId(),
                                            transactionModel.getCuenta().getCliente().getNombre()
                                    ),
                                    transactionModel.getCuenta().getSaldo_Global()
                            ), transactionModel.getMonto_transaccion(),
                            transactionModel.getSaldo_inicial(),
                            transactionModel.getSaldo_final(),
                            transactionModel.getCosto_tansaccion(),
                            transactionModel.getTipo(),
                            transactionModel.getIdProceso()
                    );
                });

    }
}

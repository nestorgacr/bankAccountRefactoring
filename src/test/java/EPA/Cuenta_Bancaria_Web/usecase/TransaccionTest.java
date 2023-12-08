package EPA.Cuenta_Bancaria_Web.usecase;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioTransaccionMongo;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_ClienteMongo;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_CuentaMongo;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_TransaccionMongo;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ActualizarSaldoCuentaUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.CrearCuentaUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ListarCuentaPorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ListarCuentasUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.BorrarTransaccionPorIdProcesoUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.ListarTransaccionPorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.ListarTransaccionesUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.ProcesarTransaccionUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TransaccionTest {


    @Mock
    private I_RepositorioTransaccionMongo repository;

    @Mock
    private RabbitMqPublisher rabbitMqPublisher;


    @InjectMocks
    private BorrarTransaccionPorIdProcesoUseCase borrarTransaccionPorIdProcesoUseCase;

    @InjectMocks
    private ListarTransaccionesUseCase listarTransaccionesUseCase;

    @InjectMocks
    private ListarTransaccionPorIdUseCase listarTransaccionPorIdUseCase;

    @InjectMocks
    private ProcesarTransaccionUseCase procesarTransaccionUseCase;



    @BeforeEach
    public void setup() {
    }

    @Test
    @DisplayName("Usecase -> Listar transaccion")
    public void ListarTransaccion() {

        M_CuentaMongo cuentaMongo = new M_CuentaMongo();
        cuentaMongo.setSaldo_Global(BigDecimal.valueOf(1000));
        cuentaMongo.setId("1");
        cuentaMongo.setCliente( new M_ClienteMongo("1", "Test"));

        M_TransaccionMongo transaccionMongo = new M_TransaccionMongo();
        transaccionMongo.setIdProceso("1");
        transaccionMongo.setId("1");
        transaccionMongo.setCuenta( cuentaMongo);


        when(repository.findAll()).thenReturn(Flux.just(transaccionMongo));

        StepVerifier.create(listarTransaccionesUseCase.get())
                .expectNextMatches(transaccion -> transaccion.getIdProceso().equals("1"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Usecase -> Listar transaccion por id")
    public void ListarTransaccionPorId() {

        M_CuentaMongo cuentaMongo = new M_CuentaMongo();
        cuentaMongo.setSaldo_Global(BigDecimal.valueOf(1000));
        cuentaMongo.setId("1");
        cuentaMongo.setCliente( new M_ClienteMongo("1", "Test"));

        M_TransaccionMongo transaccionMongo = new M_TransaccionMongo();
        transaccionMongo.setIdProceso("1");
        transaccionMongo.setId("1");
        transaccionMongo.setCuenta( cuentaMongo);


        when(repository.findById("1")).thenReturn(Mono.just(transaccionMongo));

        StepVerifier.create(listarTransaccionPorIdUseCase.apply("1"))
                .expectNextMatches(transaccion -> transaccion.getIdProceso().equals("1"))
                .verifyComplete();
    }





}

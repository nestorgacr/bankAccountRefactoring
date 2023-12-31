package EPA.Cuenta_Bancaria_Web.usecase;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioClienteMongo;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_ClienteMongo;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_CuentaMongo;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.CrearClienteUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientePorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientesUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ActualizarSaldoCuentaUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.CrearCuentaUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ListarCuentaPorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ListarCuentasUseCase;
import EPA.Cuenta_Bancaria_Web.utils.ClienteUtil;
import EPA.Cuenta_Bancaria_Web.utils.CuentaUtil;
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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CuentaTest {


    @Mock
    private I_RepositorioCuentaMongo repository;

    @Mock
    private RabbitMqPublisher rabbitMqPublisher;


    @InjectMocks
    private CrearCuentaUseCase crearCuentaUseCase;

    @InjectMocks
    private ListarCuentasUseCase listarCuentasUseCase;

    @InjectMocks
    private ListarCuentaPorIdUseCase listarCuentaPorIdUseCase;

    @InjectMocks
    private ActualizarSaldoCuentaUseCase actualizarSaldoCuentaUseCase;



    @BeforeEach
    public void setup() {
    }

    @Test
    @DisplayName("Usecase -> Listar cuentas")
    public void ListarCuenta() {

        M_CuentaMongo cuentaMongo = new M_CuentaMongo();
        cuentaMongo.setSaldo_Global(BigDecimal.valueOf(1000));
        cuentaMongo.setId("1");
        cuentaMongo.setCliente( new M_ClienteMongo("1", "Test"));

        when(repository.findAll()).thenReturn(Flux.just(cuentaMongo));

        StepVerifier.create(listarCuentasUseCase.get())
                .expectNextMatches(cuenta -> cuenta.getId().equals("1") &&
                        cuenta.getSaldo_Global().equals(BigDecimal.valueOf(1000)))
                .verifyComplete();
    }

    @Test
    @DisplayName("Usecase -> Listar cuenta por id")
    public void ListarCuentaPorId() {

        M_CuentaMongo cuentaMongo = new M_CuentaMongo();
        cuentaMongo.setSaldo_Global(BigDecimal.valueOf(1000));
        cuentaMongo.setId("1");
        cuentaMongo.setCliente( new M_ClienteMongo("1", "Test"));

        when(repository.findById("1")).thenReturn(Mono.just(cuentaMongo));

        StepVerifier.create(listarCuentaPorIdUseCase.apply("1"))
                .expectNextMatches(cuenta -> cuenta.getId().equals("1") &&
                        cuenta.getSaldo_Global().equals(BigDecimal.valueOf(1000)))
                .verifyComplete();
    }

//    @Test
//    @DisplayName("Usecase -> Crear cliente")
//    public void CrearCliente() {
//
//        M_ClienteMongo clienteMongo = new M_ClienteMongo("1", "Cliente1");
//
//        M_CuentaMongo cuentaMongo = new M_CuentaMongo();
//        cuentaMongo.setSaldo_Global(BigDecimal.valueOf(1000));
//        cuentaMongo.setId("1");
//        cuentaMongo.setCliente( new M_ClienteMongo("1", "Test"));
//        cuentaMongo.setCliente(clienteMongo);
//
//        M_Cliente_DTO clienteDto = new M_Cliente_DTO ("1","Cliente");
//        M_Cuenta_DTO cuentaDto = new M_Cuenta_DTO();
//        cuentaDto.setCliente(clienteDto);
//        clienteDto.setId("1");
//        clienteDto.setNombre("nombre");
//
//
//        when(repository.save(cuentaMongo))
//                .thenReturn(Mono.just(cuentaMongo));
//
//        doNothing().when(rabbitMqPublisher).publishCloudWatchMessage("Inicia creación de cuenta",cuentaDto);
//
//
//        StepVerifier.create(crearCuentaUseCase.apply(cuentaDto))
//                .expectNext(cuentaDto)
//                .verifyComplete();
//    }




}

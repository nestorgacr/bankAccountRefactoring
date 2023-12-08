package EPA.Cuenta_Bancaria_Web.routes;

import EPA.Cuenta_Bancaria_Web.handlers.ClienteHandler;
import EPA.Cuenta_Bancaria_Web.handlers.TransaccionHandler;
import EPA.Cuenta_Bancaria_Web.models.DTO.*;
import EPA.Cuenta_Bancaria_Web.models.Enum_Tipos_Deposito;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.CrearClienteUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientePorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientesUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.BorrarTransaccionPorIdProcesoUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.ListarTransaccionPorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.ListarTransaccionesUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.ProcesarTransaccionUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransaccionRoutesTest {
    private WebTestClient webTestClient;

    @MockBean
    private BorrarTransaccionPorIdProcesoUseCase borrarTransaccionPorIdProcesoUseCase;

    @MockBean
    private ListarTransaccionesUseCase listarTransaccionesUseCase;

    @MockBean
    private ListarTransaccionPorIdUseCase listarTransaccionPorIdUseCase;

    @MockBean
    private ProcesarTransaccionUseCase procesarTransaccionUseCase;

    @BeforeEach
    void setUp(){

        borrarTransaccionPorIdProcesoUseCase = Mockito.mock(BorrarTransaccionPorIdProcesoUseCase.class);

        listarTransaccionesUseCase = Mockito.mock(ListarTransaccionesUseCase.class);

        listarTransaccionPorIdUseCase = Mockito.mock(ListarTransaccionPorIdUseCase.class);

        procesarTransaccionUseCase = Mockito.mock(ProcesarTransaccionUseCase.class);

        webTestClient = WebTestClient.bindToRouterFunction(new TransaccionRouter(new TransaccionHandler(borrarTransaccionPorIdProcesoUseCase,listarTransaccionesUseCase,procesarTransaccionUseCase, listarTransaccionPorIdUseCase  )).routerFunctionTransaccion())
                .build();
    }

    @Test
    @DisplayName("Listar transacciones")
    void listarTransacciones() {


        M_Cuenta_DTO cuenta = new M_Cuenta_DTO();
        cuenta.setSaldo_Global(BigDecimal.valueOf(1000));
        cuenta.setCliente( new M_Cliente_DTO("1", "Test"));
        cuenta.setId("1");


        M_Transaccion_DTO tran = new M_Transaccion_DTO();
        tran.setId("1");
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setCuenta(cuenta);
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setTipo("test");
        tran.setSaldo_final(BigDecimal.valueOf(100));
        tran.setCosto_tansaccion(BigDecimal.valueOf(100));
        tran.setSaldo_inicial(BigDecimal.valueOf(100));


        when(listarTransaccionesUseCase.get()).thenReturn(Flux.just(tran));

        webTestClient.get()
                .uri("/Transacciones/listar")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.valueOf("text/event-stream;charset=UTF-8"))
                .expectBodyList(M_Transaccion_DTO.class)
                .hasSize(1)
                .contains(tran);
    }

    @Test
    @DisplayName("Listar transacciones con errores")
    void listarTransaccionesWithError() {


        M_Cuenta_DTO cuenta = new M_Cuenta_DTO();
        cuenta.setSaldo_Global(BigDecimal.valueOf(1000));
        cuenta.setCliente( new M_Cliente_DTO("1", "Test"));
        cuenta.setId("1");


        M_Transaccion_DTO tran = new M_Transaccion_DTO();
        tran.setId("1");
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setCuenta(cuenta);
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setTipo("test");
        tran.setSaldo_final(BigDecimal.valueOf(100));
        tran.setCosto_tansaccion(BigDecimal.valueOf(100));
        tran.setSaldo_inicial(BigDecimal.valueOf(100));

        M_Transaccion_DTO tranResult = new M_Transaccion_DTO();
        tran.setId("2");
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setCuenta(cuenta);
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setTipo("test 1");
        tran.setSaldo_final(BigDecimal.valueOf(100));
        tran.setCosto_tansaccion(BigDecimal.valueOf(100));
        tran.setSaldo_inicial(BigDecimal.valueOf(100));


        when(listarTransaccionesUseCase.get()).thenReturn(Flux.just(tran));

        webTestClient.get()
                .uri("/Transacciones/listar")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.valueOf("text/event-stream;charset=UTF-8"))
                .expectBodyList(M_Transaccion_DTO.class)
                .hasSize(1)
                .doesNotContain(tranResult);
    }

    @Test
    @DisplayName("Listar transaccion por id")
    void listarTransaccionPorId() {

        TransaccionResponseDto tran = new TransaccionResponseDto();
        tran.setId("1");
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setTipo("test");
        tran.setSaldo_final(BigDecimal.valueOf(100));
        tran.setSaldo_inicial(BigDecimal.valueOf(100));

        when(listarTransaccionPorIdUseCase.apply("1")).thenReturn(Mono.just(tran));


        webTestClient.get()
                .uri("/Transacciones/listar/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransaccionResponseDto.class)
                .isEqualTo(tran);

    }

    @Test
    @DisplayName("Listar transaccion por id, con error")
    void listarTransaccionPorIdWithError() {

        TransaccionResponseDto tran = new TransaccionResponseDto();
        tran.setId("1");
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setTipo("test");
        tran.setSaldo_final(BigDecimal.valueOf(100));
        tran.setSaldo_inicial(BigDecimal.valueOf(100));

        TransaccionResponseDto tranResult = new TransaccionResponseDto();
        tran.setId("2");
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setTipo("test a");
        tran.setSaldo_final(BigDecimal.valueOf(100));
        tran.setSaldo_inicial(BigDecimal.valueOf(100));

        when(listarTransaccionPorIdUseCase.apply("1")).thenReturn(Mono.just(tran));


        webTestClient.get()
                .uri("/Transacciones/listar/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransaccionResponseDto.class)
                .value(tranDTO -> {
                    assertNotEquals(tranResult, tranDTO);
                });

    }

    @Test
    @DisplayName("Transaccion, crear deposito a cajero")
    void CrearDeposito() {

        M_Cuenta_DTO cuenta = new M_Cuenta_DTO();
        cuenta.setSaldo_Global(BigDecimal.valueOf(1000));
        cuenta.setCliente( new M_Cliente_DTO("1", "Test"));
        cuenta.setId("1");


        M_Transaccion_DTO tran = new M_Transaccion_DTO();
        tran.setId("1");
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setCuenta(cuenta);
        tran.setMonto_transaccion(BigDecimal.valueOf(100));
        tran.setTipo("test");
        tran.setSaldo_final(BigDecimal.valueOf(100));
        tran.setCosto_tansaccion(BigDecimal.valueOf(100));
        tran.setSaldo_inicial(BigDecimal.valueOf(100));

        ProcesarTransaccionDto proceso = new ProcesarTransaccionDto();
        proceso.setTipo( Enum_Tipos_Deposito.CAJERO);
        proceso.setMonto(BigDecimal.valueOf(100));
        proceso.setId_Cuenta("1");

        when(procesarTransaccionUseCase.apply(proceso)).thenReturn(Mono.just(tran));


        webTestClient.get()
                .uri("/Transacciones/Crear/Deposito/Cajero/1/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(M_Transaccion_DTO.class)
                .isEqualTo(tran);

    }
}

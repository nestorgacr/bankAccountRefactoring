package EPA.Cuenta_Bancaria_Web.routes;

import EPA.Cuenta_Bancaria_Web.handlers.TransaccionHandler;
import EPA.Cuenta_Bancaria_Web.models.DTO.*;
import EPA.Cuenta_Bancaria_Web.models.Enum_Tipos_Deposito;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Mock
    private BorrarTransaccionPorIdProcesoUseCase borrarTransaccionPorIdProcesoUseCase;

    @Mock
    private ListarTransaccionesUseCase listarTransaccionesUseCase;

    @Mock
    private ListarTransaccionPorIdUseCase listarTransaccionPorIdUseCase;

    @Mock
    private ProcesarTransaccionUseCase procesarTransaccionUseCase;

    @Mock
    private ProcesarTransaccionErrorUseCase procesarTransaccionErrorUseCase;

    private TransaccionHandler transaccionHandler;

    private TransaccionRouter transaccionRouter;

    @BeforeEach
    void setUp(){

        transaccionHandler = new TransaccionHandler(  borrarTransaccionPorIdProcesoUseCase, listarTransaccionesUseCase,  procesarTransaccionUseCase, listarTransaccionPorIdUseCase, procesarTransaccionErrorUseCase);

        transaccionRouter = new TransaccionRouter(transaccionHandler);

        webTestClient = WebTestClient.bindToRouterFunction(transaccionRouter.routerFunctionTransaccion())
                .build();
    }

    @Test
    @DisplayName("Routes -> Listar transacciones")
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
    @DisplayName("Routes -> Listar transacciones con errores")
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
    @DisplayName("Routes -> Listar transaccion por id")
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
    @DisplayName("Routes -> Listar transaccion por id, con error")
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
    @DisplayName("Routes -> Transaccion, crear deposito a cajero")
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


        webTestClient.post()
                .uri("/Transacciones/Crear/Deposito/Cajero/1/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(M_Transaccion_DTO.class)
                .isEqualTo(tran);

    }

    @Test
    @DisplayName("Routes -> Transaccion, crear deposito a sucursal")
    void CrearDepositoSucursal() {

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
        proceso.setTipo( Enum_Tipos_Deposito.SUCURSAL);
        proceso.setMonto(BigDecimal.valueOf(100));
        proceso.setId_Cuenta("1");

        when(procesarTransaccionUseCase.apply(proceso)).thenReturn(Mono.just(tran));


        webTestClient.post()
                .uri("/Transacciones/Crear/Deposito/Sucursal/1/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(M_Transaccion_DTO.class)
                .isEqualTo(tran);

    }

//    @Test
//    @DisplayName("Routes -> Transaccion, crear deposito a otras cuentas")
//    void CrearDepositoOtrasCuentas() {
//
//        M_Cuenta_DTO cuenta = new M_Cuenta_DTO();
//        cuenta.setSaldo_Global(BigDecimal.valueOf(1000));
//        cuenta.setCliente( new M_Cliente_DTO("1", "Test"));
//        cuenta.setId("1");
//
//
//        M_Transaccion_DTO tran = new M_Transaccion_DTO();
//        tran.setId("1");
//        tran.setMonto_transaccion(BigDecimal.valueOf(100));
//        tran.setCuenta(cuenta);
//        tran.setMonto_transaccion(BigDecimal.valueOf(100));
//        tran.setTipo("test");
//        tran.setSaldo_final(BigDecimal.valueOf(100));
//        tran.setCosto_tansaccion(BigDecimal.valueOf(100));
//        tran.setSaldo_inicial(BigDecimal.valueOf(100));
//
//        ProcesarTransaccionDto proceso = new ProcesarTransaccionDto();
//        proceso.setTipo( Enum_Tipos_Deposito.OTRA_CUENTA);
//        proceso.setMonto(BigDecimal.valueOf(100));
//        proceso.setId_Cuenta("1");
//
//        when(procesarTransaccionUseCase.apply(proceso)).thenReturn(Mono.just(tran));
//
//
//        webTestClient.post()
//                .uri("/Transacciones/Crear/Deposito/OtraCuenta/1/100")
//                .exchange()
//                .expectStatus().is5xxServerError()
//                .expectBody(M_Transaccion_DTO.class)
//                .isEqualTo(tran);
//
//    }
}

package EPA.Cuenta_Bancaria_Web.routes;

import EPA.Cuenta_Bancaria_Web.handlers.ClienteHandler;
import EPA.Cuenta_Bancaria_Web.handlers.CuentaHandler;
import EPA.Cuenta_Bancaria_Web.models.DTO.ActualizarSaldoDto;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.CrearClienteUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientePorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientesUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ActualizarSaldoCuentaUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.CrearCuentaUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ListarCuentaPorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ListarCuentasUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
public class CuentaRoutesTest {

    private WebTestClient webTestClient;

    @Mock
    private CrearCuentaUseCase crearCuentaUseCase;

    @Mock
    private ListarCuentasUseCase listarCuentasUseCase;

    @Mock
    private ListarCuentaPorIdUseCase listarCuentaPorIdUseCase;

    private CuentaHandler cuentaHandler;

    private CuentaRouter cuentaRouter;

    @BeforeEach
    void setUp(){

        cuentaHandler = new CuentaHandler(  listarCuentasUseCase, listarCuentaPorIdUseCase,  crearCuentaUseCase);

        cuentaRouter = new CuentaRouter(cuentaHandler);

        webTestClient = WebTestClient.bindToRouterFunction(cuentaRouter.routerFunctionCuentas())
                .build();


    }

    @Test
    @DisplayName("Routes -> Listar cuentas")
    void listarCuentas() {

        M_Cuenta_DTO cuenta = new M_Cuenta_DTO();
        cuenta.setSaldo_Global(BigDecimal.valueOf(1000));
        cuenta.setCliente( new M_Cliente_DTO("1", "Test"));
        cuenta.setId("1");

        when(listarCuentasUseCase.get()).thenReturn(Flux.just(cuenta));

        webTestClient.get()
                .uri("/Cuentas/listar")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.valueOf("text/event-stream;charset=UTF-8"))
                .expectBodyList(M_Cuenta_DTO.class)
                .hasSize(1)
                .contains(cuenta);
    }

    @Test
    @DisplayName("Routes -> Listar cuentas, con error")
    void listarCuentasWithError() {

        M_Cuenta_DTO cuenta = new M_Cuenta_DTO();
        cuenta.setSaldo_Global(BigDecimal.valueOf(1000));
        cuenta.setCliente( new M_Cliente_DTO("1", "Test"));
        cuenta.setId("1");

        M_Cuenta_DTO cuentaResult = new M_Cuenta_DTO();
        cuenta.setSaldo_Global(BigDecimal.valueOf(1000));
        cuenta.setCliente( new M_Cliente_DTO("1", "Test"));
        cuenta.setId("2");

        when(listarCuentasUseCase.get()).thenReturn(Flux.just(cuenta));

        webTestClient.get()
                .uri("/Cuentas/listar")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.valueOf("text/event-stream;charset=UTF-8"))
                .expectBodyList(M_Cuenta_DTO.class)
                .hasSize(1)
                .doesNotContain(cuentaResult);
    }


    @Test
    @DisplayName("Routes -> Listar cuenta por id")
    void listarCuentasPorId() {

        M_Cuenta_DTO cuenta = new M_Cuenta_DTO();
        cuenta.setSaldo_Global(BigDecimal.valueOf(1000));
        cuenta.setCliente( new M_Cliente_DTO("1", "Test"));
        cuenta.setId("1");

        when(listarCuentaPorIdUseCase.apply("1")).thenReturn(Mono.just(cuenta));

        webTestClient.get()
                .uri("/Cuentas/listar/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(M_Cuenta_DTO.class)
                .isEqualTo(cuenta);


    }

    @Test
    @DisplayName("Routes -> Listar cuenta por id, con error")
    void listarCuentasPorIdWithError() {

        M_Cuenta_DTO cuenta = new M_Cuenta_DTO();
        cuenta.setSaldo_Global(BigDecimal.valueOf(1000));
        cuenta.setCliente( new M_Cliente_DTO("1", "Test"));
        cuenta.setId("1");

        M_Cuenta_DTO cuentaResult = new M_Cuenta_DTO();
        cuenta.setSaldo_Global(BigDecimal.valueOf(1000));
        cuenta.setCliente( new M_Cliente_DTO("1", "Test"));
        cuenta.setId("2");

        when(listarCuentaPorIdUseCase.apply("1")).thenReturn(Mono.just(cuenta));

        webTestClient.get()
                .uri("/Cuentas/listar/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(M_Cuenta_DTO.class)
                .value(cuentaDto -> {
                    assertNotEquals(cuentaResult, cuentaDto);
                });


    }
    @Test
    @DisplayName("Routes -> Crear cuenta")
    void CrearCliente() {
        M_Cuenta_DTO cuenta = new M_Cuenta_DTO();
        cuenta.setSaldo_Global(BigDecimal.valueOf(1000));
        cuenta.setCliente( new M_Cliente_DTO("1", "Test"));
        cuenta.setId("1");

        when(crearCuentaUseCase.apply(cuenta))
                .thenReturn(Mono.just(cuenta));

        webTestClient.post()
                .uri("/Cuentas/Crear")
                .bodyValue(cuenta)
                .exchange()
                .expectStatus().isOk()
                .expectBody(M_Cuenta_DTO.class)
                .isEqualTo(cuenta);

    }

}

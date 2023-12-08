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



    @MockBean
    private CrearCuentaUseCase crearCuentaUseCase;

    @MockBean
    private ListarCuentasUseCase listarCuentasUseCase;

    @MockBean
    private ListarCuentaPorIdUseCase listarCuentaPorIdUseCase;

    @BeforeEach
    void setUp(){

        crearCuentaUseCase = Mockito.mock(CrearCuentaUseCase.class);

        listarCuentasUseCase = Mockito.mock(ListarCuentasUseCase.class);

        listarCuentaPorIdUseCase = Mockito.mock(ListarCuentaPorIdUseCase.class);

        webTestClient = WebTestClient.bindToRouterFunction(
                new CuentaRouter(
                        new CuentaHandler(listarCuentasUseCase, listarCuentaPorIdUseCase, crearCuentaUseCase)).routerFunctionCuentas())
                .build();
    }

    @Test
    @DisplayName("Listar cuentas")
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
    @DisplayName("Listar cuentas, con error")
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
    @DisplayName("Listar cuenta por id")
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
    @DisplayName("Listar cuenta por id, con error")
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

}

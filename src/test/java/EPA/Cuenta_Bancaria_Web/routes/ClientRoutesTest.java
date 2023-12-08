package EPA.Cuenta_Bancaria_Web.routes;

import EPA.Cuenta_Bancaria_Web.handlers.ClienteHandler;
import EPA.Cuenta_Bancaria_Web.handlers.CuentaHandler;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.CrearClienteUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientePorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientesUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.CrearCuentaUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ListarCuentaPorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ListarCuentasUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ClientRoutesTest {

    private WebTestClient webTestClient;

    @Mock
    private ListarClientePorIdUseCase listarClientePorIdUseCase;

    @Mock
    private ListarClientesUseCase listarClientesUseCase;

    @Mock
    private CrearClienteUseCase crearClienteUseCase;

    private ClienteHandler clienteHandler;

    private ClienteRouter clienteRouter;

    @BeforeEach
    void setUp(){
        clienteHandler = new ClienteHandler(  listarClientePorIdUseCase, listarClientesUseCase,  crearClienteUseCase);

        clienteRouter = new ClienteRouter(clienteHandler);

        webTestClient = WebTestClient.bindToRouterFunction(clienteRouter.routerFunctionClientes())
                .build();
    }

    @Test
    @DisplayName("Routes -> Listar clientes")
    void listarClientes() {
        when(listarClientesUseCase.get()).thenReturn(Flux.just(new M_Cliente_DTO("1", "Cliente1")));

        webTestClient.get()
                .uri("/Clientes/listar")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.valueOf("text/event-stream;charset=UTF-8"))
                .expectBodyList(M_Cliente_DTO.class)
                .hasSize(1)
                .contains(new M_Cliente_DTO("1", "Cliente1"));
    }

    @Test
    @DisplayName("Routes -> Listar clientes con error, No debe contener el cliente con ID 2")
    void listarClientesWithError() {
        when(listarClientesUseCase.get()).thenReturn(Flux.just(new M_Cliente_DTO("1", "Cliente1")));

        webTestClient.get()
                .uri("/Clientes/listar")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.valueOf("text/event-stream;charset=UTF-8"))
                .expectBodyList(M_Cliente_DTO.class)
                .hasSize(1)
                .doesNotContain(new M_Cliente_DTO("2", "Cliente1"));
    }

    @Test
    @DisplayName("Routes -> Listar cliente por id")
    void listarClientePorId() {
        when(listarClientePorIdUseCase.apply("1")).thenReturn(Mono.just(new M_Cliente_DTO("1", "Cliente1")));


        webTestClient.get()
                .uri("/Clientes/listar/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(M_Cliente_DTO.class)
                .isEqualTo(new M_Cliente_DTO("1", "Cliente1"));

    }

    @Test
    @DisplayName("Routes -> Listar cliente por id, con error")
    void listarClientePorIdWithError() {
        when(listarClientePorIdUseCase.apply("1")).thenReturn(Mono.just(new M_Cliente_DTO("1", "Cliente1")));


        webTestClient.get()
                .uri("/Clientes/listar/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(M_Cliente_DTO.class)
                .value(clienteDTO -> {
                    assertNotEquals(new M_Cliente_DTO("2", "Cliente1"), clienteDTO);
                });

    }

    @Test
    @DisplayName("Routes -> Crear cliente")
    void CrearCliente() {
        M_Cliente_DTO clienteDTO = new M_Cliente_DTO("1", "Cliente1");

        when(crearClienteUseCase.apply(clienteDTO))
                .thenReturn(Mono.just(clienteDTO));


        webTestClient.post()
                .uri("/Clientes/Crear")
                .bodyValue(clienteDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(M_Cliente_DTO.class)
                .isEqualTo(new M_Cliente_DTO("1", "Cliente1"));

    }


}

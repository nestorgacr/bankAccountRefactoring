package EPA.Cuenta_Bancaria_Web.routes;

import EPA.Cuenta_Bancaria_Web.handlers.ClienteHandler;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.CrearClienteUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientePorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientesUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @MockBean
    private ListarClientePorIdUseCase listarClientePorIdUseCase;

    @MockBean
    private ListarClientesUseCase listarClientesUseCase;

    @MockBean
    private CrearClienteUseCase crearClienteUseCase;

    @BeforeEach
    void setUp(){
        webTestClient = WebTestClient.bindToRouterFunction(new ClienteRouter(new ClienteHandler(listarClientePorIdUseCase, listarClientesUseCase, crearClienteUseCase)).routerFunctionClientes())
                .build();
    }

    @Test
    @DisplayName("Listar clientes")
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
    @DisplayName("Listar clientes con error, No debe contener el cliente con ID 2")
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
    @DisplayName("Listar cliente por id")
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
    @DisplayName("Listar cliente por id, con error")
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
    @DisplayName("Crear cliente")
    void CrearCliente() {
        M_Cliente_DTO clienteDTO = new M_Cliente_DTO("1", "Cliente1");

        when(crearClienteUseCase.apply(clienteDTO)
                .thenReturn(Mono.just(clienteDTO)));


        webTestClient.get()
                .uri("/Clientes/Crear")
                .exchange()
                .expectStatus().isOk()
                .expectBody(M_Cliente_DTO.class)
                .isEqualTo(new M_Cliente_DTO("1", "Cliente1"));

    }


}

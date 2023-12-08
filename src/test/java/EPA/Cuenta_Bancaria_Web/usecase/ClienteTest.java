package EPA.Cuenta_Bancaria_Web.usecase;

import EPA.Cuenta_Bancaria_Web.drivenAdapters.bus.RabbitMqPublisher;
import EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios.I_RepositorioClienteMongo;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_ClienteMongo;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.CrearClienteUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientePorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientesUseCase;
import EPA.Cuenta_Bancaria_Web.utils.ClienteUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ClienteTest {


    @Mock
    private I_RepositorioClienteMongo repository;

    @Mock
    private RabbitMqPublisher rabbitMqPublisher;


    @InjectMocks
    private CrearClienteUseCase crearClienteUseCase;

    @InjectMocks
    private ListarClientePorIdUseCase listarClientePorIdUseCase;

    @InjectMocks
    private ListarClientesUseCase listarClientesUseCase;



    @BeforeEach
    public void setup() {
    }

    @Test
    @DisplayName("Usecase -> Listar clientes")
    public void ListarCLiente() {

        M_ClienteMongo clienteMongo = new M_ClienteMongo("1", "Cliente1");
        when(repository.findAll()).thenReturn(Flux.just(clienteMongo));

        StepVerifier.create(listarClientesUseCase.get())
                .expectNextMatches(cliente -> cliente.getId().equals("1") && cliente.getNombre().equals("Cliente1"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Usecase -> Listar clientes por id")
    public void ListarCLienteId() {

        M_ClienteMongo clienteMongo = new M_ClienteMongo("1", "Cliente1");
        when(repository.findById("1")).thenReturn(Mono.just(clienteMongo));

        StepVerifier.create(listarClientePorIdUseCase.apply("1"))
                .expectNextMatches(cliente -> cliente.getId().equals("1") && cliente.getNombre().equals("Cliente1"))
                .verifyComplete();
    }


//    @Test
//    @DisplayName("Usecase ->Crear cliente")
//    public void CrearCliente() {
//
//
//        M_Cliente_DTO clienteDto = new M_Cliente_DTO("1", "NombreCliente");
//
//
//        when(repository.save(ClienteUtil.dtoToEntity(clienteDto)))
//                .thenReturn(Mono.just(ClienteUtil.dtoToEntity(clienteDto)));
//
//
//        StepVerifier.create(crearClienteUseCase.apply(clienteDto))
//                .expectNextMatches(createdClient -> createdClient.getId().equals(clienteDto.getId()))
//                .verifyComplete();
//    }




}

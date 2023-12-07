package EPA.Cuenta_Bancaria_Web.handlers;

import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.CrearClienteUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientePorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cliente.ListarClientesUseCase;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClienteHandler {


    public ClienteHandler(ListarClientePorIdUseCase listarClientePorIdUseCase, ListarClientesUseCase listarClientesUseCase, CrearClienteUseCase crearClienteUseCase) {
        this.listarClientePorIdUseCase = listarClientePorIdUseCase;
        this.listarClientesUseCase = listarClientesUseCase;
        this.crearClienteUseCase = crearClienteUseCase;
    }

    private final ListarClientePorIdUseCase listarClientePorIdUseCase;
    private final ListarClientesUseCase listarClientesUseCase;

    private final CrearClienteUseCase crearClienteUseCase;



    public Mono<ServerResponse> listar_clientes(ServerRequest request)
    {
        Flux<M_Cliente_DTO> cuentas = listarClientesUseCase.get();
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(cuentas, M_Cuenta_DTO.class);
    }

    public Mono<ServerResponse> listarCliente(ServerRequest request)
    {
        Mono<M_Cliente_DTO> cuentaLocal = listarClientePorIdUseCase.apply(request.pathVariable("id"));
        return ServerResponse.ok()
                .body(cuentaLocal, M_Cuenta_DTO.class);
    }

    public Mono<ServerResponse> crearCliente(ServerRequest request)
    {
        Mono<M_Cliente_DTO> mClienteDtoMono = request.bodyToMono(M_Cliente_DTO.class);
        Mono<M_Cliente_DTO> cuentaLocal = crearClienteUseCase.apply(mClienteDtoMono.block());
        return ServerResponse.ok()
                .body(cuentaLocal, M_Cuenta_DTO.class);
    }


}

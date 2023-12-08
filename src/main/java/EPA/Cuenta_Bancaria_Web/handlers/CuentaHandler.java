package EPA.Cuenta_Bancaria_Web.handlers;

import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.CrearCuentaUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ListarCuentaPorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.cuentas.ListarCuentasUseCase;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CuentaHandler {


    private final ListarCuentasUseCase listarCuentasUseCase;
    private final ListarCuentaPorIdUseCase listarCuentaPorIdUseCase;

    private final CrearCuentaUseCase crearCuentaUseCase;

    public CuentaHandler(ListarCuentasUseCase listarCuentasUseCase, ListarCuentaPorIdUseCase listarCuentaPorIdUseCase, CrearCuentaUseCase crearCuentaUseCase) {
        this.listarCuentasUseCase = listarCuentasUseCase;
        this.listarCuentaPorIdUseCase = listarCuentaPorIdUseCase;
        this.crearCuentaUseCase = crearCuentaUseCase;
    }

    public Mono<ServerResponse> listar_cuentas(ServerRequest request)
    {
        Flux<M_Cuenta_DTO> cuentas = listarCuentasUseCase.get();
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(cuentas, M_Cuenta_DTO.class);
    }

    public Mono<ServerResponse> listarCuenta(ServerRequest request)
    {
        Mono<M_Cuenta_DTO> cuentaLocal = listarCuentaPorIdUseCase.apply(request.pathVariable("id"));
        return ServerResponse.ok()
                .body(cuentaLocal, M_Cuenta_DTO.class);
    }

    public Mono<ServerResponse> Crear_Cuenta(ServerRequest request)
    {
        return request.bodyToMono(M_Cuenta_DTO.class).flatMap(
                cuenta -> {
                    Mono<M_Cuenta_DTO>  temp =  crearCuentaUseCase.apply(cuenta);
                    return ServerResponse.ok()
                   .body(temp, M_Cuenta_DTO.class);
                }
        );

    }


}

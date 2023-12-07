package EPA.Cuenta_Bancaria_Web.handlers;

import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.services.Cuenta.I_Cuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CuentaHandler {
    @Autowired
    I_Cuenta cuenta;

    public Mono<ServerResponse> listar_cuentas(ServerRequest request)
    {
        Flux<M_Cuenta_DTO> cuentas = cuenta.findAll();
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(cuentas, M_Cuenta_DTO.class);
    }

    public Mono<ServerResponse> listarCuenta(ServerRequest request)
    {
        Mono<M_Cuenta_DTO> cuentaLocal = cuenta.findById(request.pathVariable("id"));
        return ServerResponse.ok()
                .body(cuentaLocal, M_Cuenta_DTO.class);
    }

    public Mono<ServerResponse> Crear_Cuenta(ServerRequest request)
    {
        Mono<M_Cuenta_DTO> mCuentaDtoMono = request.bodyToMono(M_Cuenta_DTO.class);
        Mono<M_Cuenta_DTO> cuentaLocal = cuenta.crear_Cuenta(mCuentaDtoMono.block());
        return ServerResponse.ok()
                .body(cuentaLocal, M_Cuenta_DTO.class);
    }


}

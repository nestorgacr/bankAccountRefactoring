package EPA.Cuenta_Bancaria_Web.handlers;

import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Transaccion_DTO;
import EPA.Cuenta_Bancaria_Web.models.Enum_Tipos_Deposito;
import EPA.Cuenta_Bancaria_Web.services.Transaccion.I_Transaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


@Service
public class TransaccionHandler {
    @Autowired
    I_Transaccion transaccion;

    public Mono<ServerResponse> listar_transacciones(ServerRequest request)
    {

        Flux<M_Transaccion_DTO> cuentas = transaccion.findAll();
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(cuentas, M_Cuenta_DTO.class);
    }


    public Mono<ServerResponse> Procesar_Deposito_Cajero(ServerRequest request)
    {
        String id_Cuenta = request.pathVariable("id_Cuenta");
        String sMonto = request.pathVariable("monto");
        BigDecimal monto = new BigDecimal(sMonto);

        Mono<M_Transaccion_DTO> transaccionLocal =  transaccion.Procesar_Deposito(id_Cuenta, Enum_Tipos_Deposito.CAJERO, monto);
        return ServerResponse.ok()
                .body(transaccionLocal, M_Cuenta_DTO.class);
    }


    public Mono<ServerResponse> Procesar_Deposito_Sucursal(ServerRequest request)
    {

        String id_Cuenta = request.pathVariable("id_Cuenta");
        String sMonto = request.pathVariable("monto");
        BigDecimal monto = new BigDecimal(sMonto);

        Mono<M_Transaccion_DTO> transaccionLocal =  transaccion.Procesar_Deposito(id_Cuenta, Enum_Tipos_Deposito.SUCURSAL, monto);
        return ServerResponse.ok()
                .body(transaccionLocal, M_Cuenta_DTO.class);
    }


    public Mono<ServerResponse> Procesar_Deposito_OtraCuenta(ServerRequest request)
    {
        String id_Cuenta = request.pathVariable("id_Cuenta");
        String sMonto = request.pathVariable("monto");
        BigDecimal monto = new BigDecimal(sMonto);

        Mono<M_Transaccion_DTO> transaccionLocal =  transaccion.Procesar_Deposito(id_Cuenta, Enum_Tipos_Deposito.OTRA_CUENTA, monto);
        return ServerResponse.ok()
                .body(transaccionLocal, M_Cuenta_DTO.class);
    }
}

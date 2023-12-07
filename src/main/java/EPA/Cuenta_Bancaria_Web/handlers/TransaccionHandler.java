package EPA.Cuenta_Bancaria_Web.handlers;

import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Transaccion_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.ProcesarTransaccionDto;
import EPA.Cuenta_Bancaria_Web.models.DTO.TransaccionResponseDto;
import EPA.Cuenta_Bancaria_Web.models.Enum_Tipos_Deposito;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.BorrarTransaccionPorIdProcesoUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.ListarTransaccionPorIdUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.ListarTransaccionesUseCase;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.ProcesarTransaccionUseCase;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


@Service
public class TransaccionHandler {
    BorrarTransaccionPorIdProcesoUseCase borrarTransaccionPorIdProcesoUseCase;
    ListarTransaccionesUseCase listarTransaccionesUseCase;
    ProcesarTransaccionUseCase procesarTransaccionUseCase;
    ListarTransaccionPorIdUseCase listarTransaccionPorIdUseCase;



    public TransaccionHandler(BorrarTransaccionPorIdProcesoUseCase borrarTransaccionPorIdProcesoUseCase,
                              ListarTransaccionesUseCase listarTransaccionesUseCase,
                              ProcesarTransaccionUseCase procesarTransaccionUseCase,
                              ListarTransaccionPorIdUseCase listarTransaccionPorIdUseCase) {
        this.borrarTransaccionPorIdProcesoUseCase = borrarTransaccionPorIdProcesoUseCase;
        this.listarTransaccionesUseCase = listarTransaccionesUseCase;
        this.procesarTransaccionUseCase = procesarTransaccionUseCase;
        this.listarTransaccionPorIdUseCase = listarTransaccionPorIdUseCase;
    }

    public Mono<ServerResponse> listar_transacciones(ServerRequest request)
    {

        Flux<M_Transaccion_DTO> cuentas = listarTransaccionesUseCase.get();
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(cuentas, M_Cuenta_DTO.class);
    }

    public Mono<ServerResponse> listar_transaccionesPorId(ServerRequest request)
    {

        Mono<TransaccionResponseDto> cuentas = listarTransaccionPorIdUseCase.apply(request.pathVariable("id"));
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(cuentas, M_Cuenta_DTO.class);
    }


    public Mono<ServerResponse> Procesar_Deposito_Cajero(ServerRequest request)
    {
        String id_Cuenta = request.pathVariable("id_Cuenta");
        String sMonto = request.pathVariable("monto");
        BigDecimal monto = new BigDecimal(sMonto);
        ProcesarTransaccionDto tran = new ProcesarTransaccionDto(id_Cuenta,Enum_Tipos_Deposito.CAJERO, monto);
        Mono<M_Transaccion_DTO> transaccionLocal =  procesarTransaccionUseCase.apply(tran);
        return ServerResponse.ok()
                .body(transaccionLocal, M_Cuenta_DTO.class);
    }


    public Mono<ServerResponse> Procesar_Deposito_Sucursal(ServerRequest request)
    {

        String id_Cuenta = request.pathVariable("id_Cuenta");
        String sMonto = request.pathVariable("monto");
        BigDecimal monto = new BigDecimal(sMonto);

        ProcesarTransaccionDto tran = new ProcesarTransaccionDto(id_Cuenta,Enum_Tipos_Deposito.SUCURSAL, monto);
        Mono<M_Transaccion_DTO> transaccionLocal =  procesarTransaccionUseCase.apply(tran);
        return ServerResponse.ok()
                .body(transaccionLocal, M_Cuenta_DTO.class);
    }


    public Mono<ServerResponse> Procesar_Deposito_OtraCuenta(ServerRequest request)
    {
        String id_Cuenta = request.pathVariable("id_Cuenta");
        String sMonto = request.pathVariable("monto");
        BigDecimal monto = new BigDecimal(sMonto);

        ProcesarTransaccionDto tran = new ProcesarTransaccionDto(id_Cuenta,Enum_Tipos_Deposito.OTRA_CUENTA, monto);
        Mono<M_Transaccion_DTO> transaccionLocal =  procesarTransaccionUseCase.apply(tran);

        return ServerResponse.ok()
                .body(transaccionLocal, M_Cuenta_DTO.class);
    }
}

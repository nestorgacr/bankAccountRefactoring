package EPA.Cuenta_Bancaria_Web.handlers;

import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Transaccion_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.ProcesarTransaccionDto;
import EPA.Cuenta_Bancaria_Web.models.DTO.TransaccionResponseDto;
import EPA.Cuenta_Bancaria_Web.models.Enum_Tipos_Deposito;
import EPA.Cuenta_Bancaria_Web.usecase.transaccion.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


@Service
public class TransaccionHandler {
    private final BorrarTransaccionPorIdProcesoUseCase borrarTransaccionPorIdProcesoUseCase;
    private final ListarTransaccionesUseCase listarTransaccionesUseCase;
    private final ProcesarTransaccionUseCase procesarTransaccionUseCase;
    private final ListarTransaccionPorIdUseCase listarTransaccionPorIdUseCase;

    private final ProcesarTransaccionErrorUseCase procesarTransaccionErrorUseCase;



    public TransaccionHandler(BorrarTransaccionPorIdProcesoUseCase borrarTransaccionPorIdProcesoUseCase,
                              ListarTransaccionesUseCase listarTransaccionesUseCase,
                              ProcesarTransaccionUseCase procesarTransaccionUseCase,
                              ListarTransaccionPorIdUseCase listarTransaccionPorIdUseCase, ProcesarTransaccionErrorUseCase procesarTransaccionErrorUseCase) {
        this.borrarTransaccionPorIdProcesoUseCase = borrarTransaccionPorIdProcesoUseCase;
        this.listarTransaccionesUseCase = listarTransaccionesUseCase;
        this.procesarTransaccionUseCase = procesarTransaccionUseCase;
        this.listarTransaccionPorIdUseCase = listarTransaccionPorIdUseCase;
        this.procesarTransaccionErrorUseCase = procesarTransaccionErrorUseCase;
    }

    public Mono<ServerResponse> listar_transacciones(ServerRequest request)
    {

        Flux<M_Transaccion_DTO> transacciones = listarTransaccionesUseCase.get();
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(transacciones, M_Cuenta_DTO.class);
    }

    public Mono<ServerResponse> listar_transaccionesPorId(ServerRequest request)
    {

        Mono<TransaccionResponseDto> transaccion = listarTransaccionPorIdUseCase.apply(request.pathVariable("id"));
        return ServerResponse.ok()
                .body(transaccion, M_Cuenta_DTO.class);
    }


    public Mono<ServerResponse> Procesar_Deposito_Cajero(ServerRequest request)
    {
        String id_Tran = request.pathVariable("id_Cuenta");
        String sMonto = request.pathVariable("monto");
        BigDecimal monto = new BigDecimal(sMonto);
        ProcesarTransaccionDto tran = new ProcesarTransaccionDto(id_Tran,Enum_Tipos_Deposito.CAJERO, monto);
        Mono<M_Transaccion_DTO> transaccionLocal =  procesarTransaccionUseCase.apply(tran);
        return ServerResponse.ok()
                .body(transaccionLocal, M_Cuenta_DTO.class);
    }


    public Mono<ServerResponse> Procesar_Deposito_Sucursal(ServerRequest request)
    {

        String id_Tran = request.pathVariable("id_Cuenta");
        String sMonto = request.pathVariable("monto");
        BigDecimal monto = new BigDecimal(sMonto);

        ProcesarTransaccionDto tran = new ProcesarTransaccionDto(id_Tran,Enum_Tipos_Deposito.SUCURSAL, monto);
        Mono<M_Transaccion_DTO> transaccionLocal =  procesarTransaccionUseCase.apply(tran);
        return ServerResponse.ok()
                .body(transaccionLocal, M_Cuenta_DTO.class);
    }


    public Mono<ServerResponse> Procesar_Deposito_OtraCuenta(ServerRequest request)
    {
        String id_Tran = request.pathVariable("id_Cuenta");
        String sMonto = request.pathVariable("monto");
        BigDecimal monto = new BigDecimal(sMonto);

        ProcesarTransaccionDto tran = new ProcesarTransaccionDto(id_Tran,Enum_Tipos_Deposito.OTRA_CUENTA, monto);
        Mono<M_Transaccion_DTO> transaccionLocal =  procesarTransaccionErrorUseCase.apply(tran);

        return ServerResponse.ok()
                .body(transaccionLocal, M_Cuenta_DTO.class);
    }

    public Mono<ServerResponse> Procesar_Compra_Fisica(ServerRequest request)
    {
        String id_Tran = request.pathVariable("id_Cuenta");
        String sMonto = request.pathVariable("monto");
        BigDecimal monto = new BigDecimal(sMonto);

        ProcesarTransaccionDto tran = new ProcesarTransaccionDto(id_Tran,Enum_Tipos_Deposito.COMPRA_FISICA, monto);
        Mono<M_Transaccion_DTO> transaccionLocal =  procesarTransaccionUseCase.apply(tran);

        return ServerResponse.ok()
                .body(transaccionLocal, M_Cuenta_DTO.class);
    }
    public Mono<ServerResponse> Procesar_Compra_Web(ServerRequest request)
    {
        String id_Tran = request.pathVariable("id_Cuenta");
        String sMonto = request.pathVariable("monto");
        BigDecimal monto = new BigDecimal(sMonto);

        ProcesarTransaccionDto tran = new ProcesarTransaccionDto(id_Tran,Enum_Tipos_Deposito.COMPRA_WEB, monto);
        Mono<M_Transaccion_DTO> transaccionLocal =  procesarTransaccionUseCase.apply(tran);

        return ServerResponse.ok()
                .body(transaccionLocal, M_Cuenta_DTO.class);
    }

    public Mono<ServerResponse> Procesar_Retiro(ServerRequest request)
    {
        String id_Tran = request.pathVariable("id_Cuenta");
        String sMonto = request.pathVariable("monto");
        BigDecimal monto = new BigDecimal(sMonto);

        ProcesarTransaccionDto tran = new ProcesarTransaccionDto(id_Tran,Enum_Tipos_Deposito.RETIRO, monto);
        Mono<M_Transaccion_DTO> transaccionLocal =  procesarTransaccionUseCase.apply(tran);

        return ServerResponse.ok()
                .body(transaccionLocal, M_Cuenta_DTO.class);
    }

}

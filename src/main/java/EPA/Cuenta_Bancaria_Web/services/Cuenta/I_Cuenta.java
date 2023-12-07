package EPA.Cuenta_Bancaria_Web.services.Cuenta;

import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface I_Cuenta
{
    Mono<M_Cuenta_DTO> crear_Cuenta(M_Cuenta_DTO p_Cuenta);

    Flux<M_Cuenta_DTO> findAll();

    Mono<Void> Actualizar_Saldo(String id_Cuenta, BigDecimal monto);
}

package EPA.Cuenta_Bancaria_Web.models.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class M_Transaccion_DTO
{
    private String id;
    private M_Cuenta_DTO cuenta;
    private BigDecimal monto_transaccion;
    private BigDecimal saldo_inicial;
    private BigDecimal saldo_final;
    private BigDecimal costo_tansaccion;
    private String tipo;
    public String idProceso;


    public M_Transaccion_DTO(String id, M_Cuenta_DTO cuenta, BigDecimal monto_transaccion, BigDecimal saldo_inicial, BigDecimal saldo_final, BigDecimal costo_tansaccion, String tipo, String idProceso) {
        this.id = id;
        this.cuenta = cuenta;
        this.monto_transaccion = monto_transaccion;
        this.saldo_inicial = saldo_inicial;
        this.saldo_final = saldo_final;
        this.costo_tansaccion = costo_tansaccion;
        this.tipo = tipo;
        this.idProceso = idProceso;
    }

    public M_Transaccion_DTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        M_Transaccion_DTO that = (M_Transaccion_DTO) o;
        return Objects.equals(id, that.id) && Objects.equals(cuenta, that.cuenta) && Objects.equals(monto_transaccion, that.monto_transaccion) && Objects.equals(saldo_inicial, that.saldo_inicial) && Objects.equals(saldo_final, that.saldo_final) && Objects.equals(costo_tansaccion, that.costo_tansaccion) && Objects.equals(tipo, that.tipo) && Objects.equals(idProceso, that.idProceso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cuenta, monto_transaccion, saldo_inicial, saldo_final, costo_tansaccion, tipo, idProceso);
    }
}

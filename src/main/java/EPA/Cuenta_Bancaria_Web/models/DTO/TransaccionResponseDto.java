package EPA.Cuenta_Bancaria_Web.models.DTO;

import java.math.BigDecimal;
import java.util.Objects;

public class TransaccionResponseDto {
    private String id;
    private BigDecimal monto_transaccion;
    private BigDecimal saldo_inicial;
    private BigDecimal saldo_final;
    private BigDecimal costo_transaccion;
    private String tipo;
    public String idProceso;

    public TransaccionResponseDto(String id, BigDecimal monto_transaccion, BigDecimal saldo_inicial, BigDecimal saldo_final, BigDecimal costo_transaccion, String tipo, String idProceso) {
        this.id = id;
        this.monto_transaccion = monto_transaccion;
        this.saldo_inicial = saldo_inicial;
        this.saldo_final = saldo_final;
        this.costo_transaccion = costo_transaccion;
        this.tipo = tipo;
        this.idProceso = idProceso;
    }

    public TransaccionResponseDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransaccionResponseDto that = (TransaccionResponseDto) o;
        return Objects.equals(id, that.id) && Objects.equals(monto_transaccion, that.monto_transaccion) && Objects.equals(saldo_inicial, that.saldo_inicial) && Objects.equals(saldo_final, that.saldo_final) && Objects.equals(costo_transaccion, that.costo_transaccion) && Objects.equals(tipo, that.tipo) && Objects.equals(idProceso, that.idProceso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, monto_transaccion, saldo_inicial, saldo_final, costo_transaccion, tipo, idProceso);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getMonto_transaccion() {
        return monto_transaccion;
    }

    public void setMonto_transaccion(BigDecimal monto_transaccion) {
        this.monto_transaccion = monto_transaccion;
    }

    public BigDecimal getSaldo_inicial() {
        return saldo_inicial;
    }

    public void setSaldo_inicial(BigDecimal saldo_inicial) {
        this.saldo_inicial = saldo_inicial;
    }

    public BigDecimal getSaldo_final() {
        return saldo_final;
    }

    public void setSaldo_final(BigDecimal saldo_final) {
        this.saldo_final = saldo_final;
    }

    public BigDecimal getCosto_transaccion() {
        return costo_transaccion;
    }

    public void setCosto_transaccion(BigDecimal costo_transaccion) {
        this.costo_transaccion = costo_transaccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }
}

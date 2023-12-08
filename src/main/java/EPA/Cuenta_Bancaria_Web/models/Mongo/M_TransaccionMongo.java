package EPA.Cuenta_Bancaria_Web.models.Mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Objects;

@Document("M_TransaccionMongo")
@Data
public class M_TransaccionMongo
{
    private String id;
    private BigDecimal monto_transaccion;
    private BigDecimal saldo_inicial;
    private BigDecimal saldo_final;
    private BigDecimal costo_transaccion;
    private String tipo;
    public String idProceso;

    //-------------------------------------------------------------------------------------------------------------------------
    private M_CuentaMongo cuenta;

    //-------------------------------------------------------------------------------------------------------------------------

    public M_TransaccionMongo(M_CuentaMongo cuenta, BigDecimal monto_transaccion, BigDecimal saldo_inicial, BigDecimal saldo_final, BigDecimal costo_tansaccion, String tipo) {
        //this.id = id;
        this.cuenta = cuenta;
        this.monto_transaccion = monto_transaccion;
        this.saldo_inicial = saldo_inicial;
        this.saldo_final = saldo_final;
        this.costo_transaccion = costo_tansaccion;
        this.tipo = tipo;
    }

    public M_TransaccionMongo()
    {

    }

    public void setId(String id) {
        this.id = id;
    }

    public M_CuentaMongo getCuenta() {
        return cuenta;
    }

    public void setCuenta(M_CuentaMongo cuenta) {
        this.cuenta = cuenta;
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

    public BigDecimal getCosto_tansaccion() {
        return costo_transaccion;
    }

    public void setCosto_tansaccion(BigDecimal costo_tansaccion) {
        this.costo_transaccion = costo_tansaccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getCosto_transaccion() {
        return costo_transaccion;
    }

    public void setCosto_transaccion(BigDecimal costo_transaccion) {
        this.costo_transaccion = costo_transaccion;
    }

    public String getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        M_TransaccionMongo that = (M_TransaccionMongo) o;
        return Objects.equals(id, that.id) && Objects.equals(monto_transaccion, that.monto_transaccion) && Objects.equals(saldo_inicial, that.saldo_inicial) && Objects.equals(saldo_final, that.saldo_final) && Objects.equals(costo_transaccion, that.costo_transaccion) && Objects.equals(tipo, that.tipo) && Objects.equals(idProceso, that.idProceso) && Objects.equals(cuenta, that.cuenta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, monto_transaccion, saldo_inicial, saldo_final, costo_transaccion, tipo, idProceso, cuenta);
    }
}

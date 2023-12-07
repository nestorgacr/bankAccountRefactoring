package EPA.Cuenta_Bancaria_Web.models.DTO;

import EPA.Cuenta_Bancaria_Web.models.Enum_Tipos_Deposito;

import java.math.BigDecimal;
import java.util.Objects;

public class ProcesarTransaccionDto {
    String id_Cuenta;
    Enum_Tipos_Deposito tipo;
    BigDecimal monto;

    public ProcesarTransaccionDto(String id_Cuenta, Enum_Tipos_Deposito tipo, BigDecimal monto) {
        this.id_Cuenta = id_Cuenta;
        this.tipo = tipo;
        this.monto = monto;
    }

    public String getId_Cuenta() {
        return id_Cuenta;
    }

    public ProcesarTransaccionDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcesarTransaccionDto that = (ProcesarTransaccionDto) o;
        return Objects.equals(id_Cuenta, that.id_Cuenta) && tipo == that.tipo && Objects.equals(monto, that.monto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Cuenta, tipo, monto);
    }

    public void setId_Cuenta(String id_Cuenta) {
        this.id_Cuenta = id_Cuenta;
    }

    public Enum_Tipos_Deposito getTipo() {
        return tipo;
    }

    public void setTipo(Enum_Tipos_Deposito tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}

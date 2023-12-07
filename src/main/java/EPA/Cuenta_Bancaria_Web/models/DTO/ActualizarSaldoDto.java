package EPA.Cuenta_Bancaria_Web.models.DTO;

import java.math.BigDecimal;
import java.util.Objects;

public class ActualizarSaldoDto {
    private String id_Cuenta;
    private BigDecimal monto;

    public ActualizarSaldoDto() {
    }


    public String getId_Cuenta() {
        return id_Cuenta;
    }

    public void setId_Cuenta(String id_Cuenta) {
        this.id_Cuenta = id_Cuenta;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActualizarSaldoDto that = (ActualizarSaldoDto) o;
        return Objects.equals(id_Cuenta, that.id_Cuenta) && Objects.equals(monto, that.monto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Cuenta, monto);
    }
}

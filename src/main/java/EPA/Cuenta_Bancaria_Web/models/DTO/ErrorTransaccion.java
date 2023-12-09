package EPA.Cuenta_Bancaria_Web.models.DTO;

import EPA.Cuenta_Bancaria_Web.models.Mongo.M_CuentaMongo;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_TransaccionMongo;

import java.util.Objects;

public class ErrorTransaccion {

    private M_Transaccion_DTO transaccion;

    public ErrorTransaccion(M_Transaccion_DTO transaccion) {
        this.transaccion = transaccion;
    }

    public ErrorTransaccion() {
    }

    public M_Transaccion_DTO getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(M_Transaccion_DTO transaccion) {
        this.transaccion = transaccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorTransaccion that)) return false;
        return Objects.equals(transaccion, that.transaccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaccion);
    }
}

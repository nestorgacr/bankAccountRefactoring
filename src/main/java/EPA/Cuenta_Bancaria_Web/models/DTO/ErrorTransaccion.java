package EPA.Cuenta_Bancaria_Web.models.DTO;

import EPA.Cuenta_Bancaria_Web.models.Mongo.M_CuentaMongo;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_TransaccionMongo;

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
}

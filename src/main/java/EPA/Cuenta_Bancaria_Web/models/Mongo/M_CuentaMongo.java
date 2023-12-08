package EPA.Cuenta_Bancaria_Web.models.Mongo;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Objects;

@Document("M_CuentaMongo")
public class M_CuentaMongo
{
    @Id
    private String id;
    private BigDecimal saldo_Global;
    private M_ClienteMongo cliente;

    public M_CuentaMongo(String id, M_ClienteMongo cliente, BigDecimal saldo_Global) {
        this.id = id;
        this.saldo_Global = saldo_Global;
        this.cliente = cliente;
    }

    public M_CuentaMongo()
    {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getSaldo_Global() {
        return saldo_Global;
    }

    public void setSaldo_Global(BigDecimal saldo_Global) {
        this.saldo_Global = saldo_Global;
    }

    public M_ClienteMongo getCliente() {
        return cliente;
    }

    public void setCliente(M_ClienteMongo cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        M_CuentaMongo that = (M_CuentaMongo) o;
        return Objects.equals(id, that.id) && Objects.equals(saldo_Global, that.saldo_Global) && Objects.equals(cliente, that.cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, saldo_Global, cliente);
    }
}

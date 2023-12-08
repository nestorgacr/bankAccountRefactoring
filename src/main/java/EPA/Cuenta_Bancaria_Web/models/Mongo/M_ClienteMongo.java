package EPA.Cuenta_Bancaria_Web.models.Mongo;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("M_ClienteMongo")
public class M_ClienteMongo
{
    @Id
    private String id;
    private String nombre;

    public M_ClienteMongo(String id, String nombre)
    {
        this.id = id;
        this.nombre = nombre;
    }

    public M_ClienteMongo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        M_ClienteMongo that = (M_ClienteMongo) o;
        return Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }
}

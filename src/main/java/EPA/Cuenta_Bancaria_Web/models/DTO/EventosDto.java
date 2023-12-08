package EPA.Cuenta_Bancaria_Web.models.DTO;

public class EventosDto {
    String mensaje;

    Object data;

    public EventosDto(String mensaje, Object data) {
        this.mensaje = mensaje;
        this.data = data;
    }

    public EventosDto() {
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

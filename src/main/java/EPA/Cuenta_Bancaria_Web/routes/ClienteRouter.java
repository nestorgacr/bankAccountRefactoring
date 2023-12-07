package EPA.Cuenta_Bancaria_Web.routes;

import EPA.Cuenta_Bancaria_Web.handlers.ClienteHandler;
import EPA.Cuenta_Bancaria_Web.handlers.CuentaHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ClienteRouter {


    private final ClienteHandler handler;

    @Autowired
    public ClienteRouter(ClienteHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionCuentas() {
        return RouterFunctions.route()
                .path("/Clientes", builder ->
                        builder
                                .GET("/listar", handler::listar_clientes)
                                .GET("/listar/{id}", handler::listarCliente)
                                .POST("/Crear", handler::crearCliente)
                )
                .build();
    }
}

package EPA.Cuenta_Bancaria_Web.routes;

import EPA.Cuenta_Bancaria_Web.handlers.CuentaHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CuentaRouter {


    private final CuentaHandler handler;

    @Autowired
    public CuentaRouter(CuentaHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionCuentas() {
        return RouterFunctions.route()
                .path("/Cuentas", builder ->
                        builder
                                .GET("/listar", handler::listar_cuentas)
                                .GET("/listar/{id}", handler::listarCuenta)
                                .POST("/Crear", handler::Crear_Cuenta)
                )
                .build();
    }
}

package EPA.Cuenta_Bancaria_Web.routes;

import EPA.Cuenta_Bancaria_Web.handlers.TransaccionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TransaccionRouter {


    private final TransaccionHandler handler;

    @Autowired
    public TransaccionRouter(TransaccionHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionTransaccion() {
        return RouterFunctions.route()
                .path("/Transacciones", builder ->
                        builder
                                .GET("/listar", handler::listar_transacciones)
                                .POST("/Crear/Deposito/Cajero/{id_Cuenta}/{monto}", handler::Procesar_Deposito_Cajero)
                                .POST("/Crear/Deposito/Sucursal/{id_Cuenta}/{monto}", handler::Procesar_Deposito_Sucursal)
                                .POST("/Crear/Deposito/OtraCuenta/{id_Cuenta}/{monto}", handler::Procesar_Deposito_OtraCuenta)
                )
                .build();



    }
}

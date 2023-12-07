package EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios;

import EPA.Cuenta_Bancaria_Web.models.Mongo.M_TransaccionMongo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface I_Repositorio_TransaccionMongo extends ReactiveMongoRepository<M_TransaccionMongo, String>
{
    Mono<Void> deleteByIdProceso(String idProceso);
}

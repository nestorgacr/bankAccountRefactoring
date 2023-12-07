package EPA.Cuenta_Bancaria_Web.drivenAdapters.repositorios;

import EPA.Cuenta_Bancaria_Web.models.Mongo.M_ClienteMongo;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_CuentaMongo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface I_RepositorioClienteMongo extends ReactiveMongoRepository<M_ClienteMongo, String>
{
}

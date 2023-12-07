package EPA.Cuenta_Bancaria_Web.utils;

import EPA.Cuenta_Bancaria_Web.models.DTO.TransaccionResponseDto;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_TransaccionMongo;
import org.springframework.beans.BeanUtils;

public class TransaccionUtil {
    public static TransaccionResponseDto entityToDto(M_TransaccionMongo transaccionMongo) {
        TransaccionResponseDto tranDto = new TransaccionResponseDto();
        BeanUtils.copyProperties(transaccionMongo, tranDto);
        return tranDto;
    }

    public static M_TransaccionMongo dtoToEntity(TransaccionResponseDto transaction) {
        M_TransaccionMongo transaccionMongo = new M_TransaccionMongo();
        BeanUtils.copyProperties(transaction, transaccionMongo);
        return transaccionMongo;
    }
}

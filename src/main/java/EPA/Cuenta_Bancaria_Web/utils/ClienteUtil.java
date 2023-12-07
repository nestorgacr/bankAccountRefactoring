package EPA.Cuenta_Bancaria_Web.utils;

import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.TransaccionResponseDto;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_ClienteMongo;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_TransaccionMongo;
import org.springframework.beans.BeanUtils;

public class ClienteUtil {
    public static M_Cliente_DTO entityToDto(M_ClienteMongo entidad) {
        M_Cliente_DTO dto = new M_Cliente_DTO();
        BeanUtils.copyProperties(entidad, dto);
        return dto;
    }

    public static M_ClienteMongo dtoToEntity(M_Cliente_DTO dto) {
        M_ClienteMongo entidad = new M_ClienteMongo();
        BeanUtils.copyProperties(dto, entidad);
        return entidad;
    }
}

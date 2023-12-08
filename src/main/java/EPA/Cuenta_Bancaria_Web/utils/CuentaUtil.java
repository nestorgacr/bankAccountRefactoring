package EPA.Cuenta_Bancaria_Web.utils;

import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cliente_DTO;
import EPA.Cuenta_Bancaria_Web.models.DTO.M_Cuenta_DTO;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_ClienteMongo;
import EPA.Cuenta_Bancaria_Web.models.Mongo.M_CuentaMongo;
import org.springframework.beans.BeanUtils;

public class CuentaUtil {
    public static M_Cuenta_DTO entityToDto(M_CuentaMongo entidad) {
        M_Cuenta_DTO dto = new M_Cuenta_DTO();
        BeanUtils.copyProperties(entidad, dto);
        return dto;
    }

    public static M_CuentaMongo dtoToEntity(M_Cuenta_DTO dto) {
        M_CuentaMongo entidad = new M_CuentaMongo();
        BeanUtils.copyProperties(dto, entidad);
        return entidad;
    }
}

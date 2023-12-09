package EPA.Cuenta_Bancaria_Web.models;

public enum Enum_Tipos_Deposito
{
    CAJERO,
    SUCURSAL,
    OTRA_CUENTA,

    COMPRA_FISICA,

    COMPRA_WEB,

    RETIRO;

    @Override
    public String toString()
    {
        String sTipo = "Indefinido";

        switch (this)
        {
            case CAJERO: sTipo = "CAJERO"; break;
            case SUCURSAL: sTipo = "SUCURSAL"; break;
            case OTRA_CUENTA: sTipo = "OTRA_CUENTA"; break;
            case COMPRA_FISICA: sTipo = "COMPRA_FISICA"; break;
            case COMPRA_WEB: sTipo = "COMPRA_WEB"; break;
            case RETIRO: sTipo = "RETIRO"; break;
        }

        return sTipo;
    }
}

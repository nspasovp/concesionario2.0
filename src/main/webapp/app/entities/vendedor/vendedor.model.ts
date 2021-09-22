import * as dayjs from 'dayjs';

export interface IVendedor {
  id?: number;
  dni?: string;
  nombre?: string;
  primeriApellido?: string;
  segundoApellido?: string | null;
  fechaNacimiento?: dayjs.Dayjs;
  fechaContratacion?: dayjs.Dayjs;
  fechaBaja?: dayjs.Dayjs | null;
}

export class Vendedor implements IVendedor {
  constructor(
    public id?: number,
    public dni?: string,
    public nombre?: string,
    public primeriApellido?: string,
    public segundoApellido?: string | null,
    public fechaNacimiento?: dayjs.Dayjs,
    public fechaContratacion?: dayjs.Dayjs,
    public fechaBaja?: dayjs.Dayjs | null
  ) {}
}

export function getVendedorIdentifier(vendedor: IVendedor): number | undefined {
  return vendedor.id;
}

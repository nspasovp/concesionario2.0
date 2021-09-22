import * as dayjs from 'dayjs';

export interface IComprador {
  id?: number;
  dni?: string;
  nombre?: string;
  primerApellido?: string;
  segundoApellido?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  direccion?: string;
}

export class Comprador implements IComprador {
  constructor(
    public id?: number,
    public dni?: string,
    public nombre?: string,
    public primerApellido?: string,
    public segundoApellido?: string | null,
    public fechaNacimiento?: dayjs.Dayjs | null,
    public direccion?: string
  ) {}
}

export function getCompradorIdentifier(comprador: IComprador): number | undefined {
  return comprador.id;
}

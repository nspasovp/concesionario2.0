import * as dayjs from 'dayjs';
import { IVendedor } from 'app/entities/vendedor/vendedor.model';
import { IComprador } from 'app/entities/comprador/comprador.model';

export interface IVenta {
  id?: number;
  fecha?: dayjs.Dayjs;
  numeroCoches?: number;
  vendedor?: IVendedor;
  comprador?: IComprador;
}

export class Venta implements IVenta {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs,
    public numeroCoches?: number,
    public vendedor?: IVendedor,
    public comprador?: IComprador
  ) {}
}

export function getVentaIdentifier(venta: IVenta): number | undefined {
  return venta.id;
}

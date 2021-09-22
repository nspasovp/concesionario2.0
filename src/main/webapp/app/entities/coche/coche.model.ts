import { IVenta } from 'app/entities/venta/venta.model';

export interface ICoche {
  id?: number;
  marca?: string;
  modelo?: string;
  precio?: number;
  venta?: IVenta | null;
}

export class Coche implements ICoche {
  constructor(public id?: number, public marca?: string, public modelo?: string, public precio?: number, public venta?: IVenta | null) {}
}

export function getCocheIdentifier(coche: ICoche): number | undefined {
  return coche.id;
}

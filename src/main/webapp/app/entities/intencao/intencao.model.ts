import * as dayjs from 'dayjs';
import { IProduto } from 'app/entities/produto/produto.model';

export interface IIntencao {
  id?: number;
  descricao?: string | null;
  valorEstimado?: number | null;
  data?: dayjs.Dayjs | null;
  produto?: IProduto | null;
}

export class Intencao implements IIntencao {
  constructor(
    public id?: number,
    public descricao?: string | null,
    public valorEstimado?: number | null,
    public data?: dayjs.Dayjs | null,
    public produto?: IProduto | null
  ) {}
}

export function getIntencaoIdentifier(intencao: IIntencao): number | undefined {
  return intencao.id;
}

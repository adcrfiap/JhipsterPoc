export interface IProduto {
  id?: number;
  nome?: string | null;
  modelo?: string | null;
  categoria?: string | null;
  marca?: string | null;
}

export class Produto implements IProduto {
  constructor(
    public id?: number,
    public nome?: string | null,
    public modelo?: string | null,
    public categoria?: string | null,
    public marca?: string | null
  ) {}
}

export function getProdutoIdentifier(produto: IProduto): number | undefined {
  return produto.id;
}

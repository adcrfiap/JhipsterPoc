import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IIntencao, Intencao } from '../intencao.model';
import { IntencaoService } from '../service/intencao.service';
import { IProduto } from 'app/entities/produto/produto.model';
import { ProdutoService } from 'app/entities/produto/service/produto.service';

@Component({
  selector: 'jhi-intencao-update',
  templateUrl: './intencao-update.component.html',
})
export class IntencaoUpdateComponent implements OnInit {
  isSaving = false;

  produtosCollection: IProduto[] = [];

  editForm = this.fb.group({
    id: [],
    descricao: [],
    valorEstimado: [],
    data: [],
    produto: [],
  });

  constructor(
    protected intencaoService: IntencaoService,
    protected produtoService: ProdutoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intencao }) => {
      this.updateForm(intencao);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const intencao = this.createFromForm();
    if (intencao.id !== undefined) {
      this.subscribeToSaveResponse(this.intencaoService.update(intencao));
    } else {
      this.subscribeToSaveResponse(this.intencaoService.create(intencao));
    }
  }

  trackProdutoById(index: number, item: IProduto): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIntencao>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(intencao: IIntencao): void {
    this.editForm.patchValue({
      id: intencao.id,
      descricao: intencao.descricao,
      valorEstimado: intencao.valorEstimado,
      data: intencao.data,
      produto: intencao.produto,
    });

    this.produtosCollection = this.produtoService.addProdutoToCollectionIfMissing(this.produtosCollection, intencao.produto);
  }

  protected loadRelationshipsOptions(): void {
    this.produtoService
      .query({ filter: 'intencao-is-null' })
      .pipe(map((res: HttpResponse<IProduto[]>) => res.body ?? []))
      .pipe(
        map((produtos: IProduto[]) => this.produtoService.addProdutoToCollectionIfMissing(produtos, this.editForm.get('produto')!.value))
      )
      .subscribe((produtos: IProduto[]) => (this.produtosCollection = produtos));
  }

  protected createFromForm(): IIntencao {
    return {
      ...new Intencao(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      valorEstimado: this.editForm.get(['valorEstimado'])!.value,
      data: this.editForm.get(['data'])!.value,
      produto: this.editForm.get(['produto'])!.value,
    };
  }
}

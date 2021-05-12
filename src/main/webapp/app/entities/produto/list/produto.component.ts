import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProduto } from '../produto.model';
import { ProdutoService } from '../service/produto.service';
import { ProdutoDeleteDialogComponent } from '../delete/produto-delete-dialog.component';

@Component({
  selector: 'jhi-produto',
  templateUrl: './produto.component.html',
})
export class ProdutoComponent implements OnInit {
  produtos?: IProduto[];
  isLoading = false;

  constructor(protected produtoService: ProdutoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.produtoService.query().subscribe(
      (res: HttpResponse<IProduto[]>) => {
        this.isLoading = false;
        this.produtos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProduto): number {
    return item.id!;
  }

  delete(produto: IProduto): void {
    const modalRef = this.modalService.open(ProdutoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.produto = produto;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

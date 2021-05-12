import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIntencao } from '../intencao.model';

@Component({
  selector: 'jhi-intencao-detail',
  templateUrl: './intencao-detail.component.html',
})
export class IntencaoDetailComponent implements OnInit {
  intencao: IIntencao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intencao }) => {
      this.intencao = intencao;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

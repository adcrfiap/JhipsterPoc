import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIntencao } from '../intencao.model';
import { IntencaoService } from '../service/intencao.service';

@Component({
  templateUrl: './intencao-delete-dialog.component.html',
})
export class IntencaoDeleteDialogComponent {
  intencao?: IIntencao;

  constructor(protected intencaoService: IntencaoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.intencaoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

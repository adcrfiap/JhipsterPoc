import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { IntencaoComponent } from './list/intencao.component';
import { IntencaoDetailComponent } from './detail/intencao-detail.component';
import { IntencaoUpdateComponent } from './update/intencao-update.component';
import { IntencaoDeleteDialogComponent } from './delete/intencao-delete-dialog.component';
import { IntencaoRoutingModule } from './route/intencao-routing.module';

@NgModule({
  imports: [SharedModule, IntencaoRoutingModule],
  declarations: [IntencaoComponent, IntencaoDetailComponent, IntencaoUpdateComponent, IntencaoDeleteDialogComponent],
  entryComponents: [IntencaoDeleteDialogComponent],
})
export class IntencaoModule {}

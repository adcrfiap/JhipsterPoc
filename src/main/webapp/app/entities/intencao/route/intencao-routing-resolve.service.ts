import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIntencao, Intencao } from '../intencao.model';
import { IntencaoService } from '../service/intencao.service';

@Injectable({ providedIn: 'root' })
export class IntencaoRoutingResolveService implements Resolve<IIntencao> {
  constructor(protected service: IntencaoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIntencao> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((intencao: HttpResponse<Intencao>) => {
          if (intencao.body) {
            return of(intencao.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Intencao());
  }
}

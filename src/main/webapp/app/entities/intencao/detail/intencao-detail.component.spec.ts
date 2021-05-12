import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IntencaoDetailComponent } from './intencao-detail.component';

describe('Component Tests', () => {
  describe('Intencao Management Detail Component', () => {
    let comp: IntencaoDetailComponent;
    let fixture: ComponentFixture<IntencaoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IntencaoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ intencao: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IntencaoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IntencaoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load intencao on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.intencao).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

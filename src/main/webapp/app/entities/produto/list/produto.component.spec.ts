import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProdutoService } from '../service/produto.service';

import { ProdutoComponent } from './produto.component';

describe('Component Tests', () => {
  describe('Produto Management Component', () => {
    let comp: ProdutoComponent;
    let fixture: ComponentFixture<ProdutoComponent>;
    let service: ProdutoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProdutoComponent],
      })
        .overrideTemplate(ProdutoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProdutoComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ProdutoService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.produtos?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

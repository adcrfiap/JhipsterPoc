jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IntencaoService } from '../service/intencao.service';
import { IIntencao, Intencao } from '../intencao.model';
import { IProduto } from 'app/entities/produto/produto.model';
import { ProdutoService } from 'app/entities/produto/service/produto.service';

import { IntencaoUpdateComponent } from './intencao-update.component';

describe('Component Tests', () => {
  describe('Intencao Management Update Component', () => {
    let comp: IntencaoUpdateComponent;
    let fixture: ComponentFixture<IntencaoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let intencaoService: IntencaoService;
    let produtoService: ProdutoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IntencaoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(IntencaoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IntencaoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      intencaoService = TestBed.inject(IntencaoService);
      produtoService = TestBed.inject(ProdutoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call produto query and add missing value', () => {
        const intencao: IIntencao = { id: 456 };
        const produto: IProduto = { id: 57434 };
        intencao.produto = produto;

        const produtoCollection: IProduto[] = [{ id: 91438 }];
        spyOn(produtoService, 'query').and.returnValue(of(new HttpResponse({ body: produtoCollection })));
        const expectedCollection: IProduto[] = [produto, ...produtoCollection];
        spyOn(produtoService, 'addProdutoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ intencao });
        comp.ngOnInit();

        expect(produtoService.query).toHaveBeenCalled();
        expect(produtoService.addProdutoToCollectionIfMissing).toHaveBeenCalledWith(produtoCollection, produto);
        expect(comp.produtosCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const intencao: IIntencao = { id: 456 };
        const produto: IProduto = { id: 22663 };
        intencao.produto = produto;

        activatedRoute.data = of({ intencao });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(intencao));
        expect(comp.produtosCollection).toContain(produto);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const intencao = { id: 123 };
        spyOn(intencaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ intencao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: intencao }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(intencaoService.update).toHaveBeenCalledWith(intencao);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const intencao = new Intencao();
        spyOn(intencaoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ intencao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: intencao }));
        saveSubject.complete();

        // THEN
        expect(intencaoService.create).toHaveBeenCalledWith(intencao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const intencao = { id: 123 };
        spyOn(intencaoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ intencao });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(intencaoService.update).toHaveBeenCalledWith(intencao);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProdutoById', () => {
        it('Should return tracked Produto primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProdutoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

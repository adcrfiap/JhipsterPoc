import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IIntencao, Intencao } from '../intencao.model';

import { IntencaoService } from './intencao.service';

describe('Service Tests', () => {
  describe('Intencao Service', () => {
    let service: IntencaoService;
    let httpMock: HttpTestingController;
    let elemDefault: IIntencao;
    let expectedResult: IIntencao | IIntencao[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IntencaoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        descricao: 'AAAAAAA',
        valorEstimado: 0,
        data: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            data: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Intencao', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            data: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            data: currentDate,
          },
          returnedFromService
        );

        service.create(new Intencao()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Intencao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            descricao: 'BBBBBB',
            valorEstimado: 1,
            data: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            data: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Intencao', () => {
        const patchObject = Object.assign(
          {
            descricao: 'BBBBBB',
          },
          new Intencao()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            data: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Intencao', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            descricao: 'BBBBBB',
            valorEstimado: 1,
            data: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            data: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Intencao', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIntencaoToCollectionIfMissing', () => {
        it('should add a Intencao to an empty array', () => {
          const intencao: IIntencao = { id: 123 };
          expectedResult = service.addIntencaoToCollectionIfMissing([], intencao);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(intencao);
        });

        it('should not add a Intencao to an array that contains it', () => {
          const intencao: IIntencao = { id: 123 };
          const intencaoCollection: IIntencao[] = [
            {
              ...intencao,
            },
            { id: 456 },
          ];
          expectedResult = service.addIntencaoToCollectionIfMissing(intencaoCollection, intencao);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Intencao to an array that doesn't contain it", () => {
          const intencao: IIntencao = { id: 123 };
          const intencaoCollection: IIntencao[] = [{ id: 456 }];
          expectedResult = service.addIntencaoToCollectionIfMissing(intencaoCollection, intencao);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(intencao);
        });

        it('should add only unique Intencao to an array', () => {
          const intencaoArray: IIntencao[] = [{ id: 123 }, { id: 456 }, { id: 12786 }];
          const intencaoCollection: IIntencao[] = [{ id: 123 }];
          expectedResult = service.addIntencaoToCollectionIfMissing(intencaoCollection, ...intencaoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const intencao: IIntencao = { id: 123 };
          const intencao2: IIntencao = { id: 456 };
          expectedResult = service.addIntencaoToCollectionIfMissing([], intencao, intencao2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(intencao);
          expect(expectedResult).toContain(intencao2);
        });

        it('should accept null and undefined values', () => {
          const intencao: IIntencao = { id: 123 };
          expectedResult = service.addIntencaoToCollectionIfMissing([], null, intencao, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(intencao);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

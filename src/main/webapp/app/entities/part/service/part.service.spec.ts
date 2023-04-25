import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPart } from '../part.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../part.test-samples';

import { PartService } from './part.service';

const requireRestSample: IPart = {
  ...sampleWithRequiredData,
};

describe('Part Service', () => {
  let service: PartService;
  let httpMock: HttpTestingController;
  let expectedResult: IPart | IPart[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PartService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Part', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const part = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(part).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Part', () => {
      const part = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(part).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Part', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Part', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Part', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPartToCollectionIfMissing', () => {
      it('should add a Part to an empty array', () => {
        const part: IPart = sampleWithRequiredData;
        expectedResult = service.addPartToCollectionIfMissing([], part);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(part);
      });

      it('should not add a Part to an array that contains it', () => {
        const part: IPart = sampleWithRequiredData;
        const partCollection: IPart[] = [
          {
            ...part,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPartToCollectionIfMissing(partCollection, part);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Part to an array that doesn't contain it", () => {
        const part: IPart = sampleWithRequiredData;
        const partCollection: IPart[] = [sampleWithPartialData];
        expectedResult = service.addPartToCollectionIfMissing(partCollection, part);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(part);
      });

      it('should add only unique Part to an array', () => {
        const partArray: IPart[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const partCollection: IPart[] = [sampleWithRequiredData];
        expectedResult = service.addPartToCollectionIfMissing(partCollection, ...partArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const part: IPart = sampleWithRequiredData;
        const part2: IPart = sampleWithPartialData;
        expectedResult = service.addPartToCollectionIfMissing([], part, part2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(part);
        expect(expectedResult).toContain(part2);
      });

      it('should accept null and undefined values', () => {
        const part: IPart = sampleWithRequiredData;
        expectedResult = service.addPartToCollectionIfMissing([], null, part, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(part);
      });

      it('should return initial array if no Part is added', () => {
        const partCollection: IPart[] = [sampleWithRequiredData];
        expectedResult = service.addPartToCollectionIfMissing(partCollection, undefined, null);
        expect(expectedResult).toEqual(partCollection);
      });
    });

    describe('comparePart', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePart(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePart(entity1, entity2);
        const compareResult2 = service.comparePart(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePart(entity1, entity2);
        const compareResult2 = service.comparePart(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePart(entity1, entity2);
        const compareResult2 = service.comparePart(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

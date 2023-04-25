import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITheme } from '../theme.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../theme.test-samples';

import { ThemeService } from './theme.service';

const requireRestSample: ITheme = {
  ...sampleWithRequiredData,
};

describe('Theme Service', () => {
  let service: ThemeService;
  let httpMock: HttpTestingController;
  let expectedResult: ITheme | ITheme[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ThemeService);
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

    it('should create a Theme', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const theme = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(theme).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Theme', () => {
      const theme = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(theme).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Theme', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Theme', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Theme', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addThemeToCollectionIfMissing', () => {
      it('should add a Theme to an empty array', () => {
        const theme: ITheme = sampleWithRequiredData;
        expectedResult = service.addThemeToCollectionIfMissing([], theme);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(theme);
      });

      it('should not add a Theme to an array that contains it', () => {
        const theme: ITheme = sampleWithRequiredData;
        const themeCollection: ITheme[] = [
          {
            ...theme,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addThemeToCollectionIfMissing(themeCollection, theme);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Theme to an array that doesn't contain it", () => {
        const theme: ITheme = sampleWithRequiredData;
        const themeCollection: ITheme[] = [sampleWithPartialData];
        expectedResult = service.addThemeToCollectionIfMissing(themeCollection, theme);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(theme);
      });

      it('should add only unique Theme to an array', () => {
        const themeArray: ITheme[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const themeCollection: ITheme[] = [sampleWithRequiredData];
        expectedResult = service.addThemeToCollectionIfMissing(themeCollection, ...themeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const theme: ITheme = sampleWithRequiredData;
        const theme2: ITheme = sampleWithPartialData;
        expectedResult = service.addThemeToCollectionIfMissing([], theme, theme2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(theme);
        expect(expectedResult).toContain(theme2);
      });

      it('should accept null and undefined values', () => {
        const theme: ITheme = sampleWithRequiredData;
        expectedResult = service.addThemeToCollectionIfMissing([], null, theme, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(theme);
      });

      it('should return initial array if no Theme is added', () => {
        const themeCollection: ITheme[] = [sampleWithRequiredData];
        expectedResult = service.addThemeToCollectionIfMissing(themeCollection, undefined, null);
        expect(expectedResult).toEqual(themeCollection);
      });
    });

    describe('compareTheme', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTheme(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTheme(entity1, entity2);
        const compareResult2 = service.compareTheme(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTheme(entity1, entity2);
        const compareResult2 = service.compareTheme(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTheme(entity1, entity2);
        const compareResult2 = service.compareTheme(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

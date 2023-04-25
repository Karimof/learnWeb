import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PartDetailComponent } from './part-detail.component';

describe('Part Management Detail Component', () => {
  let comp: PartDetailComponent;
  let fixture: ComponentFixture<PartDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PartDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ part: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PartDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PartDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load part on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.part).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

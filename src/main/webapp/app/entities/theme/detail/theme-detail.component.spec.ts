import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ThemeDetailComponent } from './theme-detail.component';

describe('Theme Management Detail Component', () => {
  let comp: ThemeDetailComponent;
  let fixture: ComponentFixture<ThemeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ThemeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ theme: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ThemeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ThemeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load theme on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.theme).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MediaDetailComponent } from './media-detail.component';

describe('Media Management Detail Component', () => {
  let comp: MediaDetailComponent;
  let fixture: ComponentFixture<MediaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MediaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ media: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MediaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MediaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load media on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.media).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

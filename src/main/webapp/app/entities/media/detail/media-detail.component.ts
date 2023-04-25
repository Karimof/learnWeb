import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMedia } from '../media.model';

@Component({
  selector: 'jhi-media-detail',
  templateUrl: './media-detail.component.html',
})
export class MediaDetailComponent implements OnInit {
  media: IMedia | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ media }) => {
      this.media = media;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

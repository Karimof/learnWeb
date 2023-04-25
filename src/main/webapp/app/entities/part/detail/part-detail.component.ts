import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPart } from '../part.model';

@Component({
  selector: 'jhi-part-detail',
  templateUrl: './part-detail.component.html',
})
export class PartDetailComponent implements OnInit {
  part: IPart | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ part }) => {
      this.part = part;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITheme } from '../theme.model';

@Component({
  selector: 'jhi-theme-detail',
  templateUrl: './theme-detail.component.html',
})
export class ThemeDetailComponent implements OnInit {
  theme: ITheme | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ theme }) => {
      this.theme = theme;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

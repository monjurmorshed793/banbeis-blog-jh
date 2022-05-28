import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { INavigation, Navigation } from '../navigation.model';
import { NavigationService } from '../service/navigation.service';

@Component({
  selector: 'jhi-navigation-update',
  templateUrl: './navigation-update.component.html',
})
export class NavigationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    icon: [null, [Validators.required]],
    route: [],
    roles: [],
  });

  constructor(protected navigationService: NavigationService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ navigation }) => {
      this.updateForm(navigation);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const navigation = this.createFromForm();
    if (navigation.id !== undefined) {
      this.subscribeToSaveResponse(this.navigationService.update(navigation));
    } else {
      this.subscribeToSaveResponse(this.navigationService.create(navigation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INavigation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(navigation: INavigation): void {
    this.editForm.patchValue({
      id: navigation.id,
      title: navigation.title,
      icon: navigation.icon,
      route: navigation.route,
      roles: navigation.roles,
    });
  }

  protected createFromForm(): INavigation {
    return {
      ...new Navigation(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      icon: this.editForm.get(['icon'])!.value,
      route: this.editForm.get(['route'])!.value,
      roles: this.editForm.get(['roles'])!.value,
    };
  }
}

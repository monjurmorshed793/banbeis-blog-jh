import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INavigation } from '../navigation.model';
import { NavigationService } from '../service/navigation.service';
import { NavigationDeleteDialogComponent } from '../delete/navigation-delete-dialog.component';

@Component({
  selector: 'jhi-navigation',
  templateUrl: './navigation.component.html',
})
export class NavigationComponent implements OnInit {
  navigations?: INavigation[];
  isLoading = false;

  constructor(protected navigationService: NavigationService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.navigationService.query().subscribe({
      next: (res: HttpResponse<INavigation[]>) => {
        this.isLoading = false;
        this.navigations = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: INavigation): string {
    return item.id!;
  }

  delete(navigation: INavigation): void {
    const modalRef = this.modalService.open(NavigationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.navigation = navigation;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

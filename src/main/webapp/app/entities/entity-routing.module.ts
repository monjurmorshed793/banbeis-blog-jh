import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'navigation',
        data: { pageTitle: 'banbeisBlogApp.navigation.home.title' },
        loadChildren: () => import('./navigation/navigation.module').then(m => m.NavigationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

import {RouterModule, Routes} from "@angular/router";
import {ClientComponent} from "./home/client/client.component";
import {AdminComponent} from "./home/admin/admin.component";
import {NgModule} from "@angular/core";
import {NotFoundComponent} from "../../shared/components/not-found/not-found.component";
import {hasRole} from "../../core/guard/authorization/authorization.guard";

export const routes: Routes = [
  {
    path: 'admin',
    canActivate: [hasRole],
    component: AdminComponent,
    data: {
      requiredRoles: ['ROLE_ADMIN']
    }
  },
  {
    path: 'client',
    canActivate: [hasRole],
    component: ClientComponent,
    data: {
      requiredRoles: ['ROLE_USER']
    }
  },
  {
    path: '**',
    component: NotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule {
}

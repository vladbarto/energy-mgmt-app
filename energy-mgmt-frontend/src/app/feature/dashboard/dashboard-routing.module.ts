import {RouterModule, Routes} from "@angular/router";
import {ClientComponent} from "./home/client/client.component";
import {AdminComponent} from "./home/admin/admin.component";
import {NgModule} from "@angular/core";
import {NotFoundComponent} from "../../shared/components/not-found/not-found.component";
import {hasRole} from "../../core/guard/authorization/authorization.guard";
import {UsersComponent} from "./home/admin/users/users.component";
import {DevicesComponent} from "./home/admin/devices/devices.component";

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

  // {
  //   path: 'client/push',
  //   canActivate: [hasRole],
  //   component: PushComponent,
  //   data: {
  //     requiredRoles: ['ROLE_USER']
  //   }
  // },

  {
    path: 'admin/users',
    canActivate: [hasRole],
    component: UsersComponent,
    data: {
      requiredRoles: ['ROLE_ADMIN']
    }
  },
  {
    path: 'admin/devices',
    canActivate: [hasRole],
    component: DevicesComponent,
    data: {
      requiredRoles: ['ROLE_ADMIN']
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

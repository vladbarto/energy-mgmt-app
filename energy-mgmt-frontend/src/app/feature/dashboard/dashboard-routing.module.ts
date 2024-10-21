import {RouterModule, Routes} from "@angular/router";
import {ClientComponent} from "./home/client/client.component";
import {AdminComponent} from "./home/admin/admin.component";
import {NgModule} from "@angular/core";
import {NotFoundComponent} from "../../shared/components/not-found/not-found.component";

export const routes: Routes = [
  {
    path: 'client',
    component: ClientComponent
  },
  {
    path: 'admin',
    component: AdminComponent
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

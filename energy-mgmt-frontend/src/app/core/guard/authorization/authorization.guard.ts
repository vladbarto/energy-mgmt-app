import { inject } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivateFn, Router} from '@angular/router';
import { UserModel } from '../../../shared/models/user.model';

export const hasRole: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const router: Router = inject(Router);
  const requiredRoles: string[] = route.data['requiredRoles'];
  const loggedUser: UserModel = getUser();

  return requiredRoles.includes(getUserRole(loggedUser))
    ? true
    : router.navigateByUrl('/dashboard/invalid-access');
};

const getUser = (): UserModel => {
  return JSON.parse(localStorage.getItem('loggedUser') || '');
};

const getUserRole = (loggedUser: UserModel) : string => {
  console.log(loggedUser);
  return loggedUser.admin
    ? 'ROLE_ADMIN'
    : 'ROLE_USER';
}

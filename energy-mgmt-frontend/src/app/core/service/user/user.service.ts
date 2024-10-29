import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import { UserModel } from "../../../shared/models/user.model";
import {environment} from "../../../../environments/environment.development"
@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  getInfo(): Observable<UserModel> {
    return this.http.get<UserModel>('user/v1/info');
  }

  getAll(): Observable<UserModel[]> {
    return this.http.get<UserModel[]>('user/v1/all');
  }

  getByUsername(username: string): Observable<UserModel> {
    return this.http.get<UserModel>(`user/v1/${username}`);
  }

  insert(user: UserModel): Observable<UserModel> {
    return this.http.post<UserModel>(`user/v1/one`, user);
  }

  deleteById(userId: string): Observable<UserModel> {
    return this.http.delete<UserModel>(`user/v1/${userId}`);
  }

  update(user: UserModel, userId: string | undefined): Observable<UserModel> {
    return this.http.put<UserModel>(`user/v1/${userId}`, user);
  }

  // register(newUser: UserModel): Observable<UserModel> {
  //   return this.http.post<UserModel>(`user/v1/one`, newUser);
  // }
}

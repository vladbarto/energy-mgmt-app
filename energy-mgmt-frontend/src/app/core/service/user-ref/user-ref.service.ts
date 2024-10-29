import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserRefModel} from "../../../shared/models/userRef.model";

@Injectable({
  providedIn: 'root'
})
export class UserRefService {

  constructor(private http: HttpClient) { }

  delete(userId: string): Observable<UserRefModel> {
    console.log("it's launched!");
    return this.http.delete<UserRefModel>(`userRef/v1/${userId}`);
  }
}

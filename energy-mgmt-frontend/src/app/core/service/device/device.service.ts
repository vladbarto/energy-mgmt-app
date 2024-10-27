import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {DeviceModel} from "../../../shared/models/device.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DeviceService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<DeviceModel[]> {
    return this.http.get<DeviceModel[]>(`device/v1/all`, {
      params: { }
    }); //TODO: change path??
  }

  getAllOfLoggedUser(id: string): Observable<DeviceModel[]> {
    return this.http.get<DeviceModel[]>(`device/v1/user`, {
      params: { id: id }
    });
  }

  insert(device: DeviceModel): Observable<DeviceModel> {
    return this.http.post<DeviceModel>(`device/v1/one`, device);
  }
}

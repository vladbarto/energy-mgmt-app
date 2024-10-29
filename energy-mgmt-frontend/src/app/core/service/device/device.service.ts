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
    });
  }

  getAll_2(mhec: number = 0): Observable<DeviceModel[]> {
    return this.http.get<DeviceModel[]>(`device/v1/all/${mhec}`, {
      params: { }
    });
  }

  getAllOfLoggedUser(id: string): Observable<DeviceModel[]> {
    return this.http.get<DeviceModel[]>(`device/v1/device`, {
      params: { id: id }
    });
  }

  insert(device: DeviceModel): Observable<DeviceModel> {
    return this.http.post<DeviceModel>(`device/v1/one`, device);
  }

  update(device: DeviceModel, deviceId: string | undefined): Observable<DeviceModel> {
    return this.http.put<DeviceModel>(`device/v1/${deviceId}`, device);
  }

  deleteById(deviceId: string): Observable<DeviceModel> {
    return this.http.delete<DeviceModel>(`device/v1/${deviceId}`);
  }
}

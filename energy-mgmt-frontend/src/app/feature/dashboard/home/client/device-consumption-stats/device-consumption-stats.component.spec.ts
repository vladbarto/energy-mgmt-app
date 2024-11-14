import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeviceConsumptionStatsComponent } from './device-consumption-stats.component';

describe('DeviceConsumptionStatsComponent', () => {
  let component: DeviceConsumptionStatsComponent;
  let fixture: ComponentFixture<DeviceConsumptionStatsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeviceConsumptionStatsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DeviceConsumptionStatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

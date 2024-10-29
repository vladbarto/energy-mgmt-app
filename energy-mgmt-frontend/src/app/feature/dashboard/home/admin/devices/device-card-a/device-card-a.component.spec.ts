import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeviceCardAComponent } from './device-card-a.component';

describe('DeviceCardAComponent', () => {
  let component: DeviceCardAComponent;
  let fixture: ComponentFixture<DeviceCardAComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeviceCardAComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DeviceCardAComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

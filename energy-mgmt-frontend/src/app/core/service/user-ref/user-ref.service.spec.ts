import { TestBed } from '@angular/core/testing';

import { UserRefService } from './user-ref.service';

describe('UserrefService', () => {
  let service: UserRefService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserRefService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

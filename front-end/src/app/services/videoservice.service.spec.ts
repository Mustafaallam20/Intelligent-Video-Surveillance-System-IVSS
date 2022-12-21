import { TestBed } from '@angular/core/testing';

import { VideoserviceService } from './videoservice.service';

describe('VideoserviceService', () => {
  let service: VideoserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VideoserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { TestBed } from '@angular/core/testing';
import { Resetpassword } from './reset-password';

describe('Resetpassword', () => {
  let service: Resetpassword;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Resetpassword);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

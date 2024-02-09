import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CounterpartyComponent } from './counterparty.component';

describe('CounterpartyComponent', () => {
  let component: CounterpartyComponent;
  let fixture: ComponentFixture<CounterpartyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CounterpartyComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CounterpartyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

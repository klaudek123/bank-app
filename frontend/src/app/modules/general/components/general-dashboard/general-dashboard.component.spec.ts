import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GeneralDashboardComponent } from './general-dashboard.component';

describe('GeneralDashboardComponent', () => {
  let component: GeneralDashboardComponent;
  let fixture: ComponentFixture<GeneralDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GeneralDashboardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GeneralDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

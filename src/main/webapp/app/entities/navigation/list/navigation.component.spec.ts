import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { NavigationService } from '../service/navigation.service';

import { NavigationComponent } from './navigation.component';

describe('Navigation Management Component', () => {
  let comp: NavigationComponent;
  let fixture: ComponentFixture<NavigationComponent>;
  let service: NavigationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [NavigationComponent],
    })
      .overrideTemplate(NavigationComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NavigationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(NavigationService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.navigations?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NavigationService } from '../service/navigation.service';
import { INavigation, Navigation } from '../navigation.model';

import { NavigationUpdateComponent } from './navigation-update.component';

describe('Navigation Management Update Component', () => {
  let comp: NavigationUpdateComponent;
  let fixture: ComponentFixture<NavigationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let navigationService: NavigationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NavigationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(NavigationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NavigationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    navigationService = TestBed.inject(NavigationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const navigation: INavigation = { id: 'CBA' };

      activatedRoute.data = of({ navigation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(navigation));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Navigation>>();
      const navigation = { id: 'ABC' };
      jest.spyOn(navigationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ navigation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: navigation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(navigationService.update).toHaveBeenCalledWith(navigation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Navigation>>();
      const navigation = new Navigation();
      jest.spyOn(navigationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ navigation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: navigation }));
      saveSubject.complete();

      // THEN
      expect(navigationService.create).toHaveBeenCalledWith(navigation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Navigation>>();
      const navigation = { id: 'ABC' };
      jest.spyOn(navigationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ navigation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(navigationService.update).toHaveBeenCalledWith(navigation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

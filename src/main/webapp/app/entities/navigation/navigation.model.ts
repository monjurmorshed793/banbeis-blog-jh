import { ISubNavigation } from 'app/entities/sub-navigation/sub-navigation.model';

export interface INavigation {
  id?: string;
  title?: string;
  icon?: string;
  route?: string | null;
  roles?: string | null;
  submenus?: ISubNavigation[] | null;
}

export class Navigation implements INavigation {
  constructor(
    public id?: string,
    public title?: string,
    public icon?: string,
    public route?: string | null,
    public roles?: string | null,
    public submenus?: ISubNavigation[] | null
  ) {}
}

export function getNavigationIdentifier(navigation: INavigation): string | undefined {
  return navigation.id;
}

import { INavigation } from 'app/entities/navigation/navigation.model';

export interface ISubNavigation {
  title?: string;
  icon?: string;
  route?: string | null;
  roles?: string | null;
  navigation?: INavigation | null;
}

export class SubNavigation implements ISubNavigation {
  constructor(
    public title?: string,
    public icon?: string,
    public route?: string | null,
    public roles?: string | null,
    public navigation?: INavigation | null
  ) {}
}

import * as React from 'react';
import { mount, route } from 'navi';

export const routes = mount({
  '/': route({
    title: 'todo app',
    view: <>Todo App Works!</>,
  }),
});

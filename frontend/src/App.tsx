import React, { Component, Suspense } from 'react';
import { Router, View } from 'react-navi';

import { routes } from './routes';
import { Layout } from './Layout';
class App extends Component {
  render = () => (
    <Router routes={routes}>
      <Layout>
        <Suspense fallback={null}>
          <View />
        </Suspense>
      </Layout>
    </Router>
  );
}

export default App;

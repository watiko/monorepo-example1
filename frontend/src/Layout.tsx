import React from 'react';
import { Link, NotFoundBoundary } from 'react-navi';
import { Grid, Box } from 'grommet';

function renderNotFound() {
  return (
    <div className="Layout-error">
      <h1>404 - Not Found</h1>
    </div>
  );
}

export const Layout: React.SFC<{ children: any }> = ({ children }) => {
  return (
    <div className="Layout">
      <Grid
        rows={['xsmall', 'large']}
        columns={['small', 'auto']}
        gap="small"
        areas={[
          { name: 'header', start: [0, 0], end: [1, 0] },
          { name: 'main', start: [0, 1], end: [1, 1] },
        ]}
      >
        <Box gridArea="header" background="brand">
          <header>
            <h1 className="Layout-title">
              <Link href="/">Todo App</Link>
            </h1>
          </header>
        </Box>
        <Box gridArea="main" background="light-2">
          <main>
            <NotFoundBoundary render={renderNotFound}>{children}</NotFoundBoundary>
          </main>
        </Box>
      </Grid>
    </div>
  );
};

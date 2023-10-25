import Home from './Home';
import Login from './Login';
import Registration from './Registration';

interface RouteConfig {
  path: string;
  element: React.ReactNode;
}

const routes: RouteConfig[] = [
  { path: '/', element: <Home /> },
  { path: '/Login', element: <Login /> },
  { path: '/Registration', element: <Registration /> },
];

export type { RouteConfig };
export default routes;
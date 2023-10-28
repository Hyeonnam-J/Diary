import Home from './Home';
import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';

interface RouteConfig {
  path: string;
  element: React.ReactNode;
}

const routes: RouteConfig[] = [
  { path: '/', element: <Home /> },
  { path: '/SignIn', element: <SignIn /> },
  { path: '/SignUp', element: <SignUp /> },
];

export type { RouteConfig };
export default routes;
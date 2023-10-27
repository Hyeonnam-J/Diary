import Home from './Home';
import SignIn from './SignIn';
import SignUp from './SignUp';

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
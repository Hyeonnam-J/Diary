import Home from './Home';
import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';
import Board from './pages/Board';

interface RouteConfig {
  path: string;
  element: React.ReactNode;
}

const routes: RouteConfig[] = [
  { path: '/', element: <Home /> },
  { path: '/SignIn', element: <SignIn /> },
  { path: '/SignUp', element: <SignUp /> },
  { path: '/Board', element: <Board /> },
];

export type { RouteConfig };
export default routes;
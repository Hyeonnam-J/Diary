import Home from './Home';
import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';
import FreeBoard from './pages/FreeBoard';
import FreeBoardPostWrite from './pages/FreeBoardPostWrite';
import FreeBoardPostRead from './pages/FreeBoardPostRead';
import FreeBoardPostReply from './pages/FreeBoardPostReply';
import FreeBoardPostUpdate from './pages/FreeBoardPostUpdate';

interface RouteConfig {
    path: string;
    element: React.ReactNode;
}

const routes: RouteConfig[] = [
    { path: '/', element: <Home /> },   
    { path: '/signIn', element: <SignIn /> },
    { path: '/signUp', element: <SignUp /> },
    { path: '/freeBoard', element: <FreeBoard /> },
    { path: '/freeBoard/post/write', element: <FreeBoardPostWrite /> },
    { path: '/freeBoard/post/read', element: <FreeBoardPostRead /> },
    { path: '/freeBoard/post/reply', element: <FreeBoardPostReply /> },
    { path: '/freeBoard/post/update', element: <FreeBoardPostUpdate /> },
];

export type { RouteConfig };
export default routes;
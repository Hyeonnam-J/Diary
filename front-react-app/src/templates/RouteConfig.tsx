import Home from './Home';
import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';
import Board from './pages/Board';
import BoardPostWrite from './pages/BoardPostWrite';
import BoardPostRead from './pages/BoardPostRead';
import BoardPostReply from './pages/BoardPostReply';
import BoardPostUpdate from './pages/BoardPostUpdate';

interface RouteConfig {
    path: string;
    element: React.ReactNode;
}

const routes: RouteConfig[] = [
    { path: '/', element: <Home /> },   
    { path: '/signIn', element: <SignIn /> },
    { path: '/signUp', element: <SignUp /> },
    { path: '/board', element: <Board /> },
    { path: '/board/post/write', element: <BoardPostWrite /> },
    { path: '/board/post/read', element: <BoardPostRead /> },
    { path: '/board/post/reply', element: <BoardPostReply /> },
    { path: '/board/post/update', element: <BoardPostUpdate /> },
];

export type { RouteConfig };
export default routes;
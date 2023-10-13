import 'bootstrap/dist/css/bootstrap.min.css';
import '../css/common/common.css';
import '../css/layouts/header.css';

import Button from 'react-bootstrap/Button';

const Header = () => {
  return (
    <header>
      <div id='info'>
        <Button variant='primary'>로그인</Button>
        <Button variant='outline-primary'>회원가입</Button>
      </div>
    </header>
  )
}

export default Header
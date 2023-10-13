import 'bootstrap/dist/css/bootstrap.min.css';
import '../css/common/common.css';
import '../css/layouts/nav.css';

const Navi = () => {
  return (
    <nav>
      <ul>
        <li><a href="#">메뉴 항목 1</a></li>
        <li><a href="#">메뉴 항목 2</a></li>
        <li><a href="#">메뉴 항목 3</a></li>
        <li><a href="#">메뉴 항목 4</a></li>
      </ul>
    </nav>
  );
}

export default Navi;
import DefaultLayout from './layouts/DefaultLayout';
// import React, { useState, useEffect } from 'react';

function Home() {
  /*
  // const data = useState(0)[0];
  // const setData = useState(0)[1];
  const [count, setCount] = useState(0);

  const [data, setData] = useState('');

  // 의존성 배열 
  // 비었으면 처음 마운트될 때 동작
  // 값이 있으면 해당 값이 변경될 때마다 동작
  useEffect(() => {
    fetchData();
    alert('!');
  }, [count]);

  const fetchData = async (): Promise<void> => {
    const response = await fetch('http://localhost:8080/hello', {
        method: 'GET',
        mode: 'cors'
    });
    const result: string = await response.text();
    setData(result);  // setData를 사용하여 업데이트하는 이유는 옵저버 패턴으로 바뀐 값만 렌더링하여 성능을 최적화하기 위함입니다.
  };

  const handlePlusClick = () => {
    setCount(count + 1);
  };
  */
  
  return (
    <DefaultLayout>
      <section>
        {/* <p id='count'>{count}</p>
        <button id='plus' onClick={handlePlusClick}>+</button>

        <p id='data'>{data}</p> */}
      </section>
    </DefaultLayout>
  );
}

export default Home;

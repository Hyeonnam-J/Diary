// node_modules 안에 있는 것은 다이렉트로 import.
// 그 외는 src가 루트. ./ 이 기호로 시작해야 한다.
import React, { useState, useEffect } from 'react';
import DefaultLayout from './templates/DefaultLayout';

function App() {
  // const data = useState(null)[0];
  // const setData = useState(null)[1];
  const [data, setData] = useState(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const response = await fetch('/hello');
      const result = await response.text();
      setData(result);  // setData를 써서 업데이트 하는 이유는 옵저버 패턴으로 바뀐 값만 렌더링하여 성능을 최적화하기 위함.
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  return (
    <div>
      <h1>Data from Server:</h1>
      <pre>
        <DefaultLayout>
            {data}
        </DefaultLayout>
      </pre>
    </div>
  );
}

export default App;
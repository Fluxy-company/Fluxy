import { useState } from 'react'
import RouterDom from './routes.jsx'


function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <RouterDom />
      <script src="./js/util.js"></script>
    </>
  )
}

export default App

import { RouterProvider } from 'react-router-dom'
import {routes} from "./provider/route"

export default function App() {
  return (
    <RouterProvider router={routes}/>
  );
}

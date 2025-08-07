
import { createRoot } from 'react-dom/client'
import './index.css'

import { BrowserRouter, Routes, Route } from 'react-router'
import Login from './pages/login.jsx'
import Register from './pages/register.jsx'
import Navbar from './pages/navbar.jsx'
import UserList from './pages/users.jsx'


createRoot(document.getElementById('root')).render(
 <BrowserRouter>
 <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path='/users' element={<><Navbar/> <UserList/></>}/>
      <Route path="/users/update/:id" element={<Register />} />
 </Routes>
 
 </BrowserRouter>
)

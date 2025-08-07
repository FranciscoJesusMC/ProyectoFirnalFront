import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';
import { useEffect, useState } from 'react';

export default function Navbar() {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  // Detectar token al cargar el navbar
  useEffect(() => {
    const token = localStorage.getItem('token');
    setIsLoggedIn(!!token);
  }, []);

  const handleLogout = async () => {
    const confirm = await Swal.fire({
      title: '¿Cerrar sesion?',
      text: '¿Deseas salir de la aplicacion?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Si',
      cancelButtonText: 'Cancelar',
    });

    if (confirm.isConfirmed) {
      localStorage.removeItem('token');
      Swal.fire({
        icon: 'success',
        title: 'Sesion cerrada',
        text: 'Has salido de la aplicacion',
        timer: 1000,
        showConfirmButton: false,
      });
      setIsLoggedIn(false);
      navigate('/');
    }
  };

  return (
    <nav className="bg-gray-800 text-white px-6 py-4 flex justify-between items-center">
      <h1 className="text-lg font-bold">Control de usuarios</h1>

      <div className="flex gap-4 items-center">
        {isLoggedIn && (
          <>
            <button
              onClick={() => navigate('/register')}
              className="bg-blue-500 hover:bg-blue-600 px-4 py-2 rounded text-sm"
            >
              Crear
            </button>

            <button
              onClick={handleLogout}
              className="bg-red-500 hover:bg-red-600 px-4 py-2 rounded text-sm"
            >
              Salir
            </button>
          </>
        )}
      </div>
    </nav>
  );
};
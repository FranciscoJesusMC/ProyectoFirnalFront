import { useEffect, useState } from 'react';
import { getAllUsers, deleteUser } from '../services/userService';
import Swal from 'sweetalert2';
import { getDecodedToken } from '../utils/auth';

export default function Users() {
  const [users, setUsers] = useState([]);
  const decoded = getDecodedToken();
  const isAdmin = decoded?.isAdmin === true;

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      const data = await getAllUsers();
      setUsers(data);
    } catch (err) {
      console.error(err);
      Swal.fire('Error', 'Error al cargar usuarios', 'error');
    }
  };

  const handleDelete = async (id) => {
    const confirm = await Swal.fire({
      title: '¿Estás seguro?',
      text: 'No podrás revertir esta acción',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí',
      cancelButtonText: 'Cancelar'
    });

    if (confirm.isConfirmed) {
      try {
        await deleteUser(id);
        setUsers(users.filter(user => user.id !== id));

        Swal.fire('Eliminado', 'Usario eliminado correctamente', 'success');
      } catch (err) {
        console.error(err);
        Swal.fire('Error', 'No se pudo eliminar al usuario', 'error');
      }
    }
  };

  
  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold text-center mb-6">Users</h2>

      <table className="min-w-full bg-white shadow-md rounded">
        <thead className="bg-gray-800 text-white">
          <tr>
            <th className="py-2 px-4">Id</th>
            <th className="py-2 px-4">Name</th>
            <th className="py-2 px-4">Lastname</th>
            <th className="py-2 px-4">Email</th>
            <th className="py-2 px-4">Cellphone</th>
            <th className="py-2 px-4">Username</th>
            <th className="py-2 px-4">DNI</th>
            <th className="py-2 px-4">Remove</th>
            <th className="py-2 px-4">Update</th>
          </tr>
        </thead>
        <tbody>
          {users.map(user => (
            <tr key={user.id} className="text-center border-b hover:bg-gray-100">
              <td className="py-2 px-4">{user.id}</td>
              <td className="py-2 px-4">{user.name}</td>
              <td className="py-2 px-4">{user.lastname}</td>
              <td className="py-2 px-4">{user.email}</td>
              <td className="py-2 px-4">{user.cellphone}</td>
              <td className="py-2 px-4">{user.username}</td>
              <td className="py-2 px-4">{user.dni}</td>
              <td className="py-2 px-4">
                {isAdmin && (
                <button
                  onClick={() => handleDelete(user.id)}
                  className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 text-sm"
                >
                  Delete
                </button>
                )}
              </td>
              <td className="py-2 px-4">
                <a
                  href={`/users/update/${user.id}`}
                  className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 text-sm"
                >
                  Update
                </a>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

const API_BASE = 'http://localhost:8080/api/user';

 const token = localStorage.getItem('token');

// Obtener todos los usuarios
export const getAllUsers = async () => {
  const res = await fetch(API_BASE);
  if (!res.ok) throw new Error('Error al obtener usuarios');
  return await res.json();
};

//Obtener usuario por id
export const getUserById = async (id) => {
  const token = localStorage.getItem('token');
  const res = await fetch(`${API_BASE}/${id}`, {
    headers: { 'Authorization': `Bearer ${token}` }
  });

  if (!res.ok) throw new Error('No se pudo cargar el usuario');
  return res.json();
};

//crear usuario
export const createUser = async (data) => {
    const res = await fetch(`${API_BASE}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });

    if (!res.ok) {
    const text = await res.text();
    throw new Error(text || 'Error al actualizar el usuario');
    }

    return null;

};

// Eliminar un usuario
export const deleteUser = async (id) => {
  const res = await fetch(`${API_BASE}/${id}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  if (!res.ok) throw new Error('Error al eliminar usuario');
};

//Actualizar usuario
export async function updateUser(id, user) {
  const res = await fetch(`${API_BASE}/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(user),
  });

  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || 'Error al actualizar el usuario');
  }
  
  return null;
}


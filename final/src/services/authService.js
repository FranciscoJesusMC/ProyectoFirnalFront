const API_BASE = 'http://localhost:8080';

//Metodo para logearse
export const login = async (data) => {
  const response = await fetch(`${API_BASE}/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      username: data.username,
      password: data.password,
    }),
  });

  if (!response.ok) throw new Error('Credenciales inválidas');
  return response.json();
};



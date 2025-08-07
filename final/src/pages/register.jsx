import { useForm } from 'react-hook-form';
import { useNavigate,useParams } from 'react-router-dom';
import Swal from 'sweetalert2';
import { useEffect } from 'react';
import { updateUser, getUserById,createUser} from '../services/userService';

export default function Register() {
  const { register, handleSubmit,setValue,setError, formState: { errors } } = useForm();
  const navigate = useNavigate();
  const { id } = useParams();

    useEffect(() => {
    if (id) {
      getUserById(id).then(user => {
        Object.entries(user).forEach(([key, value]) => {
          setValue(key, value);
        });
      }).catch(() => {
        Swal.fire('Error', 'No se pudo obtener al usuario', 'error');
      });
    }
  }, [id, setValue]);

  const onSubmit = async (data) => {
    try {
      if (id) {
        await updateUser(id, data);
        Swal.fire('Actualizado', 'Usuario actualizado correctamente', 'success');
      } else {
        await createUser(data);
        Swal.fire('Registrado', 'Usuario creado correctamente', 'success');
      }

      navigate('/users');
    } catch (err) {
      if (err.errors) {
        Object.entries(err.errors).forEach(([field, message]) => {
          setError(field, { type: 'manual', message });
        });
      } else {
        Swal.fire('Error', err.message, 'error');
      }
    }
  
};


  return (
    <form onSubmit={handleSubmit(onSubmit)} className="bg-white p-8 shadow-md max-w-lg mx-auto mt-6 rounded space-y-4">
      <h2 className="text-xl font-bold text-center">{id ? 'Actualizar Usuario' : 'Registrar Usuario'}</h2>

      <div>
        <label>Nombre</label>
        <input {...register('name', { required: 'Nombre requerido' })} className="w-full border p-2 rounded" />
        {errors.name && <p className="text-red-500 text-sm">{errors.name.message}</p>}
      </div>

      <div>
        <label>Apellido</label>
        <input {...register('lastname', { required: 'Apellido requerido' })} className="w-full border p-2 rounded" />
        {errors.lastname && <p className="text-red-500 text-sm">{errors.lastname.message}</p>}
      </div>

      <div>
        <label>Correo</label>
        <input type="email" {...register('email', { required: 'Correo requerido' })} className="w-full border p-2 rounded" />
        {errors.email && <p className="text-red-500 text-sm">{errors.email.message}</p>}
      </div>

      <div>
        <label>Dirección</label>
        <input {...register('address', { required: 'Dirección requerida' })} className="w-full border p-2 rounded" />
        {errors.address && <p className="text-red-500 text-sm">{errors.address.message}</p>}
      </div>

      <div>
        <label>Celular</label>
        <input {...register('cellphone', { required: 'Celular requerido' })} className="w-full border p-2 rounded" />
        {errors.cellphone && <p className="text-red-500 text-sm">{errors.cellphone.message}</p>}
      </div>

      <div>
        <label>DNI</label>
        <input {...register('dni', { required: 'DNI requerido' })} className="w-full border p-2 rounded" />
        {errors.dni && <p className="text-red-500 text-sm">{errors.dni.message}</p>}
      </div>

      <div>
        <label>Usuario</label>
        <input {...register('username', { required: 'Usuario requerido' })} className="w-full border p-2 rounded" />
        {errors.username && <p className="text-red-500 text-sm">{errors.username.message}</p>}
      </div>

  
      {!id && (
        <div>
          <label>Contraseña</label>
          <input type="password" {...register('password', { required: 'Contraseña requerida' })} className="w-full border p-2 rounded" />
          {errors.password && <p className="text-red-500 text-sm">{errors.password.message}</p>}
        </div>
      )}

      <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700">
        {id ? 'Actualizar' : 'Registrar'}
      </button>

      <button 
      onClick={() => navigate('/users')}
      type="submit" className="w-full bg-red-500 text-white py-2 rounded hover:bg-red-600">
        Atras
      </button>
    </form>
  );
}

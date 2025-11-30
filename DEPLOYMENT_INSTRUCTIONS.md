# Guía de Despliegue en Render

He preparado tu proyecto para que sea fácil de desplegar en Render. Aquí tienes los pasos a seguir:

## 1. Preparación (Ya realizada)
He creado dos archivos importantes en tu carpeta `municipalidad`:
- **Dockerfile**: Contiene las instrucciones para construir y ejecutar tu aplicación Java.
- **render.yaml**: Un archivo de configuración que ayuda a Render a entender tu proyecto.

También he actualizado tu frontend para que sea fácil de conectar:
- He creado `src/environments/environment.prod.ts` para la configuración de producción.
- He refactorizado los servicios para usar esta configuración.

## 2. Subir cambios a GitHub
Para que Render pueda acceder a tu código, necesitas subir estos nuevos archivos a tu repositorio de GitHub.

```bash
git add .
git commit -m "Configuración para despliegue y conexión frontend-backend"
git push origin main
```

## 3. Crear el servicio Backend en Render
1. Ve a [dashboard.render.com](https://dashboard.render.com/).
2. Haz clic en **New +** y selecciona **Web Service**.
3. Conecta tu repositorio de GitHub.
4. Render detectará automáticamente el `Dockerfile` en la carpeta `municipalidad`.
5. Asegúrate de que la configuración sea:
   - **Runtime**: Docker
   - **Region**: Oregon (US West) u Ohio (US East).
   - **Branch**: main.
6. Haz clic en **Create Web Service**.

### Variables de Entorno (Backend)
En la pestaña **Environment** de tu servicio en Render, añade:
- `SPRING_DATASOURCE_URL`: (Tu URL de TiDB)
- `SPRING_DATASOURCE_USERNAME`: (Tu usuario de TiDB)
- `SPRING_DATASOURCE_PASSWORD`: (Tu contraseña de TiDB)
- `APPLICATION_SECURITY_JWT_SECRET_KEY`: (Tu clave secreta JWT)

## 4. Conectar el Frontend
Una vez que tu backend esté desplegado en Render, obtendrás una URL (ej: `https://municipalidad-backend.onrender.com`).

1. Abre el archivo `frontend/muni-front/src/environments/environment.prod.ts`.
2. Actualiza `apiUrl` con la URL real de tu backend en Render:
   ```typescript
   export const environment = {
       production: true,
       apiUrl: 'https://tu-backend-en-render.onrender.com/api/v1', // <--- Poner URL real aquí
       culqiPublicKey: '...'
   };
   ```
3. Guarda los cambios, haz commit y push a GitHub.
4. Vercel (donde tienes tu frontend) debería detectar los cambios y volver a desplegar automáticamente.

¡Listo! Tu frontend en Vercel ahora se comunicará con tu backend en Render.

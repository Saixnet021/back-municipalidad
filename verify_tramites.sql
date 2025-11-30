-- Script SQL para verificar las tablas generadas por JPA Inheritance (JOINED)

-- Verificar tabla padre
SELECT * FROM tramite;

-- Verificar tabla hija: Mesa de Partes
SELECT t.*, tmp.asunto, tmp.descripcion, tmp.ruta_archivo
FROM tramite t
INNER JOIN tramite_mesa_partes tmp ON t.id = tmp.id
WHERE t.tipo_tramite = 'MESA_PARTES';

-- Verificar tabla hija: Constancia
SELECT t.*, tc.direccion, tc.tiempo_residencia, tc.recibo_servicio_url
FROM tramite t
INNER JOIN tramite_constancia tc ON t.id = tc.id
WHERE t.tipo_tramite = 'CONSTANCIA';

-- Verificar tabla hija: Licencia
SELECT t.*, tl.nombre_negocio, tl.giro, tl.area, tl.zonificacion
FROM tramite t
INNER JOIN tramite_licencia tl ON t.id = tl.id
WHERE t.tipo_tramite = 'LICENCIA';

-- Contar trámites por tipo
SELECT tipo_tramite, COUNT(*) as total
FROM tramite
GROUP BY tipo_tramite;

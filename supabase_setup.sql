-- 📸 Configuración de Supabase Storage para VetAPI
-- Ejecutar este script en el SQL Editor de Supabase

-- 1. Crear el bucket para fotos de usuarios
INSERT INTO storage.buckets (id, name, public, file_size_limit, allowed_mime_types)
VALUES (
  'user-photos', 
  'user-photos', 
  true, 
  10485760, -- 10MB en bytes
  ARRAY['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp']
);

-- 2. Política para permitir subir archivos (INSERT)
CREATE POLICY "Allow upload user photos" ON storage.objects
FOR INSERT WITH CHECK (
  bucket_id = 'user-photos'
);

-- 3. Política para permitir leer archivos (SELECT)
CREATE POLICY "Allow read user photos" ON storage.objects
FOR SELECT USING (
  bucket_id = 'user-photos'
);

-- 4. Política para permitir actualizar archivos (UPDATE)
CREATE POLICY "Allow update user photos" ON storage.objects
FOR UPDATE USING (
  bucket_id = 'user-photos'
);

-- 5. Política para permitir eliminar archivos (DELETE)
CREATE POLICY "Allow delete user photos" ON storage.objects
FOR DELETE USING (
  bucket_id = 'user-photos'
);

-- 6. Verificar que el bucket se creó correctamente
SELECT * FROM storage.buckets WHERE id = 'user-photos';

-- 7. Verificar las políticas creadas
SELECT * FROM pg_policies WHERE tablename = 'objects' AND policyname LIKE '%user photos%'; 
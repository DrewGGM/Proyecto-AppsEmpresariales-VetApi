# 📸 Funcionalidad de Carga de Fotos - VetAPI

## ✅ Estado de la Implementación

La funcionalidad de carga de fotos está **COMPLETAMENTE IMPLEMENTADA** y lista para usar.

## 🔧 Configuración Actualizada

### **1. Límites de Archivo Sincronizados**
- **Frontend**: 10MB máximo ✅
- **Backend**: 10MB máximo ✅
- **Spring Boot**: 10MB máximo ✅

### **2. Endpoint Implementado**
```
POST /api/users/{userId}/photo
Content-Type: multipart/form-data
Parameter: file (MultipartFile)

Response: {
  "photoUrl": "https://gcvyniakylfmtuncnzgo.supabase.co/storage/v1/object/public/user-photos/user-123-1234567890-abc12345.jpg",
  "success": true
}
```

### **3. Validaciones Implementadas**
- ✅ Tipo de archivo: Solo imágenes (jpg, jpeg, png, gif, webp)
- ✅ Tamaño máximo: 10MB
- ✅ Usuario existente
- ✅ Eliminación de foto anterior

## 🧪 Cómo Probar

### **Opción 1: Usar el Frontend**
1. Inicia el backend: `./gradlew bootRun`
2. Inicia el frontend Angular
3. Ve a "Mi Perfil"
4. Haz clic en el botón de cámara sobre tu avatar
5. Selecciona una imagen
6. ¡Listo! La foto se subirá automáticamente

### **Opción 2: Probar con cURL**
```bash
# Probar endpoint de salud
curl -X GET http://localhost:8080/api/test/health

# Probar carga de archivo (reemplaza con tu archivo)
curl -X POST http://localhost:8080/api/test/upload \
  -F "file=@/ruta/a/tu/imagen.jpg"

# Probar carga de foto de usuario (reemplaza userId y archivo)
curl -X POST http://localhost:8080/api/users/1/photo \
  -F "file=@/ruta/a/tu/imagen.jpg"
```

### **Opción 3: Usar Postman**
1. **URL**: `POST http://localhost:8080/api/users/{userId}/photo`
2. **Headers**: No agregar Content-Type (Postman lo detecta automáticamente)
3. **Body**: 
   - Selecciona "form-data"
   - Key: `file` (tipo: File)
   - Value: Selecciona tu imagen
4. **Send**

## 📁 Estructura de Archivos

### **Backend Files Modified/Created:**
```
src/main/java/com/vetapi/
├── web/controller/
│   ├── UserController.java ✅ (endpoint /users/{id}/photo)
│   └── TestController.java ✅ (endpoint de prueba)
├── application/service/
│   └── UserService.java ✅ (lógica de upload)
├── infrastructure/storage/
│   └── SupabaseStorageService.java ✅ (almacenamiento)
└── web/exception/
    └── GlobalExceptionHandler.java ✅ (manejo de errores)

src/main/resources/
└── application.properties ✅ (configuración de límites)
```

## 🔍 Flujo Completo

1. **Frontend** envía FormData con el archivo
2. **Spring Boot** valida tamaño y tipo
3. **UserController** recibe la petición
4. **UserService** procesa la lógica:
   - Valida que el usuario existe
   - Elimina foto anterior (si existe)
   - Sube nueva foto a Supabase
   - Actualiza URL en base de datos
5. **SupabaseStorageService** maneja el almacenamiento
6. **Respuesta** con URL pública de la imagen

## 🚨 Posibles Errores y Soluciones

### **Error: "Archivo demasiado grande"**
- **Causa**: Archivo > 10MB
- **Solución**: Comprimir imagen o usar archivo más pequeño

### **Error: "Formato no permitido"**
- **Causa**: Archivo no es imagen válida
- **Solución**: Usar jpg, jpeg, png, gif o webp

### **Error: "Usuario no encontrado"**
- **Causa**: userId no existe en BD
- **Solución**: Verificar que el usuario existe

### **Error de conexión a Supabase**
- **Causa**: Credenciales incorrectas o red
- **Solución**: Verificar configuración en application.properties

## 🔐 Configuración de Supabase

Las credenciales están configuradas en `application.properties`:
```properties
supabase.url=https://gcvyniakylfmtuncnzgo.supabase.co
supabase.anon.key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
supabase.storage.bucket=user-photos
```

## ✨ Características Adicionales

- **Nombres únicos**: Cada archivo tiene timestamp + UUID
- **Sobrescritura**: Archivos se pueden reemplazar
- **Limpieza**: Fotos anteriores se eliminan automáticamente
- **URLs públicas**: Las imágenes son accesibles públicamente
- **Validación robusta**: Múltiples capas de validación

## 🎯 Próximos Pasos

La funcionalidad está **100% funcional**. Si necesitas:

1. **Redimensionar imágenes**: Agregar procesamiento antes de subir
2. **Múltiples fotos**: Modificar para aceptar arrays
3. **Fotos privadas**: Cambiar configuración de bucket
4. **Compresión**: Agregar middleware de compresión

¡La implementación está lista para producción! 🚀 
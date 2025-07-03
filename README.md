# Stats-backend

Para ejecutar el proyecto en local tener instalado RabbitMq y dynamoDB local

usuario y contraseña de rabbitmq: guest

cuando se ejecuta el programa, el mismo crea la tabla en la dynamoDB, solo es necesario
que el dynamo este ejecutandose en local correctamente el el puerto 8000

ENDPOINTS

1. Guardar stats

POST http://localhost:8080/stats

body: 
{
"totalContactoClientes": 252,
"motivoReclamo": 25,
"motivoGarantia": 11,
"motivoDuda": 100,
"motivoCompra": 100,
"motivoFelicitaciones": 9,
"motivoCambio": 8,
"hash": "fda3f119e5c3805e5edb60b6d8e4812b"
}

guarda la estadistica en dynamo si el hash es valido, de lo contrario muestra un mensaje de error.

2. Obtener stats

GET http://localhost:8080/stats/2025-07-01T19:33:47.726948600Z

reemplazar el 2025-07-01T19:33:47.726948600Z por uno que exista en DB cuando se guarde una stat

3. Generar hash

POST http://localhost:8080/stats/generar

body:
{
"totalContactoClientes": 252,
"motivoReclamo": 25,
"motivoGarantia": 11,
"motivoDuda": 100,
"motivoCompra": 100,
"motivoFelicitaciones": 9,
"motivoCambio": 89
}

endpoint  adicional para generar hash con la info del body


4. Obtener mensajes de la Rabbitmq

GET http://localhost:8080/stats/mensajes

lista mensajes de la cola de rabbit

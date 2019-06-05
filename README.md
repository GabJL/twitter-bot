# SquareWarBot
Un ejemplo simple de cómo usar las clases estándar de Java para crear un bot en twitter usando el API REST de esta red social.

## Cuenta de ejemplo
En <https://twitter.com/rysd19> se muestra un ejemplo de su uso.

## Uso

1. Descargue este código.
2. Cree una cuenta de desarrollador de twitter, cree una app y obtenga las claves ([info](doc/twitter-developer.md))
3. Añada las claves en `Twitter.java`.

## Explicación

De forma rápida tenemos las siguientes clases:

* `entidades.*`: Son clases usadas para analizar las respuestas obtenidas tras las peticiones a los diferentes *endpoints* de Twitter.
* `modelo.Main`: Clase principal que se encarga de conectar el resto
* `modelo.SquareWarBot`: Clase que maneja el bot
* `modelo.Twitter`: Clase que se comunica con Twitter

### Funcionamiento del bot

### Funcionamiento del uso del API de Twitter

[En este enlace](doc/usingAPI.md) podéis una información inicial sobre cómo se usa el API de twitter a nivel de usuario.

### Código adicional

Aunque no se usa en la clase `Twitter` incluye los métodos necesarios para enviar mensajes directos. Es posible que para enviar MD necesite ajustar los permisos de la aplicación. Más información [aquí](doc/usingAPI.md)

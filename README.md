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

El bot parte de una matriz de NxN (N es configurable en la clase). Cada una de las casillas está ocupada por un jugador. En cada movimiento se elije al azar un conquistador y se examinan que posiciones de su alrededor son conquistables, es decir, está en posesión de otro jugador y existe (las prácticas de matrices de Programación sirven para algo :P). Entre las posiciones conquistables se elige una al azar. Esa información se devuelve para el que programa principal puede mostrar la información. 

### Funcionamiento del uso del API de Twitter

[En este enlace](doc/usingAPI.md) podéis una información inicial sobre cómo se usa el API de twitter a nivel de usuario.

### Código adicional

Aunque no se usa en la clase `Twitter` incluye los métodos necesarios para enviar mensajes directos. Es posible que para enviar MD necesite ajustar los permisos de la aplicación. Más información [aquí](doc/usingAPI.md)

## Disclaimer

Java (y la mayoría de los lenguajes) tiene bibliotecas externas que facilita la consulta del API de Twitter, pero el interés de este ejemplo es ver cómo usarla a bajo nivel.

También se pueden hacer muchas mejoras, pero la idea era hacer algo rápido y sencillo. Casi he tardado más en escribir estas explicaciones que programarlo (implementarlo solo ha requerido pocas horas, todo depende del conocimiento de JAVA, el API de Twitter y cómo de eficiente y rápido se sea buscando en stackoverflow :P).

Es posible que el código tenga errores, si los detectáis avisadme o haced un pull request.

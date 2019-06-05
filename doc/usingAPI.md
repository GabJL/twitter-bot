# Usando el API de twitter a nivel de usuario

En este documento se explica de forma más o menos rápida como usar el API a nivel de usuario y cómo enviar tuits.

Todos los código mencionados en este documento pueden verser en el fichero `Twitter.java`.

## Autenticación en twitter

Twitter permite varios tipos de autenticación para usar su API de forma gratuito:

* **Autenticación de aplicación**: Este es el que hemos utilizado en clase, pero es limitado, ya que solo permite hacer las operaciones sobre recursos a los que podemos acceder de forma pública (lo que podemos ver en la web sin necesidad de tener usuario de twitter). Entre estas operaciones están buscar tuits con ciertos términos, ver los tuits de cierto usuario...
* **Autenticación de usuario**: Este es el que se usa en en este caso y además de hacer las mismas operaciones que el anterior ya permite realizar operaciones que están limitadas a usuarios registrados como enviar un tuit, retuitear, seguir...

En el API de twitter (<https://developer.twitter.com/en/docs>) las operaciones que están limitadas a este segundo proceso de autenticación vienen marcadas con:

> Requires authentication? 	Yes **(user context only)**

### Proceso de autenticación de aplicación

Por completitud añado este que es el que se ha usado en las prácticas:

1. Usamos en *endpoint* <https://api.twitter.com/oauth2/token> para solicitar un token. Para ello le pasamos nuestras claves de aplicación. Las claves se pasan en la cabecera `Authentication` de HTTP y son de tipo `Basic`.
2. Si lo hicimos bien, nos devuelve el token.
3. En cada petición adicional que hagamos pasamos dicho token (este token tiene fecha de caducidad y habría que solicitar uno cada cierto tiempo). El token se pasa en la cabecera `Authenticacion` de HTTP y es de tipo `Bearer`.

### Proceso de autenticación de usuario

Aquí existen varias formas, pero por simplicidad usaremos aquel en el que sabemos las claves oAuth (que se pueden obtener en nuestra aplicación desde el portal de desarrollador de twitter) y las incrustaremos en el código. Entonces ahora tenemos 4 claves: 2 de aplicación y 2 de usuario.

En este caso no hay que hacer nada especial como en el caso previo de pedir un token, sino que con esas claves se generará una firma que añadiremos en todas las peticiones. Igual que antes esta firma se pasa en la cabecear `Authorization` pero el tipo es `OAuth`. La dificultad es que esa firma no es simple de generar (tiene un formato especial con una codificación especial y luego hay que hacerle un hash) y requiere los siguientes elementos:

* Las cuatro claves mencionadas previamente.
* El método (`GET`, `POST`, `DELETE`...) a utilizar.
* La URL a la que se va a consultar.
* Los parámetros y valores de los parámetros que se van a usar dicha petición.
* Una marca temporal que limita cuándo es válida dicha firma.

En el código se facilita el método `builAuthorization` que genera la firma completa. A ese método hay que pasarle toda la información previa salvo la que ya tiene (las claves que son atributos de clase) y la marca temporal que la puede generar en ese momento. En cada petición habrá por lo tanto que generar las siguientes variables para construir la firma:

* `method`: Método HTTP usado en la petición. En nuestro caso será `GET` o `POST` dependiendo de la operación a realizar.
* `twitter_endpoint`: URL a consultar (sin parámetros).
* `parameters`: Este quizás es el más complejo ya que hay que crear un conjunto de pares `parametro`:`valor` con todos los parámetros que requiere la consulta.

Generados esos valores llamamos al método `buildAuthorization` y su resultado lo añadimos en la cabecera `Authorization` de la petición. 

Por lo demás las peticiones se hacen igual que las peticiones realizadas en la práctica.

## Enviar un tuit con imágenes



# Crear una aplicación de Twitter

Primero debe ser un desarrollador validado por twitter. Para ello siga los siguientes pasos

1. Cree una cuenta o use una existente.
2. Entre en <https://develop.twitter.com> y dese de alta como desarrollador.
    1. Elija como tipo de desarrollador el tipo `teaching` y complete la información indicando que es usuario de la UMA.
    2. Desmarque todos los elementos, salvo quizás, la capacidad de twitter.
    3. Requiere que indique un correo y teléfono válido.
  
Una validado como desarrollador puede crear aplicaciones (menú arriba derecha -> App). Para crear una aplicación simple debe indicar:

* **App name**: Nombre de la aplicación
* **Application description**: Una breve descripción de la aplicación (es público)
* **Website URL**: El sitio web de la aplicación (puede usar cualquier cosa por ejemplo: https://www.example.com)
* **Tell us how this app will be used**: Breve comentario indicando el uso (académico en este caso). 

Es importante que **NO** marque la opción **Enable Sign in with Twitter** porque esto si requiere tener un sitio web para la aplicación.

Una vez creada la aplicación, el único paso es generar y crear las claves. Necesita generar (si no tiene la aplicación) tanto las claves de aplicación (Consumer API keys) como las de usuario (Access token & access token secret).

Esas claves debe añadirlas en el fichero `Twitter.java` con la siguiente equivalencia:

(claves.png)
  

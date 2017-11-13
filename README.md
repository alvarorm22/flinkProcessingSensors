# flinkProcessingSensors
### Implementación de una arquitectura en tiempo real para el procesamiento y control de eventos procedentes de sensores y alarmas

#### Esta aplicación es una parte de mi Proyecto Fin de Master elaborado como resultado del III Máster de Arquitecturas Big Data impartido por KSchool.

> http://kschool.com/cursos/master-en-arquitectura-big-data-madrid

#### La arquitectura completa de todo el proyecto se puede ver en la siguiente imagen:

![Arquitectura del proyecto](https://image.ibb.co/cRSmCb/arquitectura_completa.png)



El mensaje que recibe y procesa Flink tiene el siguiente aspecto:
{
"42959",
"true",
"false",
"25",
"30"
}

Los campos se corresponden respectivamente con: Id del sensor, movimiento, humo, temperatura y temperatura máxima.

#### La lógica necesaria para este sistema y que Flink deberá de implementar es la siguiente:


- Si el mensaje llega con sus campos (movimiento, humo y temperatura) correctos, se descartará el mensaje.


- Si se recibe un mensaje con el campo movimiento activo, se activará el protocolo de actuación y Flink deberá enviar un correo electrónico a un buzón de monitorización y control de alarmas indicando que esa alarma se ha activado, dicho mensaje irá compuesto por todos los datos de ese cliente, por último se deberá actualizar un panel de visualización mostrando en el mapa la alarma que se acaba de activar quedando diferenciada del resto de sensores.


- Si un mensaje llega con el campo de temperatura o de humo activo, Flink almacenará el estado de ese sensor y esperará tres mensajes con este campo activo antes de activar el protocolo de actuación y se repetirán los pasos del punto II.


- Se deberá de habilitar un punto de entrada para permitir a los sensores enviar un mensaje cuando recuperen su estado correcto después de que su alarma haya sido activada.

- Se desea visualizar en tiempo real el histórico de alarmas que han sido activadas en una determinada franja de tiempo según su tipo.

#### Para poder cumplir con estos puntos, se ha propuesto implementar la siguiente arquitectura:


![Arquitectura](https://image.ibb.co/cyTUyG/arq_flink.png)


####Los puntos de entrada a Flink son dos topics de Kafka:
- ENTRY:  Los sensores envían todos los mensajes con su estado.
- OK: Se reciben únicamente los mensajes de los sensores que han recuperado su estado normal después de haberse activado su alarma.

Los mensajes del topic ENTRY que han llegado con el campo movimiento activo o se reciben tres mensajes con el campo humo o temperatura activo, se escribirán en el topic COUNT. A su vez, se activará el protocolo de actuación y Flink lanzará una petición a cassandra para obtener todos los datos del cliente que tiene contratada esa alarma, posteriormente enviará un correo electrónico a un buzón de monitorización y se escribirá el estado de la alarma en InfluxDB para actualizar el dashboard de Grafana.

- COUNT: Por otro lado, Flink recoge los mensajes del topic COUNT, (que ya son todos con su alarma activa), le aplica un filtro para conocer el tipo del mensaje, paraleliza por su tipo (así se asegura que todos los mensajes del mismo tipo van a ir al mismo task de flink para poder obtener la suma de todos ellos), le aplica una ventana de 1 minuto, hace la suma y escribe en InfluxDB. De esta forma se crea un time series en esta bbdd con los datos históricos de la activación de alarmas según su tipo para poder visualizarlo fácilmente en Grafana a través de una gráfica.





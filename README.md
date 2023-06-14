# obligatorio_p2_2023-


-------------------------*********  07-Jun-2023 *********------------------------------

*- Se actualiza el archivo "gitignore" añadiendo nuevas especificaciones.

*- Se sube el avance del proyecto donde las consultas 2, 3, 4, 5 y 6 funcionan. La consulta 1 aún no ha sido implementada
de forma completa/exitosa.

*- Se agrega un archivo.csv llamado "f1_dataset_test_test.csv" que es una muestra de 2.000 registros a partir del archivo
brindado por Germán llamado "f1_dataset_test.csv". Se detectan inconsistencias que probablemente se deban a problemas
en los campos del archivo.csv que, si bien los mismos son atrapados por las Excepciones, afectan al output de las consultas.
En otras palabras: cuando las consultas son realizadas a partir del archivo "f1_dataset_test_test.csv" se verifican resultados
correctos de las consultas al contrastarlos con la fuente de datos. No sucede lo mismo al ser la fuente el archivo
"f1_dataset_test.csv".

*- Se incorpora al código las métricas para el tiempo de ejecución de las consultas y carga de datos así como también
la cantidad de memoria ram utilizada por cada una de ellas.

*- Se avanza en agregar los comentarios explicando el código.


-------------------------*********  08-Jun-2023 *********------------------------------

*- Se terminan de comentar/explicar el código de las funciones

*- Se detecta que el archivo "f1_dataset_test.csv" tiene un problema en el campo fecha: algunas veces aparece en formato
  "yyyy-mm-dd HH:mm:ss"  y  otras veces aparece en formato "yyyy-mm-dd H:mm:ss".  Se intenta corregir de varias maneras,
  NO TENIENDO ÉXITO ninguno de los intentos desarrollados. Al ejecutar la consulta 2 con el archivo "f1_dataset_test.csv"
  el resultado que se observa es  F1reader con 139 apariciones, cuando lo correcto sería que ese mismo usuario tuviera
  974 apariciones.
  
  
  -------------------------*********  14-Jun-2023 *********------------------------------
  
  *- Se corrige parte de la función número 5 para que el número de favoritos para cada usuario sea el mayor que existe
  y no la suma de todos ellos (ya es un número acumulativo en el archivo.csv).
  
  
  
  
  
  
  
  
  

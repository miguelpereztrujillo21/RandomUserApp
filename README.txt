*Duplicidad de usuarios:
   Ya que la API parece que no tiene ninguna clave  única, he decidido usar una conbinación
   de username + email para añadir una clave única a los usuarios y que no exista duplicidad.
*Filtro por email:
   En cuanto al filtro por email o nombre, en la API no he encontrado ningún parámetro para 
   realizar el filtrado por llamada API, he realizado un filtrado de usuarios en el
   UserPagingSource. Como solicitáis una llamada infinita, no obtenemos ningún mensaje de no
   encontrado por parte de la API.
*Mejoras:
   -Cuadro de búsqueda para el email .
   -Filtro de género con chips.
  
   

Feature: Iniciar sesion en sucursal
    Como Cliente sdfsdfsdf
    Requiero servicios de autenticación con JWT 
    Para iniciar sesión sesión desde el UI

    Scenario: al iniciar con la tarea de sesion
    Given datos del usuario "vargas" con contraseña "vargas" 
    When Envio estos datos con POST al servicio de carpinteart
    Then El servidor responde con el estado 200 en ambas llamadas
        And Con el token debe iniciar a la sucursal "SCZ"
        And La respuesta contiene el username "vargas"
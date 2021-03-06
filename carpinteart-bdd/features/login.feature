Feature: Iniciar sesion en sucursal
    Como Cliente
    Requiero servicios de autenticación con JWT 
    Para iniciar sesión sesión desde el UI

    Scenario: Iniciar sesion con usuario de PuntoCom
    Given datos del usuario "vargas" con contraseña "vargas" 
    When Envio estos datos con POST al servicio de autenticacion de carpinteart
    Then El servidor responde con el estado 200 
        And Con el token debe iniciar a la sucursal 1 en otro POST de autenticacion
        And La respuesta contiene el username "vargas"
        And debe tener 1 permisos

    Scenario Outline: Iniciar sesion con usuario de PuntoCom o de Sistema
    Given datos del usuario <username> con contraseña <password> 
    When Envio estos datos con POST al servicio de autenticacion de carpinteart
    Then El servidor responde con el estado <spected_status_code> 
        And Con el token debe iniciar a la sucursal <branch_office_id> en otro POST de autenticacion
        And La respuesta contiene el username <username>
        And debe tener <permissions> permisos

    Examples: 
    |  username  |   password    | branch_office_id | spected_status_code |   permissions   | 
    |  "vargas"  |  "vargas"     |       1          |       200           |        1        |
    |  "vargas"  |  "123"        |       1          |       401           |        0        |
    |  "admin"   |  "toro"       |       1          |       200           |        208      | 
    |  "vargas"  |  "vargas"     |       10000      |       200           |        0        | 
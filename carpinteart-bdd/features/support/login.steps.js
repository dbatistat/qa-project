const { Given, When, Then } = require('cucumber')
const { expect } = require('chai')
const request = require('request-promise')

let username;
let password;
let statusCode;
let firstToken;

let authResponse;

Given('datos del usuario {string} con contraseña {string}', function (usernameParam, passwordParam) {
  username = usernameParam;
  password = passwordParam;
});

When('Envio estos datos con POST al servicio de autenticacion de carpinteart', async function () {
  let options = {
    method: 'POST',
    uri: 'http://localhost:9090/api/auth',
    json: true,
    headers: {
      'username': username,
      'password': password
    },
    resolveWithFullResponse: true
  };

  await request(options)
    .then(function (response) {
      authResponse = response;
    })
    .catch(function (error) {
      authResponse = error;
    });
});

Then('El servidor responde con el estado {int}', function (code) {
  expect(authResponse.statusCode).to.eql(code);
  statusCode = code;


  if (authResponse.statusCode == 200) {
    firstToken = authResponse.body.token;
    if (authResponse.body.branchOffices.length == 0) {
      console.log(username + " no cuenta con sucursales disponibles");
      expect(authResponse.body.branchOffices.length).to.eql(0);  
    }
  } 
});

Then('Con el token debe iniciar a la sucursal {int} en otro POST de autenticacion', async function (branchOfficeId) {
  if (statusCode != 200) {
    return "skip";
  }
  let options = {
    method: 'POST',
    uri: 'http://localhost:9090/api/auth/branchOffice',
    json: true,
    headers: {
      'branch_office': branchOfficeId,
      'Authorization': "bearer " + firstToken
    },
    resolveWithFullResponse: true
  };

  await request(options)
    .then(function (response) {
      authResponse = response;
    })
    .catch(function (error) {
      authResponse = error;
    });
});

Then('La respuesta contiene el username {string}', function (username) {
  if (statusCode != 200) {
    return "skip";
  }
  expect(authResponse.statusCode).to.eql(statusCode);
  expect(authResponse.body.username).to.eql(username);
});

Then('debe tener {int} permisos', function (int) {
  if (statusCode != 200) {
    return "skip";
  }
  expect(authResponse.body.permissions.length).to.most(int);
});
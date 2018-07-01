package com.srlpunto.resources.auth.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Strings;
import com.srlpunto.adapters.puntocom.login.LoginAdapter;
import com.srlpunto.adapters.puntocom.login.LoginData;
import com.srlpunto.adapters.puntocom.login.LoginUserData;
import com.srlpunto.api.User;
import com.srlpunto.api.custom.ResponseFirstLoginCustom;
import com.srlpunto.api.enums.StatusBasic;
import com.srlpunto.resources.User.dao.UserDao;
import com.srlpunto.resources.User.dao.UserDaoImpl;
import com.srlpunto.resources.User.service.UserService;
import com.srlpunto.resources.User.service.UserServiceImpl;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * AuthService Implementation Test.
 *
 * @author David
 * @since 01/07/2018
 */
public class AuthServiceImplTest {

  private UserDao userDao;
  private UserService userService;
  private LoginAdapter loginAdapter;

  private AuthService authService;

  @Before
  public void setUp() {
    this.userDao = mock(UserDaoImpl.class);
    this.userService = mock(UserServiceImpl.class);
    this.loginAdapter = mock(LoginAdapter.class);

    this.authService = new AuthServiceImpl(this.userDao, this.userService, this.loginAdapter);
  }

  @Test
  public void testLoginWithPuntoComUserAndActive() throws Exception {
    final String usernameForTest = "vargas";
    final String passwordForTest = "vargas";

    LoginUserData loginUserData = new LoginUserData();
    loginUserData.nombre_completo = usernameForTest;
    LoginData loginData = new LoginData();
    loginData.usuario = new ArrayList<>(0);
    loginData.usuario.add(loginUserData);
    Optional<LoginData> optionalLoginData = Optional.of(loginData);
    when(loginAdapter.getLoginData(usernameForTest, passwordForTest)).thenReturn(optionalLoginData);

    User user = new User();
    user.id = 1;
    user.username = usernameForTest;
    user.status = StatusBasic.ACTIVED;
    Optional<User> optionalUserResponse = Optional.of(user);
    when(userService.saveOrUpdateUserPuntocom(optionalLoginData.get(), usernameForTest,
        passwordForTest)).thenReturn(optionalUserResponse);

    Optional<ResponseFirstLoginCustom> loginResponse = this.authService.login(usernameForTest, passwordForTest);

    Assert.assertEquals(true, loginResponse.isPresent());
    loginResponse.ifPresent(responseFirstLoginCustom ->
        Assert.assertFalse(Strings.isNullOrEmpty(responseFirstLoginCustom.token))
    );
  }

  @Test
  public void testLoginWithPuntoComUserAndInactive() throws Exception {
    final String usernameForTest = "vargas";
    final String passwordForTest = "123456";

    LoginUserData loginUserData = new LoginUserData();
    loginUserData.nombre_completo = usernameForTest;
    LoginData loginData = new LoginData();
    loginData.usuario = new ArrayList<>(0);
    loginData.usuario.add(loginUserData);
    Optional<LoginData> optionalLoginData = Optional.of(loginData);
    when(loginAdapter.getLoginData(usernameForTest, passwordForTest)).thenReturn(optionalLoginData);

    User user = new User();
    user.id = 1;
    user.username = usernameForTest;
    user.status = StatusBasic.INACTIVED;
    Optional<User> optionalUserResponse = Optional.of(user);
    when(userService.saveOrUpdateUserPuntocom(optionalLoginData.get(), usernameForTest,
        passwordForTest)).thenReturn(optionalUserResponse);

    Optional<ResponseFirstLoginCustom> loginResponse = this.authService.login(usernameForTest, passwordForTest);

    Assert.assertEquals(false, loginResponse.isPresent());
  }

  @Test
  public void testLoginWithSystemUserAndActive() throws Exception {
    final String usernameForTest = "admin";
    final String passwordForTest = "toro";

    Optional<LoginData> optionalLoginData = Optional.empty();
    when(loginAdapter.getLoginData(usernameForTest, passwordForTest)).thenReturn(optionalLoginData);

    User user = new User();
    user.id = 1;
    user.username = usernameForTest;
    user.status = StatusBasic.ACTIVED;
    Optional<User> optionalUserResponse = Optional.of(user);
    when(userDao.getByUsernamePassword(usernameForTest, passwordForTest)).thenReturn(optionalUserResponse);

    Optional<ResponseFirstLoginCustom> loginResponse = this.authService.login(usernameForTest, passwordForTest);

    Assert.assertEquals(true, loginResponse.isPresent());
    loginResponse.ifPresent(responseFirstLoginCustom ->
        Assert.assertFalse(Strings.isNullOrEmpty(responseFirstLoginCustom.token))
    );
  }

  @Test
  public void testLoginWithSystemUserAndInactive() throws Exception {
    final String usernameForTest = "admin";
    final String passwordForTest = "toro";

    Optional<LoginData> optionalLoginData = Optional.empty();
    when(loginAdapter.getLoginData(usernameForTest, passwordForTest)).thenReturn(optionalLoginData);

    User user = new User();
    user.id = 1;
    user.username = usernameForTest;
    user.status = StatusBasic.INACTIVED;
    Optional<User> optionalUserResponse = Optional.of(user);
    when(userDao.getByUsernamePassword(usernameForTest, passwordForTest)).thenReturn(optionalUserResponse);

    Optional<ResponseFirstLoginCustom> loginResponse = this.authService.login(usernameForTest, passwordForTest);

    Assert.assertEquals(false, loginResponse.isPresent());
  }

  @Test
  public void testLoginDoesMotExistUserInBoth() throws Exception {
    final String usernameForTest = "prueba";
    final String passwordForTest = "test";

    Optional<LoginData> optionalLoginData = Optional.empty();
    when(loginAdapter.getLoginData(usernameForTest, passwordForTest)).thenReturn(optionalLoginData);

    Optional<User> optionalUserResponse = Optional.empty();
    when(userDao.getByUsernamePassword(usernameForTest, passwordForTest)).thenReturn(optionalUserResponse);

    Optional<ResponseFirstLoginCustom> loginResponse = this.authService.login(usernameForTest, passwordForTest);

    Assert.assertEquals(false, loginResponse.isPresent());
  }
}
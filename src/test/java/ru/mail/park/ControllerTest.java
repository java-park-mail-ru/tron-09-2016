package ru.mail.park;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import ru.mail.park.dao.impl.UserDAOImpl;

/**
 * Created by zac on 26.10.16.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public class ControllerTest {
  public MockHttpSession mockHttpSession;

  @Autowired
  private UserDAOImpl userDAO;

  @Autowired
  private MockMvc mockMvc;

  private Long userID;

  @Before
  public void setup() throws Exception {
    mockHttpSession = new MockHttpSession();
    // registration user
    mockMvc.perform(post("/api/user")
        .content("{\"login\":\"testLogin\",\"password\":\"testPass\",\"email\": \"test@mail.ru\"}")
        .contentType(MediaType.APPLICATION_JSON)
        .session(mockHttpSession))
        .andExpect(status().isOk());
    userID = userDAO.getIdByLogin("testLogin", "testPass");
  }

  @Test
  public void testDuplicateRegister() throws Exception {
    mockMvc.perform(post("/api/user")
        .content("{\"login\":\"testLogin\",\"password\":\"passTest\",\"email\": \"test2@mail.ru\"}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testFailLogin() throws Exception {
    mockMvc.perform(post("/api/session")
        .content("{\"login\":\"testLogin\",\"password\":\"noTestPass\"}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testLogin() throws Exception {
    mockMvc.perform(post("/api/session")
        .content("{\"login\":\"testLogin\",\"password\":\"testPass\"}")
        .contentType(MediaType.APPLICATION_JSON)
        .session(mockHttpSession))
        .andExpect(status().isOk());
  }

  @Test
  public void testAuth() throws Exception {
    testLogin();
    mockMvc.perform(get("/api/session")
        .session(mockHttpSession))
        .andExpect(status().isOk());
  }

  @Test
  public void testFailAuth() throws Exception {
    mockMvc.perform(get("/api/session")
            .session(mockHttpSession))
            .andExpect(status().isUnauthorized());
  }

  @Test
  public void testLogout() throws Exception {
    mockMvc.perform(delete("/api/session")
            .session(mockHttpSession))
            .andExpect(status().isOk());
  }

  @Test
  public void testLoginThenLogout() throws Exception {
    testLogin();
    mockMvc.perform(delete("/api/session")
            .session(mockHttpSession))
            .andExpect(status().isOk());

    testFailAuth();
  }

  @Test
  public void testCheckInfo() throws Exception {
    testLogin();
    mockMvc.perform(get("/api/user/" + userID)
        .content("{\"login\":\"testLogin\",\"password\":\"testPass\"}")
        .contentType(MediaType.APPLICATION_JSON)
        .session(mockHttpSession))
        .andExpect(status().isOk());
  }

  @Test
  public void testFailCheckInfo() throws Exception {
    mockMvc.perform(get("/api/user/" + userID)
        .content("{\"login\":\"testLogin\",\"password\":\"testPass\"}")
        .contentType(MediaType.APPLICATION_JSON)
        .session(mockHttpSession))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testFailChangeInfo() throws Exception {
    mockMvc.perform(put("/api/user/" + userID)
            .content(
                    "{\"login\":\"testLogin_1\",\"password\":\"testPass_1\",\"email\":\"test@mail.ru_1\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .session(mockHttpSession))
            .andExpect(status().isForbidden());
  }

  @Test
  public void testChangeInfo() throws Exception {
    testLogin();
    mockMvc.perform(put("/api/user/" + userID)
            .content(
                    "{\"login\":\"testLogin_1\",\"password\":\"testPass_1\",\"email\":\"test@mail.ru_1\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .session(mockHttpSession))
            .andExpect(status().isOk());

    testLogout();
    // fail login under old login and password
    mockMvc.perform(post("/api/session")
            .content("{\"login\":\"testLogin\",\"password\":\"testPass\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .session(mockHttpSession))
            .andExpect(status().isBadRequest());
    // login under new login and password
    mockMvc.perform(post("/api/session")
            .content("{\"login\":\"testLogin_1\",\"password\":\"testPass_1\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .session(mockHttpSession))
            .andExpect(status().isOk());
  }

  @Test
  public void testFailDeleteUser() throws Exception {
    mockMvc.perform(delete("/api/user/" + userID)
            .session(mockHttpSession))
            .andExpect(status().isForbidden());
  }

  @Test
  public void testDeleteUser() throws Exception {
    testLogin();
    mockMvc.perform(delete("/api/user/" + userID)
            .session(mockHttpSession))
            .andExpect(status().isOk());

    // fail login under delete user
    mockMvc.perform(post("/api/session")
            .content("{\"login\":\"testLogin\",\"password\":\"testPass\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .session(mockHttpSession))
            .andExpect(status().isBadRequest());
  }
  
}

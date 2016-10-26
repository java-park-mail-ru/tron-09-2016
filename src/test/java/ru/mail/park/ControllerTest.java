package ru.mail.park;

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
import ru.mail.park.data.UserDataSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by zac on 26.10.16.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@Transactional
public class ControllerTest {
    public MockHttpSession mockHttpSession = new MockHttpSession();

    @Autowired
    private UserDAOImpl userDAO;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test() throws Exception {
        // registration user
        mockMvc.perform(post("/api/user")
                .content("{\"login\":\"testLogin\",\"password\":\"testPass\",\"email\": \"test@mail.ru\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // registration existing user
        mockMvc.perform(post("/api/user")
                .content("{\"login\":\"testLogin\",\"password\":\"passTest\",\"email\": \"test2@mail.ru\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        // fail login
        mockMvc.perform(post("/api/session")
                .content("{\"login\":\"testLogin\",\"password\":\"noTestPass\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .session(mockHttpSession))
                .andExpect(status().isBadRequest());
        // login
        mockMvc.perform(post("/api/session")
                .content("{\"login\":\"testLogin\",\"password\":\"testPass\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .session(mockHttpSession))
                .andExpect(status().isOk());
        // testAuth
        mockMvc.perform(get("/api/session")
                .session(mockHttpSession))
                .andExpect(status().isOk());
        // fail delete
        mockMvc.perform(delete("/api/session"))
                .andExpect(status().isOk());
        // testAuth
        mockMvc.perform(get("/api/session")
                .session(mockHttpSession))
                .andExpect(status().isOk());
        // delete
        mockMvc.perform(delete("/api/session")
                .session(mockHttpSession))
                .andExpect(status().isOk());
        // fail testAuth
        mockMvc.perform(get("/api/session")
                .session(mockHttpSession))
                .andExpect(status().isUnauthorized());
        // login
        mockMvc.perform(post("/api/session")
                .content("{\"login\":\"testLogin\",\"password\":\"testPass\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .session(mockHttpSession))
                .andExpect(status().isOk());

        final UserDataSet user = new UserDataSet(null, "testLogin", "testPass", "");
        final Long userID = userDAO.getIdByLogin(user);

        // check info
        mockMvc.perform(get("/api/user/" + userID)
                .content("{\"login\":\"testLogin\",\"password\":\"testPass\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .session(mockHttpSession))
                .andExpect(status().isOk());
        // fail check info
        mockMvc.perform(get("/api/user/" + userID)
                .content("{\"login\":\"testLogin\",\"password\":\"testPass\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        // fail change info
        mockMvc.perform(put("/api/user/" + userID)
                .content("{\"login\":\"testLogin1\",\"password\":\"testPass1\",\"email\":\"test@mail.ru1\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        // change info
        mockMvc.perform(put("/api/user/" + userID)
                .content("{\"login\":\"testLogin1\",\"password\":\"testPass1\",\"email\":\"test@mail.ru1\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .session(mockHttpSession))
                .andExpect(status().isOk());
        // fail delete user
        mockMvc.perform(delete("/api/user/" + userID))
                .andExpect(status().isForbidden());
        // delete user
        mockMvc.perform(delete("/api/user/" + userID)
                .session(mockHttpSession))
                .andExpect(status().isOk());
    }



}

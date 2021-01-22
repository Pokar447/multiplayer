package multiplayer.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    ACHTUNG:
    Zum Testen muss folgende Annotation in der ServerApplication.java auskommentiert werden: @EventListener(ApplicationReadyEvent.class).
    Wenn der Listener beim Starten der Tests läuft, werden die Tests nicht ausgeführt, da der Server auf die Clientverbindungen wartet.
*/

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    String jwt;

    //Funktioniert nur beim ersten Aufruf, danach muss der User vor erneutem Aufruf gelöscht werden
    @Test
    public void testSignUp() throws Exception {

        MvcResult result = this.mockMvc.perform(post("/users/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"username\":\"michael\",\"email\":\"michael@michael.de\",\"password\":\"Michael99\"}"))
                    .andExpect(status().isOk())
                    .andReturn();

        String content = result.getResponse().getContentAsString();
    }

    @Test
    public void testUniqueSignUp() throws Exception {

        MvcResult result = this.mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"michael\",\"email\":\"michael@michael.de\",\"password\":\"Michael99\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
    }

    @Test
    public void testLogin () throws Exception {
        MvcResult result = this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"michael\",\"password\":\"Michael99\"}"))
                .andExpect(status().isOk())
                .andReturn();

        jwt = result.getResponse().getHeader("Authorization");
    }

    @Test
    public void testGetUserNamesWithoutJwt () throws Exception {

        testLogin();

        MvcResult result = this.mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        String content = result.getResponse().getContentAsString();
    }

    @Test
    public void testGetUserNames () throws Exception {

        testLogin();

        MvcResult result = this.mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", jwt))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
    }


}
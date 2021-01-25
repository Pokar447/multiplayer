package multiplayer.server.controller;

import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    String jwt;

    //Funktioniert nur nachdem der Sign-Up Test im UserControllerTest gelaufen ist!
    @Test
    public void testLogin () throws Exception {
        MvcResult result = this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"michael\",\"password\":\"Michael99\"}"))
                .andExpect(status().isOk())
                .andReturn();

        jwt = result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
    }

    @Test
    public void testAddHistory () throws Exception {

        testLogin();

        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(DIGITS)
                .build();

        String randomString = generator.generate(32);

        //verstecke userId an Stelle 28
        String secret = randomString.substring(0,28) + "44" + randomString.substring(29);

        MvcResult result = this.mockMvc.perform(post("/history")
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .header("secret", secret)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"44\",\"opponentId\":\"45\",\"userHp\":\"0\",\"opponentHp\":\"5\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
    }

    @Test
    public void testAddHistoryWithoutJwt () throws Exception {

        MvcResult result = this.mockMvc.perform(post("/history")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"1\",\"opponentId\":\"2\",\"userHp\":\"0\",\"opponentHp\":\"5\"}"))
                .andExpect(status().isForbidden())
                .andReturn();

        String content = result.getResponse().getContentAsString();
    }

    @Test
    public void testAddHistoryWithInvalidData () throws Exception {

        testLogin();

        MvcResult result = this.mockMvc.perform(post("/history")
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"1000\",\"opponentId\":\"2000\",\"userHp\":\"0\",\"opponentHp\":\"5\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
    }

    @Test
    public void testGetByUserId () throws Exception {

        testLogin();

        MvcResult result = this.mockMvc.perform(get("/history")
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .param("userid", "1"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

    }

    @Test
    public void testGetByUserIdWithoutJwt () throws Exception {

        MvcResult result = this.mockMvc.perform(get("/history")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userid", "1"))
                .andExpect(status().isForbidden())
                .andReturn();

        String content = result.getResponse().getContentAsString();

    }

    @Test
    public void testGetByUserIdWithInvalidData () throws Exception {

        testLogin();

        MvcResult result = this.mockMvc.perform(get("/history")
                .header(HttpHeaders.AUTHORIZATION, jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .param("userid", "1000"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();

    }

}
package com.gestao.zk.infrastructure.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigurationTest {
    @Autowired
    private MockMvc mvn;

    @Test
    void deveAutenticarComSucessoUsandoCredenciaisValidas() throws Exception {
        mvn.perform(formLogin("/login")
                        .user("admin")
                        .password("admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void deveRedirecionarParaLoginComErroEmCredenciaisInvalidas() throws Exception {
        mvn.perform(formLogin("/login")
                        .user("usuarioInexistente")
                        .password("senhaErrada"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login.html?error"));
    }

    @Test
    void deveFazerLogoutComSucesso() throws Exception {
        mvn.perform(post("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login.html?logout"));
    }

    @Test
    void deveRedirecionarParaLoginAoAcessarRaizSemAutenticacao() throws Exception {
        mvn.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login.html"));
    }

    @Test
    void devePermitirAcessoPublicoAoLoginHtml() throws Exception {
        mvn.perform(get("/login.html"))
                .andExpect(status().isOk());
    }
}

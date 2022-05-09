package com.molkky.molkky.controllers;


import com.molkky.molkky.model.UserConnectionModel;
import com.molkky.molkky.repository.UserRepository;
import com.molkky.molkky.repository.UserTournamentRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ConnexionController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ExtendWith(MockitoExtension.class)
public class ConnexionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConnexionController connexionController;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserTournamentRoleRepository userTournamentRoleRepository;

    @Mock
    private UserConnectionModel userConnectionModel;

    @Test
    public void testConnexionController() throws Exception {
        mockMvc.perform(get("/connexion/"))
                .andExpect(status().isOk());

        when(this.userRepository.existsUserByEmailAndPassword(any(), any())).thenReturn(true);

        mockMvc.perform(post("/connexion/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"));
    }
}

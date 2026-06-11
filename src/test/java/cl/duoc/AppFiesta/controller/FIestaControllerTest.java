package cl.duoc.AppFiesta.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.json.JsonMapper;

import cl.duoc.AppFiesta.assembler.FiestaModelAssembler;
import cl.duoc.AppFiesta.dto.request.FiestaCreateRequest;
import cl.duoc.AppFiesta.dto.request.FiestaUpdateRequest;
import cl.duoc.AppFiesta.dto.response.FiestaResponse;
import cl.duoc.AppFiesta.service.FiestaService;
import cl.duoc.AppFiesta.security.JwtAuthenticationFilter;

@WebMvcTest(FIestaController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(FiestaModelAssembler.class)
@ActiveProfiles("test")
class FIestaControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private JsonMapper jsonMapper;

        @MockitoBean
        private FiestaService fiestaService;

        // Evita que SecurityConfig falle al crear su dependencia.
        @MockitoBean
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Test
        void obtenerUnaFiestaDebeRetornar200() throws Exception {
                when(fiestaService.obtenerUnaFiesta(1))
                                .thenReturn(crearResponse(1, "Cumpleaños"));

                mockMvc.perform(get("/api/v1/fiestas/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.idFiesta").value(1))
                                .andExpect(jsonPath("$.nombre").value("Cumpleaños"))
                                .andExpect(jsonPath("$._links.self.href").exists());
        }

        @Test
        void obtenerUnaFiestaDebeRetornar404CuandoNoExiste() throws Exception {
                when(fiestaService.obtenerUnaFiesta(99)).thenReturn(null);

                mockMvc.perform(get("/api/v1/fiestas/99"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void listarTodasFiestasDebeRetornar200() throws Exception {
                when(fiestaService.obtenerTodasFiestas())
                                .thenReturn(List.of(crearResponse(1, "Fiesta Uno")));

                mockMvc.perform(get("/api/v1/fiestas"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$._embedded").exists())
                                .andExpect(jsonPath("$._links.self.href").exists());
        }

        @Test
        void guardarFiestaDebeRetornar201CuandoRequestEsValido()
                        throws Exception {

                FiestaCreateRequest request = new FiestaCreateRequest(
                                "Cumpleaños Daniel",
                                "Cumpleaños",
                                LocalDate.now().plusDays(20),
                                "Av. Principal 123",
                                1L,
                                100);

                when(fiestaService.guardarFiesta(any(FiestaCreateRequest.class)))
                                .thenReturn(crearResponse(10, "Cumpleaños Daniel"));

                mockMvc.perform(post("/api/v1/fiestas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.idFiesta").value(10))
                                .andExpect(jsonPath("$.nombre")
                                                .value("Cumpleaños Daniel"));

                verify(fiestaService).guardarFiesta(any(FiestaCreateRequest.class));
        }

        @Test
        void guardarFiestaDebeRetornar400CuandoNombreEstaVacio()
                        throws Exception {

                FiestaCreateRequest request = new FiestaCreateRequest(
                                "",
                                "Cumpleaños",
                                LocalDate.now().plusDays(20),
                                "Av. Principal 123",
                                1L,
                                100);

                mockMvc.perform(post("/api/v1/fiestas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(
                                                request)))
                                .andExpect(status().isBadRequest());

                verify(fiestaService, never())
                                .guardarFiesta(any(FiestaCreateRequest.class));
        }

        @Test
        void actualizarFiestaDebeRetornar200CuandoExiste() throws Exception {
                FiestaUpdateRequest request = new FiestaUpdateRequest(
                                "Fiesta actualizada",
                                "Aniversario",
                                LocalDate.now().plusDays(15),
                                "Dirección actualizada",
                                180);

                when(fiestaService.actualizarFiesta(
                                any(Integer.class), any(FiestaUpdateRequest.class)))
                                .thenReturn(crearResponse(1, "Fiesta actualizada"));

                mockMvc.perform(put("/api/v1/fiestas/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(
                                                request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nombre")
                                                .value("Fiesta actualizada"));
        }

        @Test
        void eliminarFiestaDebeRetornar204CuandoExiste() throws Exception {
                when(fiestaService.eliminarFiesta(1)).thenReturn(true);

                mockMvc.perform(delete("/api/v1/fiestas/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void eliminarFiestaDebeRetornar404CuandoNoExiste() throws Exception {
                when(fiestaService.eliminarFiesta(99)).thenReturn(false);

                mockMvc.perform(delete("/api/v1/fiestas/99"))
                                .andExpect(status().isNotFound());
        }

        private FiestaResponse crearResponse(Integer id, String nombre) {
                return new FiestaResponse(
                                id,
                                nombre,
                                "Tipo de prueba",
                                LocalDate.now().plusDays(30),
                                "Dirección de prueba",
                                "Santiago",
                                100);
        }
}

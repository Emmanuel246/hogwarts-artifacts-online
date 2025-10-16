package com.hogwart.hogwarts_artifacts_online.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hogwart.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import com.hogwart.hogwarts_artifacts_online.system.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;


    @MockBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;


    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setId("123456789");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles me");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a1);

        Artifact a2 = new Artifact();
        a1.setId("246801121416");
        a1.setName("Invisibility Cloak");
        a1.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a2);

        Artifact a3 = new Artifact();
        a1.setId("13567343234523432334334");
        a1.setName("Elder Wand");
        a1.setDescription("The Elder wand, known throughout history as the Deathstick or the Wander");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a3);

        Artifact a4 = new Artifact();
        a1.setId("0944394394394293233");
        a1.setName("The Marauder's Map");
        a1.setDescription("A magical map of Hogwarts created by Remus Lupin. Peter Pettigrew");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a4);

        Artifact a5 = new Artifact();
        a1.setId("004334823423434");
        a1.setName("The Sword of Gryffindor");
        a1.setDescription("A goblin-made sword adorned with large rubies on the pommel.");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a5);

        Artifact a6 = new Artifact();
        a1.setId("2343434234223433434");
        a1.setName("Resurrection Stone");
        a1.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a6);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        // Given
        given(this.artifactService.findById("2343434234223433434")).willReturn(this.artifacts.get(0));

        // When and then
        this.mockMvc.perform(get("/api/v1/artifacts/2343434234223433434").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("2343434234223433434"))
                .andExpect(jsonPath("$.data.name").value("Resurrection Stone"));


    }

    @Test
    void testFindArtifactByNotFound() throws Exception {
        // Given
        given(this.artifactService.findById("2343434234223433434")).willThrow(new ArtifactNotFoundException("2343434234223433434"));

        // When and then
        this.mockMvc.perform(get("/api/v1/artifacts/2343434234223433434").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Artifact with id: 2343434234223433434 :("))
                .andExpect(jsonPath("$.data").isEmpty());


    }

    @Test
    void testFindAllSuccess() throws Exception {
        // Given
        given(this.artifactService.findAll()).willReturn(this.artifacts);

        // When and then
        this.mockMvc.perform(get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data").value(Matchers.hasSize(this.artifacts
                        .size())))
                .andExpect(jsonPath("$.data[0].id").value("2343434234223433434"))
                .andExpect(jsonPath("$.data[0].name").value("Resurrection Stone"));


    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        // Given
        ArtifactDto artifactDto = new ArtifactDto(null, "Remebrall", "A remebral was a magical large marble-like stone", "ImageUrl", null);

        String json = this.objectMapper.writeValueAsString(artifactDto);

        Artifact savedArtifact = new Artifact();
        savedArtifact.setId("1234567890");
        savedArtifact.setName("Remembrall");
        savedArtifact.setDescription("A remebral was a magical large marble-like stone");
        savedArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);


        //When and then
        this.mockMvc.perform(post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON
                ).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl()));

    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
        // Given
        ArtifactDto artifactDto = new ArtifactDto("123456789",
                "Invisibility Cloak",
                "A new description",
                "ImageUrl", null);

        String json = this.objectMapper.writeValueAsString(artifactDto);

        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("123456789");
        updatedArtifact.setName("Invisibility Cloak");
        updatedArtifact.setDescription("A new description");
        updatedArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.update(eq("123456789"), Mockito.any(Artifact.class))).willReturn(updatedArtifact);


        //When and then
        this.mockMvc.perform(put("/api/v1/artifacts/123456789").contentType(MediaType.APPLICATION_JSON
                ).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("123456789"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedArtifact.getImageUrl()));

    }

    @Test
    void testUpdateArtifactErrorWithNonExistentId() throws Exception {
        // Given
        ArtifactDto artifactDto = new ArtifactDto("123456789",
                "Invisibility Cloak",
                "A new description",
                "ImageUrl", null);

        String json = this.objectMapper.writeValueAsString(artifactDto);

        given(this.artifactService.update(eq("123456789"), Mockito.any(Artifact.class))).willThrow(new ArtifactNotFoundException("123456789"));


        //When and then
        this.mockMvc.perform(put("/api/v1/artifacts/123456789").contentType(MediaType.APPLICATION_JSON
                ).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Artifact with id: 123456789 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @Test
    void testDeleteArtifactSuccess() throws Exception {
        // Given
        doNothing().when(this.artifactService).delete("123456789");


        //When and then
        this.mockMvc.perform(delete("/api/v1/artifacts/1250808601744904196").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactErrorWithNonExistenceId() throws Exception {
        // Given
       doThrow(new ArtifactNotFoundException("123456789")).when(this.artifactService).delete("123456789");


        //When and then
        this.mockMvc.perform(delete("/api/v1/artifacts/123456789").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Artifact with id: 123456789 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }


}
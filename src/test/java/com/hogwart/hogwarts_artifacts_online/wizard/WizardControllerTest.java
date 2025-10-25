package com.hogwart.hogwarts_artifacts_online.wizard;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hogwart.hogwarts_artifacts_online.artifact.Artifact;
import com.hogwart.hogwarts_artifacts_online.system.StatusCode;
import com.hogwart.hogwarts_artifacts_online.system.exception.ObjectNotFoundException;
import com.hogwart.hogwarts_artifacts_online.wizard.dto.WizardDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class WizardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WizardService wizardService;

    @Autowired
    ObjectMapper objectMapper;

    List<Wizard> wizards;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("123456789");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles me");
        a1.setImageUrl("ImageUrl");


        Artifact a2 = new Artifact();
        a1.setId("246801121416");
        a1.setName("Invisibility Cloak");
        a1.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a1.setImageUrl("ImageUrl");


        Artifact a3 = new Artifact();
        a1.setId("13567343234523432334334");
        a1.setName("Elder Wand");
        a1.setDescription("The Elder wand, known throughout history as the Deathstick or the Wander");
        a1.setImageUrl("ImageUrl");


        Artifact a4 = new Artifact();
        a1.setId("0944394394394293233");
        a1.setName("The Marauder's Map");
        a1.setDescription("A magical map of Hogwarts created by Remus Lupin. Peter Pettigrew");
        a1.setImageUrl("ImageUrl");


        Artifact a5 = new Artifact();
        a1.setId("004334823423434");
        a1.setName("The Sword of Gryffindor");
        a1.setDescription("A goblin-made sword adorned with large rubies on the pommel.");
        a1.setImageUrl("ImageUrl");


        Artifact a6 = new Artifact();
        a1.setId("2343434234223433434");
        a1.setName("Resurrection Stone");
        a1.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones");
        a1.setImageUrl("ImageUrl");

        this.wizards = new ArrayList<>();

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Emmanuel");
        w1.addArtifact(a1);
        w1.addArtifact(a2);
        this.wizards.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Makanjuola");
        w2.addArtifact(a3);
        w2.addArtifact(a4);
        this.wizards.add(w2);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() throws Exception {
        // Given
        given(this.wizardService.findAll()).willReturn(this.wizards);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data").value(Matchers.hasSize(this.wizards
                        .size())))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[0].name").value("Emmanuel"));


    }

    @Test
    void testFindWizardByIdSuccess() throws Exception {
        // Given
        given(this.wizardService.findById(1)).willReturn(this.wizards.get(0));

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value("Emmanuel"));


    }

    @Test
    void testFindWizardByIdNotFound() throws Exception {
        // Given
        given(this.wizardService.findById(1878)).willThrow(new ObjectNotFoundException("Wizard", 1878));

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/wizards/1878").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Wizard with id: 1878 :("))
                .andExpect(jsonPath("$.data").isEmpty());


    }

    @Test
    void testAddWizardSuccess() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(null, "Makanjuola Emmanuel", null);

        String json = this.objectMapper.writeValueAsString(wizardDto);

        Wizard savedWizard = new Wizard();
        savedWizard.setId(1);
        savedWizard.setName("Makanjuola Emmanuel");


        given(this.wizardService.save(Mockito.any(Wizard.class))).willReturn(savedWizard);


        //When and then
        this.mockMvc.perform(post(this.baseUrl +  "/wizards").contentType(MediaType.APPLICATION_JSON
                ).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedWizard.getName()))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(savedWizard.getNumberOfArtifacts()));


    }


    @Test
    void testUpdateWizardSuccess() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(4,
                "Makanjuola Emmanuel", 4);

        String json = this.objectMapper.writeValueAsString(wizardDto);

        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(4);
        updatedWizard.setName("Invisibility Cloak");


        given(this.wizardService.update(eq(4), Mockito.any(Wizard.class))).willReturn(updatedWizard);


        //When and then
        this.mockMvc.perform(put(this.baseUrl + "/wizards/4").contentType(MediaType.APPLICATION_JSON
                ).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("4"))
                .andExpect(jsonPath("$.data.name").value(updatedWizard.getName()))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(updatedWizard.getNumberOfArtifacts()));

    }

    @Test
    void testUpdateWizardErrorWithNonExistentId() throws Exception {
        // Given
        WizardDto wizardDto = new WizardDto(5,
                "Invisibility Cloak",
                3
                 );

        String json = this.objectMapper.writeValueAsString(wizardDto);

        given(this.wizardService.update(eq(5), Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("Wizard", 5));


        //When and then
        this.mockMvc.perform(put(this.baseUrl + "/wizards/5").contentType(MediaType.APPLICATION_JSON
                ).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Wizard with id: 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @Test
    void testDeleteWizardSuccess() throws Exception {
        // Given
        doNothing().when(this.wizardService).delete(4);


        //When and then
        this.mockMvc.perform(delete(this.baseUrl + "/wizards/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteWizardErrorWithNonExistenceId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("Wizard", 4)).when(this.wizardService).delete(4);


        //When and then
        this.mockMvc.perform(delete(this.baseUrl + "/wizards/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Wizard with id: 4 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }




}

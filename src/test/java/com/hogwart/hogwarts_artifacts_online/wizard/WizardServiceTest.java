package com.hogwart.hogwarts_artifacts_online.wizard;

import com.hogwart.hogwarts_artifacts_online.artifact.Artifact;
import com.hogwart.hogwarts_artifacts_online.artifact.ArtifactRepository;
import com.hogwart.hogwarts_artifacts_online.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;


    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Emmanuel");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Makanjuola");




        this.wizards = new ArrayList<>();
        this.wizards.add(w1);
        this.wizards.add(w2);



    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void testFindAllSuccess() {
        // Given
        given(this.wizardRepository.findAll()).willReturn(this.wizards);

        // When
        List<Wizard> actualWizards = wizardService.findAll();


        // Then
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());
        verify(this.wizardRepository, times(1)).findAll();
    }


    @Test
    void testFindByIdSuccess() {
        // Given. Arrange inputs and targets. Define the behaviour of Mock object Wizard Repository
        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Harry Potter");



        given(this.wizardRepository.findById(1)).willReturn(Optional.of(w)); // Defines the behaviour of mock object.
        // When. Act on the target behavior. When steps should cover the method to be tested

        Wizard returnedWizard = wizardService.findById(1);

        // Then. Assert expected outcomes.
        assertThat(returnedWizard.getId()).isEqualTo(w.getId());
        assertThat(returnedWizard.getName()).isEqualTo(w.getName());
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testSaveSuccess() {
        // Given
        Wizard newWizard = new Wizard();
        newWizard.setName("Makanjuola Emmanuel");

        given(this.wizardRepository.save(newWizard)).willReturn(newWizard);
        // When
        Wizard savedWizard = wizardService.save(newWizard);

        // Then
        assertThat(savedWizard.getName()).isEqualTo(newWizard.getName());
        verify(this.wizardRepository, times(1)).save(newWizard);
    }

    @Test
    void testUpdateSuccess(){
        // Given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(4);
        oldWizard.setName("Elijah baba");

        Wizard update = new Wizard();
        update.setId(4);
        update.setName("Makanjuola");

        given(this.wizardRepository.findById(4)).willReturn(Optional.of(oldWizard));
        given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);
        // When
        Wizard updatedWizard = wizardService.update(4, update);

        // Then
        assertThat(updatedWizard.getId()).isEqualTo(update.getId());
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());
        verify(this.wizardRepository, times(1)).findById(4);
        verify(this.wizardRepository, times(1)).save(oldWizard);
    }


    @Test
    void testDeleteSuccess() {
        // Given
        Wizard wizard  = new Wizard();
        wizard.setId(4);
        wizard.setName("Invisibility Cloak");


        given(this.wizardRepository.findById(4)).willReturn(Optional.of(wizard));
        doNothing().when(this.wizardRepository).deleteById(4);
        // When
        wizardService.delete(4);

        //Then
        verify(this.wizardRepository, times(1)).deleteById(4);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(this.wizardRepository.findById(4)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            wizardService.delete(4);
        });

        //Then
        verify(this.wizardRepository, times(1)).findById(4);
    }


    @Test
    void testAssignArtifactSuccess() {
        // Given
        Artifact a = new Artifact();
        a.setId("123456789");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the weare invisible");
        a.setImageUrl("ImageUrl");


        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Naville Emma");


        given(this.artifactRepository.findById("123456789")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.of(w3));
        // When
    this.wizardService.assignArtifact(3, "123456789");

        // Then
        assertThat(a.getOwner().getId()).isEqualTo(3);
        assertThat(w3.getArtifacts()).contains(a);
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() {
        // Given
        Artifact a = new Artifact();
        a.setId("123456789");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the weare invisible");
        a.setImageUrl("ImageUrl");


        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Potter");
        w2.addArtifact(a);



        given(this.artifactRepository.findById("123456789")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById(3)).willReturn(Optional.empty());
        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(3, "123456789");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                        .hasMessage("Could not find wizard with id: 3 :(");
        assertThat(a.getOwner().getId()).isEqualTo(2);

    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() {
        // Given

        given(this.artifactRepository.findById("123456789")).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(3, "123456789");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with id: 123456789 :(");


    }

}

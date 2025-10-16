package com.hogwart.hogwarts_artifacts_online.artifact;

import com.hogwart.hogwarts_artifacts_online.artifact.utils.IdWorker;
import com.hogwart.hogwarts_artifacts_online.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {



    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1234567890");
        a1.setName("Invisibility Cloak");
        a1.setDescription("An invisibility cloak is used to make the wearer invisible");
        a1.setImageUrl("imageUrl");

        Artifact a2 = new Artifact();
        a2.setId("0987654321");
        a2.setName("The Flying Broom");
        a2.setDescription("A broom that flies and takes you to your dream imagined");
        a2.setImageUrl("imageUrl");

        this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);



    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        // Given. Arrange inputs and targets. Define the behaviour of Mock object Artifact Repository
        Artifact a = new Artifact();
        a.setId("123456789");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the weare invisible");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        a.setOwner(w);
        
        given(artifactRepository.findById("123456789")).willReturn(Optional.of(a)); // Defines the behaviour of mock object.
        // When. Act on the target behavior. When steps should cover the method to be tested

       Artifact returnedArtifact = artifactService.findById("123456789");

        
        // Then. Assert expected outcomes.
        assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
        verify(artifactRepository, times(1)).findById("123456789");
    }

    @Test
    void testFindByIdNotFound(){
        // Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        // When
       Throwable thrown = catchThrowable(()-> {
           Artifact returnedArtifact = artifactService.findById("123456789");
       });

        // Then
        assertThat(thrown)
                .isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("Could not find Artifact with id: 123456789 :(");
        verify(artifactRepository, times(1)).findById("123456789");

    }


    @Test
    void testFindAllSuccess() {
        // Given
        given(artifactRepository.findAll()).willReturn(this.artifacts);

        // When
        List<Artifact> actualArtifacts = artifactService.findAll();


        // Then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        // Given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription("Description....");
        newArtifact.setImageUrl("ImageUrl...");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);

        // When
        Artifact savedArtifact = artifactService.save(newArtifact);


        // Then
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository, times(1)).save(newArtifact);

    }

    @Test
    void testUpdateSuccess(){
        // Given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("123456789");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("An invisibility cloak is used to make the weare invisible");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact update = new Artifact();
        update.setId("123456789");
        update.setName("Invisibility Cloak");
        update.setDescription("A new description");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("123456789")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        // When
        Artifact updatedArtifact = artifactService.update("123456789", update);

        // then
        assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
        verify(artifactRepository, times(1)).findById("123456789");
        verify(artifactRepository, times(1)).save(oldArtifact);

    }

    @Test

    void testUpdateNotFound(){
        // Given
        Artifact update = new Artifact();
        update.setId("123456789");
        update.setName("Invisibility Cloak");
        update.setDescription("A new description");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("123456789")).willReturn(Optional.empty());

        // When
        assertThrows(ArtifactNotFoundException.class, ()-> {
            artifactService.update("123456789", update);
        });
        // Then
        verify(artifactRepository, times(1)).findById("123456789");
    }


    @Test
    void testDeleteSuccess() {
        // GGiven
        Artifact artifact  = new Artifact();
        artifact.setId("123456789");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("A new description");
        artifact.setImageUrl("ImageUrl");

        given(artifactRepository.findById("123456789")).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById("123456789");
        // When
        artifactService.delete("123456789");

        //Then
        verify(artifactRepository, times(1)).deleteById("123456789");
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(artifactRepository.findById("123456789")).willReturn(Optional.empty());

        // When
        assertThrows(ArtifactNotFoundException.class, () -> {
            artifactService.delete("123456789");
        });

        //Then
        verify(artifactRepository, times(1)).findById("123456789");
    }
}
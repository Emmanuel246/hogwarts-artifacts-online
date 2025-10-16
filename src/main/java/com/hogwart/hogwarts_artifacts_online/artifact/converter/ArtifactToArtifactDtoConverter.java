package com.hogwart.hogwarts_artifacts_online.artifact.converter;

import com.hogwart.hogwarts_artifacts_online.artifact.Artifact;
import com.hogwart.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import com.hogwart.hogwarts_artifacts_online.wizard.converter.WizardToWizardDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {



    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }


    @Override
    public ArtifactDto convert(Artifact source) {
        ArtifactDto artifactDto = new ArtifactDto(source.getId(), source.getName(),
                source.getDescription(), source.getImageUrl(), source.getOwner() != null
                ? this.wizardToWizardDtoConverter.convert(source.getOwner()): null);
        return artifactDto;
    }
}

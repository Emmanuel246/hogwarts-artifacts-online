package com.hogwart.hogwarts_artifacts_online.wizard.converter;

import com.hogwart.hogwarts_artifacts_online.wizard.Wizard;
import com.hogwart.hogwarts_artifacts_online.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardToWizardDtoConverter implements Converter<Wizard, WizardDto> {

    @Override
    public WizardDto convert(Wizard source) {

        WizardDto wizardDto = new WizardDto(source.getId(), source.getName(),source.getNumberOfArtifacts());

        return wizardDto;
    }
}

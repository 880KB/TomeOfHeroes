package dev.swim.toh.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.swim.toh.model.core.CharacterModel;
import dev.swim.toh.model.core.CharacterModelFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class CharacterFileService {

    private final CharacterModelFactory characterModelFactory;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public CharacterFileService(CharacterModelFactory characterModelFactory) {
        this.characterModelFactory = characterModelFactory;
    }

    public void save(CharacterModel characterModel, File file) throws IOException {
        objectMapper.writeValue(file, CharacterDataMapper.toData(characterModel));
    }

    public CharacterModel load(File file) throws IOException {
        CharacterData data = objectMapper.readValue(file, CharacterData.class);
        CharacterModel characterModel = characterModelFactory.createEmptyCharacter();
        CharacterDataMapper.applyTo(data, characterModel);
        return characterModel;
    }
}

package com.ansh.app.serializer;

import com.ansh.entity.animal.Animal;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import jakarta.annotation.Nonnull;
import java.io.IOException;

public class AnimalSerializer implements StreamSerializer<Animal> {

  @Override
  public void write(@Nonnull ObjectDataOutput out, Animal animal) throws IOException {
    out.writeLong(animal.getId());
    out.writeString(animal.getName());
    out.writeString(animal.getSpecies());
    out.writeString(animal.getPrimaryColor());
    out.writeString(animal.getImplantChipId());
    out.writeString(animal.getBreed());
    out.writeChar(animal.getGender());
    out.writeObject(animal.getBirthDate());
    out.writeString(animal.getPattern());
    out.writeObject(animal.getAdmissionDate());
    out.writeString(animal.getPhotoImgPath());
    out.writeLong(animal.getVersion());
  }

  @Override
  @Nonnull
  public Animal read(@Nonnull ObjectDataInput in) throws IOException {
    return Animal.builder()
        .id(in.readLong())
        .name(in.readString())
        .species(in.readString())
        .primaryColor(in.readString())
        .implantChipId(in.readString())
        .breed(in.readString())
        .gender(in.readChar())
        .birthDate(in.readObject())
        .pattern(in.readString())
        .admissionDate(in.readObject())
        .photoImgPath(in.readString())
        .version(in.readLong())
        .build();
  }

  @Override
  public int getTypeId() {
    return 1;
  }
}

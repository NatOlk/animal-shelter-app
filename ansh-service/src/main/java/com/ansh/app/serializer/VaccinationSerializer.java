package com.ansh.app.serializer;

import com.ansh.entity.animal.Vaccination;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import jakarta.annotation.Nonnull;
import java.io.IOException;

public class VaccinationSerializer implements StreamSerializer<Vaccination> {

  @Override
  public void write(@Nonnull ObjectDataOutput out, @Nonnull Vaccination vaccination)
      throws IOException {
    out.writeLong(vaccination.getId());
    out.writeString(vaccination.getVaccine());
    out.writeString(vaccination.getBatch());
    out.writeString(vaccination.getEmail());
    out.writeObject(vaccination.getVaccinationTime());
    out.writeString(vaccination.getComments());
    out.writeLong(vaccination.getVersion());
  }

  @Override
  @Nonnull
  public Vaccination read(@Nonnull ObjectDataInput in) throws IOException {
    return Vaccination.builder()
        .id(in.readLong())
        .vaccine(in.readString())
        .batch(in.readString())
        .email(in.readString())
        .vaccinationTime(in.readObject())
        .comments(in.readString())
        .version(in.readLong())
        .build();
  }

  @Override
  public int getTypeId() {
    return 2;
  }
}

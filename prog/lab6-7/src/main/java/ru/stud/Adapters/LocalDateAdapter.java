package ru.stud.Adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    @Override
    public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
        if(localDate==null){
            jsonWriter.nullValue();
        }
        else{
            jsonWriter.value(localDate.format(formatter));
        }
    }

    @Override
    public LocalDate read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek()==null){
            return null;
        }
        else{
            String localDate = jsonReader.nextString();
            try{
                return LocalDate.parse(localDate,formatter);
            }catch (DateTimeParseException e){
                throw new IOException("Неверный формат LocalDate: '" + localDate +
                        "'. Ожидается yyyy-MM-dd", e);
            }
        }
    }
}

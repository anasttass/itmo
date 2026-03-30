package ru.stud.Adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.stud.Commands.Add;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {
    private DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    @Override
    public void write(JsonWriter jsonWriter, ZonedDateTime zonedDateTime) throws IOException {
        if(zonedDateTime==null){
            jsonWriter.nullValue();
        }
        else{
            jsonWriter.value(zonedDateTime.format(formatter));
        }
    }

    @Override
    public ZonedDateTime read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek()==null){
            return null;
        }
        else{
            String localDate = jsonReader.nextString();
            try{
                return ZonedDateTime.parse(localDate,formatter);
            }catch (DateTimeParseException e){
                throw new IOException("Неверный формат LocalDate: '" + localDate +
                        "'. Ожидается yyyy-MM-dd hh:mm:ss", e);
            }
        }
    }
}

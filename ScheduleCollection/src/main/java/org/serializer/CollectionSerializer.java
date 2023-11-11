package org.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.model.Term;
import org.model.Time;

import java.io.IOException;
import java.util.Map;

public class CollectionSerializer extends StdSerializer<Term> {
    public CollectionSerializer() {
        this(null);
    }
    protected CollectionSerializer(Class<Term> t) {
        super(t);
    }


    @Override
    public void serialize(Term term, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeObjectField("ImeUcionice", term.getRoom().getName());
        jsonGenerator.writeObjectField("Kapacitet", term.getRoom().getCapacity());
        jsonGenerator.writeObjectField("Pocetak", term.getTime().getStartDate());
        jsonGenerator.writeObjectField("PocetakCasa", term.getTime().getStartTime());
        jsonGenerator.writeObjectField("KrajCasa", term.getTime().getEndTime());
        for(Map.Entry<String, String> entry : term.getAdditionalData().entrySet()) {
            jsonGenerator.writeObjectField(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<String, Integer> entry : term.getRoom().getEquipment().entrySet()) {
            jsonGenerator.writeObjectField(entry.getKey(), entry.getValue());
        }
        jsonGenerator.writeObjectField("DanUNedelji", Time.getWeekDay(term.getTime().getStartDate()));

        jsonGenerator.writeEndObject();
    }

}

package com.softvision.ipm.pms.common.adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class IndiaDateFormatSerializer extends JsonSerializer<Date> {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void serialize(Date date, JsonGenerator jgen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        jgen.writeString(FORMAT.format(date));
    }

}

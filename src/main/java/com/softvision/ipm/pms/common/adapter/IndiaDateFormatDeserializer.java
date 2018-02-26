package com.softvision.ipm.pms.common.adapter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class IndiaDateFormatDeserializer extends JsonDeserializer<Date> {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext arg1)
            throws IOException, JsonProcessingException {
    	try {
        ObjectCodec oc = jsonParser.getCodec();
        String time = oc.readValue(jsonParser, String.class);
			return FORMAT.parse(time);
		} catch (ParseException parseException) {
			throw new IOException(parseException.getMessage(), parseException);
		}
    }

}

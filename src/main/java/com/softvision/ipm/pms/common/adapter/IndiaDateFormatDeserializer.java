package com.softvision.ipm.pms.common.adapter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.softvision.ipm.pms.common.util.DateUtil;

public class IndiaDateFormatDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext arg1)
            throws IOException, JsonProcessingException {
    	try {
        ObjectCodec oc = jsonParser.getCodec();
        String time = oc.readValue(jsonParser, String.class);
			return DateUtil.parseIndianFormatDate(time);
		} catch (ParseException parseException) {
			throw new IOException(parseException.getMessage(), parseException);
		}
    }

}

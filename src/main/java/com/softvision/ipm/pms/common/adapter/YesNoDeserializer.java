package com.softvision.ipm.pms.common.adapter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.softvision.ipm.pms.common.constants.YesNo;

public class YesNoDeserializer extends JsonDeserializer<YesNo> {

    @Override
    public YesNo deserialize(JsonParser jsonParser, DeserializationContext arg1)
            throws IOException, JsonProcessingException {
    	ObjectCodec oc = jsonParser.getCodec();
    	Boolean value = oc.readValue(jsonParser, Boolean.class);
    	return YesNo.get(value);
    }

}

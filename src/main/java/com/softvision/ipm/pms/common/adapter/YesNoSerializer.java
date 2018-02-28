package com.softvision.ipm.pms.common.adapter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.softvision.ipm.pms.common.constants.YesNo;

public class YesNoSerializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(JsonParser jsonParser, DeserializationContext arg1)
            throws IOException, JsonProcessingException {
    	ObjectCodec oc = jsonParser.getCodec();
    	String value = oc.readValue(jsonParser, String.class);
    	YesNo yesNo = YesNo.get(value);
    	return (yesNo == YesNo.YES)? true : false;
    }

}

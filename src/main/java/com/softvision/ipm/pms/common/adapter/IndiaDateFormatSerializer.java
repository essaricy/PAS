package com.softvision.ipm.pms.common.adapter;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.softvision.ipm.pms.common.util.DateUtil;

public class IndiaDateFormatSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator jgen, SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        jgen.writeString(DateUtil.getIndianDateFormat(date));
    }

}

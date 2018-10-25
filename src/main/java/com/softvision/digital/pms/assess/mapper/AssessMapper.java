package com.softvision.digital.pms.assess.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softvision.digital.pms.assess.entity.AssessDetail;
import com.softvision.digital.pms.assess.entity.AssessHeader;
import com.softvision.digital.pms.assess.model.AssessDetailDto;
import com.softvision.digital.pms.assess.model.AssessHeaderDto;

@Component
public class AssessMapper {

    @Autowired ModelMapper mapper;

    public List<AssessHeaderDto> getAssessHeaderList(List<AssessHeader> all) {
        return mapper.map(all, new TypeToken<List<AssessHeaderDto>>() {}.getType());
    }

    public AssessHeaderDto getHeaderDto(AssessHeader object) {
        return mapper.map(object, AssessHeaderDto.class);
    }

    public List<AssessDetailDto> getAssessDetailList(List<AssessDetail> all) {
        return mapper.map(all, new TypeToken<List<AssessDetailDto>>() {}.getType());
    }

    public AssessDetailDto getDetailDto(AssessDetail object) {
        return mapper.map(object, AssessDetailDto.class);
    }

    public List<AssessHeader> getHeaders(List<AssessHeaderDto> list) {
        return mapper.map(list, new TypeToken<List<AssessHeader>>() {}.getType());
    }

    public AssessHeader getHeader(AssessHeaderDto dto) {
        return mapper.map(dto, AssessHeader.class);
    }

    public List<AssessDetail> getDetails(List<AssessDetailDto> list) {
        return mapper.map(list, new TypeToken<List<AssessDetail>>() {}.getType());
    }

    public AssessDetail getDetail(AssessDetailDto dto) {
        return mapper.map(dto, AssessDetail.class);
    }

}

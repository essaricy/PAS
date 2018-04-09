package com.softvision.ipm.pms.mappertest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.softvision.ipm.pms.assess.entity.AssessDetail;
import com.softvision.ipm.pms.assess.entity.AssessHeader;
import com.softvision.ipm.pms.assess.mapper.AssessMapper;
import com.softvision.ipm.pms.assess.model.AssessDetailDto;
import com.softvision.ipm.pms.assess.model.AssessHeaderDto;
import com.softvision.ipm.pms.assign.constant.PhaseAssignmentStatus;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Assessment_ModelToDto_MapperTest {

	@Autowired AssessMapper assessMapper;

    @Test
    public void test_headerNull() {
        assertNull(assessMapper.getHeaderDto((AssessHeader) null));
    }

    @Test
    public void test_detailNull() {
        assertNull(assessMapper.getDetailDto((AssessDetail) null));
    }

    @Test
    public void test_header() {
    	AssessHeader header = new AssessHeader();
    	header.setId(11L);
    	header.setAssignId(19L);
    	header.setAssessDate(new Date());
    	header.setAssessedBy(1136);
    	header.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());

    	int numberOfDetails = 20;
    	List<AssessDetail> details = new ArrayList<>();
    	for (int index = 0; index < numberOfDetails; index++) {
    	    AssessDetail assessDetail = new AssessDetail();
			assessDetail.setId((long)index);
			assessDetail.setTemplateHeaderId(34L);
			assessDetail.setRating(3.8);
			assessDetail.setScore(3.9);
			assessDetail.setComments("This is some comments for testing " + index);
    		details.add(assessDetail);
		}
		header.setAssessDetails(details);

    	AssessHeaderDto headerDto = assessMapper.getHeaderDto(header);
        assertNotNull(headerDto);
        assertEquals((Long)header.getId(), (Long)headerDto.getId());
        assertEquals((Long)header.getAssignId(), (Long)headerDto.getAssignId());
        assertEquals(header.getAssessDate(), headerDto.getAssessDate());
        assertEquals(header.getAssessDate(), headerDto.getAssessDate());
        assertEquals(header.getAssessedBy(), headerDto.getAssessedBy());
        assertEquals(header.getStatus(), headerDto.getStatus());

        List<AssessDetailDto> detailDtoList = headerDto.getAssessDetails();
        assertEquals(details.size(), detailDtoList.size());

        for (int index = 0; index < detailDtoList.size(); index++) {
            AssessDetail detail = details.get(index);
        	AssessDetailDto detailDto = detailDtoList.get(index);
        	
        	assertEquals((long)detail.getId(), (long)detailDto.getId());
        	assertEquals((long)detail.getTemplateHeaderId(), (long)detailDto.getTemplateHeaderId());
        	assertEquals((Double)detail.getRating(), (Double)detailDto.getRating());
            assertEquals((Double)detail.getScore(), (Double)detailDto.getScore());
            assertEquals(detail.getComments(), detailDto.getComments());
		}
    }

    @Test
    public void test_headerList() {
    	int numberOfHeaders = 20;
    	int numberOfDetails = 30;

    	List<AssessHeader> headers = new ArrayList<>();
    	for (int headerIndex = 0; headerIndex < numberOfHeaders; headerIndex++) {
    	    AssessHeader header = new AssessHeader();
            header.setId(11L);
            header.setAssignId(19L);
            header.setAssessDate(new Date());
            header.setAssessedBy(1136);
            header.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());

            List<AssessDetail> details = new ArrayList<>();
            for (int index = 0; index < numberOfDetails; index++) {
                AssessDetail assessDetail = new AssessDetail();
                assessDetail.setId((long)index);
                assessDetail.setTemplateHeaderId(34L);
                assessDetail.setRating(3.8);
                assessDetail.setScore(3.9);
                assessDetail.setComments("This is some comments for testing " + index);
                details.add(assessDetail);
            }
            header.setAssessDetails(details);
            headers.add(header);
		}

    	List<AssessHeaderDto> headerDtoList = assessMapper.getAssessHeaderList(headers);
    	assertEquals(headerDtoList.size(), headers.size());

    	for (int headerIndex = 0; headerIndex < headers.size(); headerIndex++) {
    	    AssessHeader header = headers.get(headerIndex);
    		AssessHeaderDto headerDto = headerDtoList.get(headerIndex);

    		assertNotNull(headerDto);
            assertEquals((Long)header.getId(), (Long)headerDto.getId());
            assertEquals((Long)header.getAssignId(), (Long)headerDto.getAssignId());
            assertEquals(header.getAssessDate(), headerDto.getAssessDate());
            assertEquals(header.getAssessDate(), headerDto.getAssessDate());
            assertEquals(header.getAssessedBy(), headerDto.getAssessedBy());
            assertEquals(header.getStatus(), headerDto.getStatus());

            List<AssessDetailDto> detailDtoList = headerDto.getAssessDetails();
            List<AssessDetail> details = header.getAssessDetails();
            assertEquals(details.size(), detailDtoList.size());

            for (int index = 0; index < detailDtoList.size(); index++) {
                AssessDetail detail = details.get(index);
                AssessDetailDto detailDto = detailDtoList.get(index);
                
                assertEquals((long)detail.getId(), (long)detailDto.getId());
                assertEquals((long)detail.getTemplateHeaderId(), (long)detailDto.getTemplateHeaderId());
                assertEquals((Double)detail.getRating(), (Double)detailDto.getRating());
                assertEquals((Double)detail.getScore(), (Double)detailDto.getScore());
                assertEquals(detail.getComments(), detailDto.getComments());
            }
		}
    }

    @Test
    public void test_detail() {

        AssessDetail detail = new AssessDetail();
        detail.setId((long)45);
        detail.setTemplateHeaderId(34L);
        detail.setRating(3.8);
        detail.setScore(3.9);
        detail.setComments("This is some comments for testing");

        AssessDetailDto detailDto = assessMapper.getDetailDto(detail);
        assertNotNull(detailDto);
        assertEquals((long)detail.getId(), (long)detailDto.getId());
        assertEquals((long)detail.getTemplateHeaderId(), (long)detailDto.getTemplateHeaderId());
        assertEquals((Double)detail.getRating(), (Double)detailDto.getRating());
        assertEquals((Double)detail.getScore(), (Double)detailDto.getScore());
        assertEquals(detail.getComments(), detailDto.getComments());
    }

    @Test
    public void test_detailList() {
        int numberOfDetails = 30;

        List<AssessDetail> details = new ArrayList<>();
        for (int index = 0; index < numberOfDetails; index++) {
            AssessDetail assessDetail = new AssessDetail();
            assessDetail.setId((long)index);
            assessDetail.setTemplateHeaderId(34L);
            assessDetail.setRating(3.8);
            assessDetail.setScore(3.9);
            assessDetail.setComments("This is some comments for testing " + index);
            details.add(assessDetail);
        }

        List<AssessDetailDto> detailDtoList = assessMapper.getAssessDetailList(details);
        assertEquals(details.size(), detailDtoList.size());

        for (int index = 0; index < detailDtoList.size(); index++) {
            AssessDetail detail = details.get(index);
            AssessDetailDto detailDto = detailDtoList.get(index);

            assertEquals((long)detail.getId(), (long)detailDto.getId());
            assertEquals((long)detail.getTemplateHeaderId(), (long)detailDto.getTemplateHeaderId());
            assertEquals((Double)detail.getRating(), (Double)detailDto.getRating());
            assertEquals((Double)detail.getScore(), (Double)detailDto.getScore());
            assertEquals(detail.getComments(), detailDto.getComments());
        }
    }

}

package com.softvision.digital.pms.assess.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import com.softvision.digital.pms.assess.entity.AssessDetail;
import com.softvision.digital.pms.assess.entity.AssessHeader;
import com.softvision.digital.pms.assess.mapper.AssessMapper;
import com.softvision.digital.pms.assess.model.AssessDetailDto;
import com.softvision.digital.pms.assess.model.AssessHeaderDto;
import com.softvision.digital.pms.assign.constant.PhaseAssignmentStatus;
import com.softvision.digital.pms.web.config.MyModelMapper;

public class AssessMapperTest {

	@Spy ModelMapper mapper = new MyModelMapper();

	@InjectMocks private AssessMapper assessMapper;

	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void prepare() {
		((MyModelMapper)mapper).loadMappings();
	}

    @Test
    public void shouldReturn_Null_When_AssessHeaderDto_isNull() {
        assertNull(assessMapper.getHeader((AssessHeaderDto) null));
    }

    @Test
    public void shouldReturn_Null_When_AssessDetailDto_isNull() {
        assertNull(assessMapper.getDetail((AssessDetailDto) null));
    }

    @Test
    public void shouldReturn_AssessHeader_When_AssessHeaderDto_isValid() {
    	AssessHeaderDto headerDto = new AssessHeaderDto();
    	headerDto.setId(11L);
    	headerDto.setAssignId(19L);
    	headerDto.setAssessDate(new Date());
    	headerDto.setAssessedBy(1136);
    	headerDto.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());

    	int numberOfDetails = 20;
    	List<AssessDetailDto> detailDtoList = new ArrayList<>();
    	for (int index = 0; index < numberOfDetails; index++) {
    	    AssessDetailDto assessDetailDto = new AssessDetailDto();
    	    assessDetailDto.setId((long)index);
    	    assessDetailDto.setTemplateHeaderId(34L);
    	    assessDetailDto.setRating(3.8);
    	    assessDetailDto.setScore(3.9);
    	    assessDetailDto.setComments("This is some comments for testing " + index);
    		detailDtoList.add(assessDetailDto);
		}
		headerDto.setAssessDetails(detailDtoList);

    	AssessHeader headers = assessMapper.getHeader(headerDto);
        assertNotNull(headers);
        assertEquals((Long)headerDto.getId(), (Long)headers.getId());
        assertEquals((Long)headerDto.getAssignId(), (Long)headers.getAssignId());
        assertEquals(headerDto.getAssessDate(), headers.getAssessDate());
        assertEquals(headerDto.getAssessDate(), headers.getAssessDate());
        assertEquals(headerDto.getAssessedBy(), headers.getAssessedBy());
        assertEquals(headerDto.getStatus(), headers.getStatus());

        List<AssessDetail> detailDtoList1 = headers.getAssessDetails();
        assertEquals(detailDtoList.size(), detailDtoList1.size());

        for (int index = 0; index < detailDtoList1.size(); index++) {
            AssessDetailDto detailDto = detailDtoList.get(index);
        	AssessDetail detail = detailDtoList1.get(index);
        	
        	assertEquals((long)detailDto.getId(), (long)detail.getId());
        	assertEquals((long)detailDto.getTemplateHeaderId(), (long)detail.getTemplateHeaderId());
        	assertEquals((Double)detailDto.getRating(), (Double)detail.getRating());
            assertEquals((Double)detailDto.getScore(), (Double)detail.getScore());
            assertEquals(detailDto.getComments(), detail.getComments());
		}
    }

    @Test
    public void shouldReturn_AssessHeaderList_When_AssessHeaderDtoList_isValid() {
    	int numberOfHeaders = 20;
    	int numberOfDetails = 30;

    	List<AssessHeaderDto> headerDtoList1 = new ArrayList<>();
    	for (int headerIndex = 0; headerIndex < numberOfHeaders; headerIndex++) {
    	    AssessHeaderDto headerDto = new AssessHeaderDto();
            headerDto.setId(11L);
            headerDto.setAssignId(19L);
            headerDto.setAssessDate(new Date());
            headerDto.setAssessedBy(1136);
            headerDto.setStatus(PhaseAssignmentStatus.MANAGER_REVIEW_SAVED.getCode());

            List<AssessDetailDto> detailDtoList = new ArrayList<>();
            for (int index = 0; index < numberOfDetails; index++) {
                AssessDetailDto assessDetailDto = new AssessDetailDto();
                assessDetailDto.setId((long)index);
                assessDetailDto.setTemplateHeaderId(34L);
                assessDetailDto.setRating(3.8);
                assessDetailDto.setScore(3.9);
                assessDetailDto.setComments("This is some comments for testing " + index);
                detailDtoList.add(assessDetailDto);
            }
            headerDto.setAssessDetails(detailDtoList);;
            headerDtoList1.add(headerDto);
		}

    	List<AssessHeader> headers = assessMapper.getHeaders(headerDtoList1);
    	assertEquals(headers.size(), headerDtoList1.size());

    	for (int headerIndex = 0; headerIndex < headerDtoList1.size(); headerIndex++) {
    	    AssessHeader header = headers.get(headerIndex);
            AssessHeaderDto headerDto = headerDtoList1.get(headerIndex);

    	    assertNotNull(header);
            assertEquals((Long)headerDto.getId(), (Long)header.getId());
            assertEquals((Long)headerDto.getAssignId(), (Long)header.getAssignId());
            assertEquals(headerDto.getAssessDate(), header.getAssessDate());
            assertEquals(headerDto.getAssessDate(), header.getAssessDate());
            assertEquals(headerDto.getAssessedBy(), header.getAssessedBy());
            assertEquals(headerDto.getStatus(), header.getStatus());

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
    public void shouldReturn_AssessDetail_When_AssessDetailDto_isValid() {

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
    public void shouldReturn_AssessDetailList_When_AssessDetailDtoList_isValid() {
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

    @Test
    public void shouldReturn_Null_When_AssessHeader_isNull() {
        assertNull(assessMapper.getHeaderDto((AssessHeader) null));
    }

    @Test
    public void shouldReturn_Null_When_AssessDetail_isNull() {
        assertNull(assessMapper.getDetailDto((AssessDetail) null));
    }

    @Test
    public void shouldReturn_AssessHeaderDto_When_AssessHeader_isValid() {
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
    public void shouldReturn_AssessHeaderDtoList_When_AssessHeaderList_isValid() {
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
    public void shouldReturn_AssessDetailDto_When_AssessDetail_isValid() {

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
    public void shouldReturn_AssessDetailDtoList_When_AssessDetailList_isValid() {
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

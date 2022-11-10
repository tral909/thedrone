package com.musalasoft.indorm1992.thedrone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musalasoft.indorm1992.thedrone.dto.DroneDto;
import com.musalasoft.indorm1992.thedrone.entity.Drone;
import com.musalasoft.indorm1992.thedrone.entity.DroneModel;
import com.musalasoft.indorm1992.thedrone.entity.DroneState;
import com.musalasoft.indorm1992.thedrone.entity.Medication;
import com.musalasoft.indorm1992.thedrone.repository.DroneRepository;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DroneControllerTest extends AbstractControllerTest {

	@Autowired
	private DroneRepository droneRepository;

	@Autowired
	private ObjectMapper mapper;

	@Test
	void registerDrone() throws Exception {
		assertEquals(0, droneRepository.findAll().size());

		DroneDto dto = DroneDto.builder()
				.serialNumber("123ABC")
				.model(DroneModel.LIGHT_WEIGHT)
				.weightLimitGrams(150)
				.batteryCapacity(75)
				.build();
		MvcResult result = mockMvc.perform(post("/api/v1/drone")
						.content(mapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		DroneDto outDto = mapper.readValue(response, DroneDto.class);

		Long outId = outDto.getId();
		assertNotNull(outId);
		assertEquals(dto.getSerialNumber(), outDto.getSerialNumber());
		assertEquals(dto.getModel(), outDto.getModel());
		assertEquals(dto.getWeightLimitGrams(), outDto.getWeightLimitGrams());
		assertEquals(dto.getBatteryCapacity(), outDto.getBatteryCapacity());

		assertEquals(1, droneRepository.findAll().size());
		Drone savedDrone = droneRepository.findById(outId)
				.orElseThrow(() -> new AssertionFailedError("drone by responded id " + outId + " not found"));

		assertEquals(dto.getSerialNumber(), savedDrone.getSerialNumber());
		assertEquals(dto.getModel(), savedDrone.getModel());
		assertEquals(dto.getWeightLimitGrams(), savedDrone.getWeightLimitGrams());
		assertEquals(dto.getBatteryCapacity(), savedDrone.getBatteryCapacity());
		assertSame(DroneState.IDLE, savedDrone.getState());
		List<Medication> meds = savedDrone.getMedications();
		assertNotNull(meds);
		assertEquals(0, meds.size());
	}

}

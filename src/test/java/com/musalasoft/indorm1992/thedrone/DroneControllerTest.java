package com.musalasoft.indorm1992.thedrone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musalasoft.indorm1992.thedrone.dto.DroneCreateDto;
import com.musalasoft.indorm1992.thedrone.dto.DroneOutDto;
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
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DroneControllerTest extends AbstractControllerTest {

	@Autowired
	private DroneRepository droneRepository;

	@Autowired
	private ObjectMapper mapper;

	@Test
	void registerDrone() throws Exception {
		assertEquals(10, droneRepository.findAll().size());

		DroneCreateDto dto = DroneCreateDto.builder()
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
		DroneOutDto outDto = mapper.readValue(response, DroneOutDto.class);

		Long outId = outDto.getId();
		assertNotNull(outId);
		assertEquals(dto.getSerialNumber(), outDto.getSerialNumber());
		assertSame(dto.getModel(), outDto.getModel());
		assertEquals(dto.getWeightLimitGrams(), outDto.getWeightLimitGrams());
		assertEquals(dto.getBatteryCapacity(), outDto.getBatteryCapacity());
		assertSame(DroneState.IDLE, outDto.getState());

		assertEquals(11, droneRepository.findAll().size());
		Drone savedDrone = droneRepository.findById(outId)
				.orElseThrow(() -> new AssertionFailedError("drone by responded id " + outId + " is not found"));

		assertEquals(outId, savedDrone.getId());
		assertEquals(outDto.getSerialNumber(), savedDrone.getSerialNumber());
		assertSame(outDto.getModel(), savedDrone.getModel());
		assertEquals(outDto.getWeightLimitGrams(), savedDrone.getWeightLimitGrams());
		assertEquals(outDto.getBatteryCapacity(), savedDrone.getBatteryCapacity());
		assertSame(outDto.getState(), savedDrone.getState());
		List<Medication> meds = savedDrone.getMedications();
		assertNotNull(meds);
		assertEquals(0, meds.size());
	}

	@Test
	void getDroneBatteryLevelById() throws Exception {
		// see data.sql (its first inserted drone)
		mockMvc.perform(get("/api/v1/drone/{id}/battery-level", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.percentages", is(60)));
	}

	@Test
	void getAllFleetOfDrones() throws Exception {
		// data.sql inserts 10 drones, we check only first in detail
		mockMvc.perform(get("/api/v1/drone"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(10)))
				.andExpect(jsonPath("$.[0].id", is(1)))
				.andExpect(jsonPath("$.[0].serialNumber", is("QWE1")))
				.andExpect(jsonPath("$.[0].model", is("LIGHT_WEIGHT")))
				.andExpect(jsonPath("$.[0].weightLimitGrams", is(120)))
				.andExpect(jsonPath("$.[0].batteryCapacity", is(60)))
				.andExpect(jsonPath("$.[0].state", is("IDLE")));
	}

	@Test
	void getAvailableForLoadingDrones() throws Exception {
		// data.sql has 6 available drones (IDLE or LOADING status)
		MvcResult result = mockMvc.perform(get("/api/v1/drone/available-for-loading"))
				.andExpect(status().isOk())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		List<DroneOutDto> outDtos = mapper.readValue(response, new TypeReference<>(){});
		Set<DroneState> outStates = outDtos.stream()
				.map(DroneOutDto::getState)
				.collect(Collectors.toSet());

		assertEquals(2, outStates.size());
		assertTrue(outStates.contains(DroneState.IDLE));
		assertTrue(outStates.contains(DroneState.LOADING));
	}
}

package com.musalasoft.indorm1992.thedrone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musalasoft.indorm1992.thedrone.dto.DroneCreateDto;
import com.musalasoft.indorm1992.thedrone.dto.DroneLoadingDto;
import com.musalasoft.indorm1992.thedrone.dto.DroneOutDto;
import com.musalasoft.indorm1992.thedrone.dto.MedicationDto;
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

import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

		DroneCreateDto dto = new DroneCreateDto(
				null,
				"123ABC",
				DroneModel.LIGHT_WEIGHT,
				150,
				75
		);
				/*.serialNumber("123ABC")
				.model(DroneModel.LIGHT_WEIGHT)
				.weightLimitGrams(150)
				.batteryCapacity(75)
				.build();*/
		MvcResult result = mockMvc.perform(post("/api/v1/drone")
						.content(mapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
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
		// data.sql has 5 available drones (IDLE or LOADING status)
		MvcResult result = mockMvc.perform(get("/api/v1/drone/available-for-loading"))
				.andExpect(status().isOk())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		List<DroneOutDto> outDtos = mapper.readValue(response, new TypeReference<>(){});

		// checking battery minimum for loading
		assertTrue(outDtos.stream().allMatch(dr -> dr.getBatteryCapacity() >= 25));
		Set<DroneState> outStates = outDtos.stream()
				.map(DroneOutDto::getState)
				.collect(Collectors.toSet());

		// checking statuses
		assertEquals(2, outStates.size());
		assertTrue(outStates.contains(DroneState.IDLE));
		assertTrue(outStates.contains(DroneState.LOADING));
	}

	@Test
	void loadDroneWithMedications() throws Exception {
		Long droneId = 1L;
		Drone drone = droneRepository.findById(droneId)
				.orElseThrow(() -> new AssertionFailedError("drone with id=" + droneId + " is not found"));

		assertEquals(0, drone.getMedications().size());

		DroneLoadingDto dto = new DroneLoadingDto();
		MedicationDto medDto = new MedicationDto(
				"PAINKILLER",
				"pills.jpg".getBytes(),
				50,
				"137_CODE"
		);
		dto.getMedications().add(medDto);

		mockMvc.perform(put("/api/v1/drone/{id}/medication", droneId)
						.content(mapper.writeValueAsString(dto))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		drone = droneRepository.findById(droneId)
				.orElseThrow(() -> new AssertionFailedError("drone with id=" + droneId + " is not found"));

		assertEquals(1, drone.getMedications().size());
		Medication med = drone.getMedications().get(0);
		assertEquals(medDto.getName(), med.getName());
		assertArrayEquals(medDto.getImage(), med.getImage());
		assertEquals(medDto.getWeightGrams(), med.getWeightGrams());
		assertEquals(medDto.getCode(), med.getCode());
	}

	@Test
	void getLoadedMedicationByDroneId() throws Exception {
		// data.sql inserts 2 medications, we check only first in detail
		String originalImage = "test_pic1.png";
		String expectedImage = Base64.getEncoder().encodeToString(originalImage.getBytes());
		mockMvc.perform(get("/api/v1/drone/10/medication"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$.[0].name", is("MED1")))
				.andExpect(jsonPath("$.[0].image", is(expectedImage)))
				.andExpect(jsonPath("$.[0].weightGrams", is(100)))
				.andExpect(jsonPath("$.[0].code", is("CODE1")));
	}
}

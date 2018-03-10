package de.tfsw.temp.temperature.server;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * CRUD repo for {@link TemperatureMeasurement}s.
 * 
 * @author thorsten
 *
 */
@Repository
public interface TemperatureRepository extends JpaRepository<TemperatureMeasurement, Long> {

	/**
	 * Returns all measurements for a specific name.
	 * 
	 * @param name name of the measurement
	 * @return a list of measurement points
	 */
	List<TemperatureMeasurement> findByName(@Param("name") String name);
	
	/**
	 * Returns the latest measurement (i.e. the one with the most recent timestamp) 
	 * @param name name of the measurement
	 * @return the most recent measurement
	 */
	TemperatureMeasurement findFirstByNameOrderByTimestampDesc(@Param("name") String name);
	
	/**
	 * A distinct list of all measurement names.
	 * @return a distinct list of all measurement names
	 */
	@Query("select distinct (name) from TemperatureMeasurement")
	List<String> findAllNames();
}
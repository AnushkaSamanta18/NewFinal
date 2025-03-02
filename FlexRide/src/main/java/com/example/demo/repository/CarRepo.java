
package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CarEntity;

@Repository
public interface CarRepo extends JpaRepository<CarEntity, Integer> {
	
	
	@Query(nativeQuery=true,value="select is_booked from car_table where id=?")
	public Boolean isBooked(@Param("id") int id);
	
}

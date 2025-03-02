
package com.example.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

/*import javax.persistence.*;*/

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class BookingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@ManyToOne
	@JoinColumn(name = "car_id", nullable = false)
	private CarEntity car;

	private String address;
	
	private double pricePerDay;
	@Column(name="startDateAndTime")
	private String startDateAndTime;
	@Column(name="endDateAndTime")
	private String endDateAndTime;

	public BookingEntity() {
	}

	public BookingEntity(UserEntity user, CarEntity car,String startDateAndTime,String endDateAndTime) {
		this.user = user;
		this.car = car;
		this.address = user.getAddress();
		this.pricePerDay = car.getPricePerDay();
		this.startDateAndTime=startDateAndTime;
		this.endDateAndTime=endDateAndTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public CarEntity getCar() {
		return car;
	}

	public void setCar(CarEntity car) {
		this.car = car;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	public String getStartDateAndTime() {
		return startDateAndTime;
	}

	public void setStartDateAndTime(String startDateAndTime) {
		this.startDateAndTime = startDateAndTime;
	}

	public String getEndDateAndTime() {
		return endDateAndTime;
	}

	public void setEndDateAndTime(String endDateAndTime) {
		this.endDateAndTime = endDateAndTime;
	}


	
}
package com.student.successreject.model;

import java.util.Objects;

public class Student {

	private int studentId;
	private String name;
	private float percentage;
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPercentage() {
		return percentage;
	}
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		return Objects.equals(name, other.name)
				&& Float.floatToIntBits(percentage) == Float.floatToIntBits(other.percentage)
				&& studentId == other.studentId;
	}
	@Override
	public String toString() {
		return "Student [studentId=" + studentId + ", name=" + name + ", percentage=" + percentage + "]";
	}
	
	
	
}

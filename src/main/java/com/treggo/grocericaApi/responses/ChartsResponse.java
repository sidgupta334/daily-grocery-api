package com.treggo.grocericaApi.responses;

import java.time.LocalDate;

public class ChartsResponse {

	private String type;
	private LocalDate[] labels;
	private Long[] data;

	public ChartsResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChartsResponse(String type, LocalDate[] labels, Long[] data) {
		super();
		this.type = type;
		this.labels = labels;
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LocalDate[] getLabels() {
		return labels;
	}

	public void setLabels(LocalDate[] labels) {
		this.labels = labels;
	}

	public Long[] getData() {
		return data;
	}

	public void setData(Long[] data) {
		this.data = data;
	}

}

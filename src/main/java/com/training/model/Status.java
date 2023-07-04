package com.training.model;

public enum Status {
	
		PENDING("PENDING"), ACCEPTED("ACCEPTED"), PROCESSING("PROCESSING"), COMPLETED("COMPLETED"),
		INTERESTED("INTERESTED"),NOTINTERESTED("NOTINTERESTED"),RETRY("RETRY");

		private String status;

		Status(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}
	}



package com.cp2196g03gr01.model;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Mail {
	private String from;
	private String mailTo;
	private String subject;
	private List<Object> attachments;
	private Map<String, Object> props;
}

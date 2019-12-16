package com.eeventbox.exception.file;

public class MyFileNotFoundException extends RuntimeException {

	public MyFileNotFoundException(String message) {super(message);}
	public MyFileNotFoundException(String message, Throwable cause) {super(message, cause);}

}

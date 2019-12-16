package com.eeventbox.payload.file;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileResponse {
	private String fileName;
	private String fileDownloadUri;
	private String fileType;
	private Long size;
}

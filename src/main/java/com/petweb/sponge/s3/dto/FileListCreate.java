package com.petweb.sponge.s3.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class FileListCreate {

    private List<String> fileUrlList;
}

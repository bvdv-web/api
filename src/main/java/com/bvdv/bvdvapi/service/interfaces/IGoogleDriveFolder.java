package com.bvdv.bvdvapi.service.interfaces;

import com.bvdv.bvdvapi.dto.GoogleDriveFoldersDTO;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface IGoogleDriveFolder {
    List<GoogleDriveFoldersDTO> getAllFolder() throws IOException, GeneralSecurityException;
    void createFolder(String folderName) throws Exception;
    void deleteFolder(String id) throws Exception;
    List<File> listFolderContent(String folderId) throws IOException, GeneralSecurityException;
}
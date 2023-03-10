package com.stein.fileuploader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

@Controller
public class FileUploadController {

    @RequestMapping("/")
    public String fileUploadForm() {
        return "fileUploadForm";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file")MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bitte Datei zum Uploaden auswählen.");
            return "redirect:/";
        }

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        Month currentMonth = currentDate.getMonth();
        String currentMonthAndYearString = currentMonth.toString() + " " + currentYear;

        Path path;
        // Change DIRPATH to access your own folder
        String DIRPATH = "/Users/istein/Documents/JavaProjects/FileUploader2/files/";
        String directoryName = DIRPATH.concat(currentMonthAndYearString);
        String fileName = file.getOriginalFilename();
        File directory = new File(directoryName);

        if (!directory.exists()){
            directory.mkdir();
        }

        int fileCount = countFilesInCurrentDirectory(directory);

        if (fileCount == 0) {
            path = Paths.get(directoryName, "0" + 1 + "-" + fileName);
        } else if (fileCount < 10) {
            path = Paths.get(directoryName, "0" + (fileCount + 1) + "-" + fileName);
        } else {
            path = Paths.get(directoryName, (fileCount + 1) + "-" + fileName);
        }

        Files.write(path, file.getBytes());
        redirectAttributes.addFlashAttribute("successMessage", file.getOriginalFilename() + " erfolgreich geuploadet");
        return "redirect:/";
    }

    public int countFilesInCurrentDirectory(File directory) {
        int count = 0;
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile()) {
                count++;
            }
        }

        return count;
    }
}

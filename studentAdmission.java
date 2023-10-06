package com.auca.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import javax.servlet.http.Part;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/studentAdmission")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,  // 1 MB
    maxFileSize = 1024 * 1024 * 5,    // 5 MB
    maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class studentAdmission extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    // Get the file parts from the request
	    Collection<Part> parts = request.getParts();

	    // Get the base directory of your web application
	    String baseDir = getServletContext().getRealPath("");

	    for (Part part : parts) {
	        // Process each part
	        if (part.getContentType() != null) {
	            // This is a file part
	            String fileName = getFileName(part);

	            // Check file type and save to the server
	            if (fileName != null && (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".pdf"))) {
	                // Specify the relative directory path where you want to store the files
	                String relativeDir = "uploads"; // Change this to your desired directory
	                
	                // Construct the complete file path using Paths
	                String filePath = Paths.get(baseDir, relativeDir, fileName).toString();

	                // Create the directory if it doesn't exist
	                Files.createDirectories(Paths.get(baseDir, relativeDir));

	                // Save the file to the server
	                part.write(filePath);
	                
	                // Handle successful upload here (e.g., display a success message)
	                PrintWriter out = response.getWriter();
	                out.println("Uploaded Successfully");
	            } else {
	                // Handle invalid file type
	                PrintWriter out = response.getWriter();
	                out.println("Uploaded Unsuccessfully");
	            }
	        }
	    }
	}

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
	
}

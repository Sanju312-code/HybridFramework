package com.ActionKeywords;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PDFUtil {
	
//Sample-Fillable-PDF,conversion-services,EDIT OoPdfFormExample1,
	static String pdfpath = System.getProperty("user.dir") + "/src/main/resources/form-cms1500.pdf";
	static String jsonfile = System.getProperty("user.dir") + "/Reports/Json/pdfData3.json";

	/**
	 * Reads AcroForm fields from a PDF and stores them in JSON format.
	 * 
	 * @param pdfPath  Path of the input PDF
	 * @param jsonPath Path of the output JSON file
	 * @throws IOException
	 */
	public static void convertPdfFormToJson(String pdfPath, String jsonPath) throws IOException {
		// Load the PDF
		try (PDDocument document = PDDocument.load(new File(pdfPath))) {

			PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
			if (acroForm == null) {
				throw new IOException("No AcroForm found in the PDF.");
			}

			Map<String, String> formData = new HashMap<>();

			// Extract all fields
			for (PDField field : acroForm.getFields()) {
				formData.put(field.getFullyQualifiedName(), field.getValueAsString());
			}

			// Convert to JSON
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(formData);

			// ✅ Print JSON to console
			System.out.println("======= PDF Data in JSON Format =======");
			System.out.println(jsonString);
			System.out.println("======================================");

			// Write to file
			try (FileWriter writer = new FileWriter(jsonPath)) {
				writer.write(jsonString);
			}

			System.out.println("✅ PDF form data exported to JSON: " + jsonPath);
		}
	}

	// Example usage
	public static void main(String[] args) throws IOException {
		String pdfFile = pdfpath;
		String jsonFile = jsonfile;

		convertPdfFormToJson(pdfFile, jsonFile);
	}

}

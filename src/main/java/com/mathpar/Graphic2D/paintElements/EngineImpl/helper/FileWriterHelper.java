package com.mathpar.Graphic2D.paintElements.EngineImpl.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterHelper {
    public static void writeFormulaToFile(String formula, String fileName) {
        File myFile = new File(fileName);
        try {
            myFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(myFile);
            myWriter.write(formula);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

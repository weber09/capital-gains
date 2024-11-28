package org.capitalgains;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.capitalgains.handler.CapitalGainsTaxCalculatorHandler;
import org.capitalgains.handler.CapitalGainsTaxCalculatorHandlerImpl;
import org.capitalgains.model.Operation;
import org.capitalgains.model.Tax;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            String line;
            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
                // Parse each line as an array of Operations
                List<Operation> operations = mapper.readValue(line, new TypeReference<>() {});

                CapitalGainsTaxCalculatorHandler capitalGainsTaxesCalculatorHandler = new CapitalGainsTaxCalculatorHandlerImpl();
                List<Tax> capitalGainsTaxes = capitalGainsTaxesCalculatorHandler.calculateTaxes(operations);

                // Print taxes as JSON array
                printOutput(capitalGainsTaxes, mapper);
            }
        } catch (Exception e) {
            System.err.println("Error processing input: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printOutput(List<Tax> taxes, ObjectMapper mapper) {
        try {
            String jsonTaxes = mapper.writeValueAsString(taxes);
            System.out.println(jsonTaxes);
        } catch (JsonProcessingException e) {
            System.err.println("Error converting taxes to JSON: " + e.getMessage());
        }
    }
}
package com.example.unitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText resultDisplayField;
    private EditText userInputField;
    private Spinner sourceUnitDropdown;
    private Spinner targetUnitDropdown;
    
    // Conversion factors to meters (base unit)
    private final Map<String, Double> unitToMeterMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initializeConversionFactors();
        setupViewReferences();
    }

    private void initializeConversionFactors() {
        unitToMeterMap.put("Meter", 1.0);
        unitToMeterMap.put("Millimeter", 0.001);
        unitToMeterMap.put("Mile", 1609.344);
        unitToMeterMap.put("Foot", 0.3048);
    }
    
    private void setupViewReferences() {
        userInputField = findViewById(R.id.inputNumberField);
        resultDisplayField = findViewById(R.id.outputDisplayField);
        sourceUnitDropdown = findViewById(R.id.dropdownSourceUnit);
        targetUnitDropdown = findViewById(R.id.dropdownTargetUnit);
    }

    public void performUnitConversion(View clickedView) {
        try {
            String selectedSourceUnit = sourceUnitDropdown.getSelectedItem().toString();
            String selectedTargetUnit = targetUnitDropdown.getSelectedItem().toString();
            
            String inputText = userInputField.getText().toString().trim();
            if (inputText.isEmpty()) {
                showErrorMessage("Please enter a value to convert");
                return;
            }
            
            double inputValue = Double.parseDouble(inputText);
            double convertedResult = calculateConversion(inputValue, selectedSourceUnit, selectedTargetUnit);
            
            displayConversionResult(convertedResult);
            
        } catch (NumberFormatException exception) {
            showErrorMessage("Invalid number format. Please enter a valid number.");
        } catch (Exception exception) {
            showErrorMessage("Conversion failed. Please try again.");
        }
    }
    
    private double calculateConversion(double inputAmount, String fromUnit, String toUnit) {
        // Convert input to meters first (base unit)
        double valueInMeters = inputAmount * unitToMeterMap.get(fromUnit);
        
        // Convert from meters to target unit
        double conversionFactor = unitToMeterMap.get(toUnit);
        return valueInMeters / conversionFactor;
    }
    
    private void displayConversionResult(double result) {
        String formattedResult = String.format("%.6f", result);
        resultDisplayField.setText(formattedResult);
    }
    
    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
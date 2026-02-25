package com.example.dailyexpenseapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextAmount, editTextDescription;
    private Spinner spinnerCategory;
    private RadioGroup radioGroupPayment;
    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private List<Expense> expenseList;

    private final String ACCOUNTANT_PHONE = "1234567890";
    private final String ACCOUNTANT_EMAIL = "accountant@example.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
        recyclerView = findViewById(R.id.recyclerViewExpenses);

        // Spinner setup
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.expense_categories, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        // RecyclerView setup
        expenseList = new ArrayList<>();
        adapter = new ExpenseAdapter(expenseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button btnAddExpense = findViewById(R.id.btnAddExpense);
        btnAddExpense.setOnClickListener(v -> addExpense());

        Button btnCall = findViewById(R.id.btnCallAccountant);
        btnCall.setOnClickListener(v -> showConfirmationDialog("Call", "Do you want to call the accountant?", this::callAccountant));

        Button btnSMS = findViewById(R.id.btnSendSMS);
        btnSMS.setOnClickListener(v -> showConfirmationDialog("SMS", "Do you want to send an SMS to the accountant?", this::sendSMS));

        Button btnEmail = findViewById(R.id.btnSendEmail);
        btnEmail.setOnClickListener(v -> sendEmail());
    }

    private void addExpense() {
        String amountStr = editTextAmount.getText().toString();
        String description = editTextDescription.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        int selectedId = radioGroupPayment.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String paymentMode = radioButton.getText().toString();

        if (amountStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);
        Expense expense = new Expense(description, amount, category, paymentMode);
        expenseList.add(0, expense);
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);

        // Clear fields
        editTextAmount.setText("");
        editTextDescription.setText("");
    }

    private void showConfirmationDialog(String title, String message, Runnable action) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(title, (dialog, which) -> action.run())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void callAccountant() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + ACCOUNTANT_PHONE));
        startActivity(intent);
    }

    private void sendSMS() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:" + ACCOUNTANT_PHONE));
        intent.putExtra("sms_body", "Hello Accountant, I have a query regarding my expenses.");
        startActivity(intent);
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ACCOUNTANT_EMAIL});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Expense Report");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello Accountant,\n\nPlease find my expense report attached.");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.raffelberg.cr_ticker.ui.logIn;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.raffelberg.cr_ticker.R;
import com.raffelberg.cr_ticker.databinding.FragmentLogInBinding;


public class LogInFragment extends Fragment {

   private FragmentLogInBinding binding;
   private EditText passwordInput;
   private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLogInBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button logInButton = binding.logInButton;
        logInButton.setOnClickListener(v -> { logIn(); });

        passwordInput = binding.logInInput;

        return root;
    }

    private void logIn() {
        String password = passwordInput.getText().toString();
        int destination = getArguments().getInt("destination");

        mAuth.signInAnonymously().addOnCompleteListener(task -> {
            Bundle bundle = new Bundle();
            if(password.equals("123648")){
                Log.i("destination", String.valueOf(destination));
                bundle.putString("id", "1-Herren");
                navigateToTarget(destination, bundle);
            }
            if(password.equals("483612")){
                bundle.putString("id", "1-Damen");
                navigateToTarget(destination, bundle);
            }
        });
    }

    private void navigateToTarget(int destination, Bundle bundle){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);

        if(destination == R.id.nav_addmatch)
            navController.navigate(R.id.action_logInFragment_to_nav_addmatch, bundle);
        if(destination == R.id.nav_editMatch)
            navController.navigate(R.id.action_logInFragment_to_nav_editMatch, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
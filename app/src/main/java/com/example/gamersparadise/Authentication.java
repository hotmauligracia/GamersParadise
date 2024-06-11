package com.example.gamersparadise;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.gamersparadise.data.User;
import com.example.gamersparadise.onboarding.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Authentication {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    public Authentication(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void registerUser(Activity activity, User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        if (firebaseUser != null) {
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user.getUsername())
                                    .build();

                            firebaseUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d("Authentication", "Profil user diperbarui.");
                                        }
                                    });

                            saveUserToFirestore(firebaseUser.getUid(), user);
                            sendEmailVerification(activity, firebaseUser);
                        }

                        Log.d("Authentication", "createUserWithEmail:success");
                        Toast.makeText(activity, "Registrasi berhasil. Verifikasi email Anda.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w("Authentication", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(activity, "Registrasi gagal.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(String uid, User user) {
        db.collection("users").document(uid).set(user);
    }

    private void sendEmailVerification(Activity activity, FirebaseUser firebaseUser) {
        firebaseUser.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mAuth.signOut();
                Intent loginIntent = new Intent(activity, LoginActivity.class);
                activity.startActivity(loginIntent);
                activity.finish();
            } else {
                Log.e("Authentication", "sendEmailVerification:failure", task.getException());
                Toast.makeText(activity, "Gagal mengirim email verifikasi.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginUser(Activity activity, String email, String password, FirebaseLoginCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();

                        if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                            checkIfAdmin(firebaseUser.getUid(), callback);
                        } else {
                            mAuth.signOut();
                            Toast.makeText(activity, "Email belum diverifikasi.", Toast.LENGTH_SHORT).show();
                        }

                        Log.d("Authentication", "signInWithEmail:success");
                    } else {
                        Log.w("Authentication", "signInWithEmail:failure", task.getException());
                        Toast.makeText(activity, "Login gagal.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfAdmin(String uid, FirebaseLoginCallback callback) {
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Boolean isAdmin = document.getBoolean("admin");
                    callback.onSuccess(isAdmin != null && isAdmin);
                } else {
                    callback.onFailure();
                }
            } else {
                callback.onFailure();
            }
        });
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void logoutUser(Context context) {
        mAuth.signOut();
        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loginIntent);
    }

    public interface FirebaseLoginCallback {
        void onSuccess(boolean isAdmin);
        void onFailure();
    }
}

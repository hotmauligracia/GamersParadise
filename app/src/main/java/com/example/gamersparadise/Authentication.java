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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

import java.util.Map;
import java.util.Objects;

public class Authentication {
    private static final String TAG = "Authentication";
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;

    public Authentication(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public interface FirebaseLoginCallback {
        void onSuccess(boolean isAdmin);
        void onFailure();
    }

    public interface FirebaseCollectionCallback {
        void onSuccess(QuerySnapshot querySnapshot);
        void onFailure(String errorMessage);
    }

    public interface FirebaseDocumentCallback {
        void onSuccess(DocumentSnapshot document);
        void onFailure(String errorMessage);
    }

    public interface FirebaseDocumentAddCallback {
        void onSuccess(String documentId);
        void onFailure(String errorMessage);
    }

    public interface FirebaseDocumentDeleteCallback {
        void onSuccess();
        void onFailure(String errorMessage);
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
                                            Log.d(TAG, "Profil user diperbarui.");
                                        }
                                    });

                            saveUserToFirestore(firebaseUser.getUid(), user);
                            sendEmailVerification(activity, firebaseUser);
                        }

                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(activity, "Registrasi berhasil. Verifikasi email Anda.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
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
                Log.e(TAG, "sendEmailVerification:failure", task.getException());
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

                        Log.d(TAG, "signInWithEmail:success");
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
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

    public void logoutUser(Context context) {
        mAuth.signOut();
        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loginIntent);
    }

    public void fetchCollectionData(String collectionName, FirebaseCollectionCallback callback) {
        db.collection(collectionName)
                .get()
                .addOnSuccessListener(callback::onSuccess)
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void addDocumentData(String collectionName, Map<String, Object> data, final FirebaseDocumentAddCallback callback) {
        CollectionReference collectionReference = db.collection(collectionName);
        collectionReference.add(data)
                .addOnSuccessListener(documentReference -> callback.onSuccess(documentReference.getId()))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void updateDocumentData(String collectionName, String documentId, Map<String, Object> data, FirebaseDocumentCallback callback) {
        DocumentReference documentReference = db.collection(collectionName).document(documentId);
        documentReference.set(data, SetOptions.merge())
                .addOnSuccessListener(aVoid -> documentReference.get()
                        .addOnSuccessListener(callback::onSuccess)
                        .addOnFailureListener(e -> callback.onFailure(e.getMessage())))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void deleteDocumentData(String collectionName, String documentId, final FirebaseDocumentDeleteCallback callback) {
        DocumentReference documentReference = db.collection(collectionName).document(documentId);
        documentReference.delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public DocumentReference getDocumentRef(String collectionName, String documentId) {
        if (getCurrentUser() != null) {
            return db.collection(collectionName).document(documentId);
        }
        return null;
    }

    public void loadDocumentData(String collectionName, String documentId, FirebaseDocumentCallback callback) {
        DocumentReference documentRef = getDocumentRef(collectionName, documentId);
        if (documentRef != null) {
            documentRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        callback.onSuccess(document);
                    }
                } else {
                    callback.onFailure("Failed to load document: " + Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        } else {
            callback.onFailure("No user logged in");
        }
    }

    public void saveDocumentData(String collectionName, String documentId, Map<String, Object> data, FirebaseDocumentCallback callback) {
        DocumentReference documentRef = getDocumentRef(collectionName, documentId);
        if (documentRef != null) {
            documentRef.set(data).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onFailure("Failed to save document: " + Objects.requireNonNull(task.getException()).getMessage());
                }
            });
        } else {
            callback.onFailure("No user logged in");
        }
    }

    public interface FirebaseDocumentCallback {
        void onSuccess(DocumentSnapshot document);
        void onFailure(String errorMessage);
    }

    public void deleteDocumentData(String collectionName, String documentId, FirebaseDocumentDeleteCallback callback) {
        if (collectionName != null && documentId != null) {
            db.collection(collectionName).document(documentId).delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure("Failed to delete document: " + task.getException());
                        }
                    });
        } else {
            callback.onFailure("Collection name or document ID is null.");
        }
    }

    public interface FirebaseDocumentDeleteCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}

// Generated by view binder compiler. Do not edit!
package com.kamui.fooddonation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.kamui.fooddonation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySigninBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button button1;

  @NonNull
  public final CardView cardView;

  @NonNull
  public final ImageView imgLogo;

  @NonNull
  public final EditText signInEmailInput;

  @NonNull
  public final EditText signInPasswordInput;

  @NonNull
  public final TextView tvFirstText;

  private ActivitySigninBinding(@NonNull LinearLayout rootView, @NonNull Button button1,
      @NonNull CardView cardView, @NonNull ImageView imgLogo, @NonNull EditText signInEmailInput,
      @NonNull EditText signInPasswordInput, @NonNull TextView tvFirstText) {
    this.rootView = rootView;
    this.button1 = button1;
    this.cardView = cardView;
    this.imgLogo = imgLogo;
    this.signInEmailInput = signInEmailInput;
    this.signInPasswordInput = signInPasswordInput;
    this.tvFirstText = tvFirstText;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySigninBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySigninBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_signin, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySigninBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.button1;
      Button button1 = ViewBindings.findChildViewById(rootView, id);
      if (button1 == null) {
        break missingId;
      }

      id = R.id.cardView;
      CardView cardView = ViewBindings.findChildViewById(rootView, id);
      if (cardView == null) {
        break missingId;
      }

      id = R.id.img_logo;
      ImageView imgLogo = ViewBindings.findChildViewById(rootView, id);
      if (imgLogo == null) {
        break missingId;
      }

      id = R.id.sign_in_email_input;
      EditText signInEmailInput = ViewBindings.findChildViewById(rootView, id);
      if (signInEmailInput == null) {
        break missingId;
      }

      id = R.id.sign_in_password_input;
      EditText signInPasswordInput = ViewBindings.findChildViewById(rootView, id);
      if (signInPasswordInput == null) {
        break missingId;
      }

      id = R.id.tv_firstText;
      TextView tvFirstText = ViewBindings.findChildViewById(rootView, id);
      if (tvFirstText == null) {
        break missingId;
      }

      return new ActivitySigninBinding((LinearLayout) rootView, button1, cardView, imgLogo,
          signInEmailInput, signInPasswordInput, tvFirstText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

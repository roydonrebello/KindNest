// Generated by view binder compiler. Do not edit!
package com.kamui.fooddonation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public final class NgoCardviewBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final ImageView deleteIcon;

  @NonNull
  public final TextView ngoAddress;

  @NonNull
  public final CardView ngoCardView;

  @NonNull
  public final TextView ngoEmail;

  @NonNull
  public final ImageView ngoImage;

  @NonNull
  public final TextView ngoName;

  private NgoCardviewBinding(@NonNull CardView rootView, @NonNull ImageView deleteIcon,
      @NonNull TextView ngoAddress, @NonNull CardView ngoCardView, @NonNull TextView ngoEmail,
      @NonNull ImageView ngoImage, @NonNull TextView ngoName) {
    this.rootView = rootView;
    this.deleteIcon = deleteIcon;
    this.ngoAddress = ngoAddress;
    this.ngoCardView = ngoCardView;
    this.ngoEmail = ngoEmail;
    this.ngoImage = ngoImage;
    this.ngoName = ngoName;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static NgoCardviewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NgoCardviewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.ngo_cardview, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NgoCardviewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.delete_icon;
      ImageView deleteIcon = ViewBindings.findChildViewById(rootView, id);
      if (deleteIcon == null) {
        break missingId;
      }

      id = R.id.ngo_address;
      TextView ngoAddress = ViewBindings.findChildViewById(rootView, id);
      if (ngoAddress == null) {
        break missingId;
      }

      CardView ngoCardView = (CardView) rootView;

      id = R.id.ngo_email;
      TextView ngoEmail = ViewBindings.findChildViewById(rootView, id);
      if (ngoEmail == null) {
        break missingId;
      }

      id = R.id.ngo_image;
      ImageView ngoImage = ViewBindings.findChildViewById(rootView, id);
      if (ngoImage == null) {
        break missingId;
      }

      id = R.id.ngo_name;
      TextView ngoName = ViewBindings.findChildViewById(rootView, id);
      if (ngoName == null) {
        break missingId;
      }

      return new NgoCardviewBinding((CardView) rootView, deleteIcon, ngoAddress, ngoCardView,
          ngoEmail, ngoImage, ngoName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

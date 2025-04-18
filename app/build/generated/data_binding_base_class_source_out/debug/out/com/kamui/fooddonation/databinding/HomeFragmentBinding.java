// Generated by view binder compiler. Do not edit!
package com.kamui.fooddonation.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.kamui.fooddonation.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class HomeFragmentBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button donate;

  @NonNull
  public final TextView emptyView;

  @NonNull
  public final RecyclerView recyclerViewDonations;

  @NonNull
  public final RecyclerView recyclerViewNgo;

  @NonNull
  public final TextView tvFirstText;

  @NonNull
  public final TextView tvSecondText;

  private HomeFragmentBinding(@NonNull LinearLayout rootView, @NonNull Button donate,
      @NonNull TextView emptyView, @NonNull RecyclerView recyclerViewDonations,
      @NonNull RecyclerView recyclerViewNgo, @NonNull TextView tvFirstText,
      @NonNull TextView tvSecondText) {
    this.rootView = rootView;
    this.donate = donate;
    this.emptyView = emptyView;
    this.recyclerViewDonations = recyclerViewDonations;
    this.recyclerViewNgo = recyclerViewNgo;
    this.tvFirstText = tvFirstText;
    this.tvSecondText = tvSecondText;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static HomeFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static HomeFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.home_fragment, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static HomeFragmentBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.donate;
      Button donate = ViewBindings.findChildViewById(rootView, id);
      if (donate == null) {
        break missingId;
      }

      id = R.id.empty_view;
      TextView emptyView = ViewBindings.findChildViewById(rootView, id);
      if (emptyView == null) {
        break missingId;
      }

      id = R.id.recycler_view_donations;
      RecyclerView recyclerViewDonations = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewDonations == null) {
        break missingId;
      }

      id = R.id.recycler_view_ngo;
      RecyclerView recyclerViewNgo = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewNgo == null) {
        break missingId;
      }

      id = R.id.tv_firstText;
      TextView tvFirstText = ViewBindings.findChildViewById(rootView, id);
      if (tvFirstText == null) {
        break missingId;
      }

      id = R.id.tv_secondText;
      TextView tvSecondText = ViewBindings.findChildViewById(rootView, id);
      if (tvSecondText == null) {
        break missingId;
      }

      return new HomeFragmentBinding((LinearLayout) rootView, donate, emptyView,
          recyclerViewDonations, recyclerViewNgo, tvFirstText, tvSecondText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
